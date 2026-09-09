package com.example.demo.Payment;

import com.example.demo.Transaction.Transaction;
import com.example.demo.Transaction.Transaction_Repo;
import com.example.demo.User.user;
import com.example.demo.User.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/payments")
public class payment_controller {

    @Autowired
    private userRepo userRepository;

    @Autowired
    private Transaction_Repo transactionRepository;

    // ගෙවීම් තහවුරු කරන OTP තාවකාලිකව තියාගන්න bucket එක
    private Map<String, String> paymentOtpStorage = new HashMap<>();

    @PostMapping("/initiate")
    public Map<String, String> initiatePayment(@RequestBody Map<String, Object> requestData) {
        String username = (String) requestData.get("username");
        double amount = Double.parseDouble(requestData.get("amount").toString());

        // 1. ඩේටාබේස් එකෙන් යූසර්ව හොයාගන්නවා
        user user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Map<String, String> response = new HashMap<>();

        // 2. එකවුන්ට් බැලන්ස් එක චෙක් කරනවා
        if (user.getAccountBalance() < amount) {
            response.put("status", "FAILED");
            response.put("message", "Insufficient account balance!");
            return response;
        }

        // 3. බැලන්ස් එක ඇති නම්, ගෙවීම තහවුරු කරන්න OTP එකක් හදනවා
        int randomPin = new Random().nextInt(1000000);
        String txOtp = String.format("%06d", randomPin);

        // යූසර්ගේ ෆෝන් නම්බර් එකට අදාළව මේ OTP එක සේဝ် කරනවා
        paymentOtpStorage.put(user.getPhoneNumber(), txOtp);

        // 🌟 FIXED LOGIC MAP: Changed from "OTP_SENT" to "OTP_REQUIRED" to align with payment.html
        response.put("status", "OTP_REQUIRED");
        response.put("simulatedSMS", "[PAYMENT ALERT] Enter code " + txOtp + " to pay LKR " + amount);

        return response;
    }

    @PostMapping("/confirm")
    public Map<String, Object> confirmPayment(@RequestBody Map<String, Object> requestData) {
        String username = (String) requestData.get("username");
        String enteredOtp = (String) requestData.get("otp");
        String cardNumber = (String) requestData.get("cardNumber");
        String billType = (String) requestData.get("billType");
        double amount = Double.parseDouble(requestData.get("amount").toString());

        String accountNumber = (String) requestData.get("accountNumber");
        String nicNumber = (String) requestData.get("nicNumber");
        String phoneNumber = (String) requestData.get("phoneNumber");

        // 1. ඩේටාබේස් එකෙන් යූසර්ගේ වත්මන් විස්තර ටික ගන්නවා
        user user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();

        // 2. Transaction OTP එක නිවැරදිද කියලා චෙක් කරනවා
        String correctOtp = paymentOtpStorage.get(user.getPhoneNumber());
        if (correctOtp == null || !correctOtp.equals(enteredOtp)) {
            response.put("status", "FAILED");
            response.put("message", "Security Alert: Invalid Payment OTP code!");
            return response;
        }

        // 3. Card Number එක චෙක් කරනවා (Simulated Rule - 4242 4242 4242 4242 නම් විතරක් Success)
        if (!cardNumber.replaceAll("\\s+", "").equals("4242424242424242")) {
            response.put("status", "FAILED");
            response.put("message", "Gateway Error: Card declined by the bank.");
            return response;
        }

        // 4. එකවුන්ට් බැලන්ස් එක අඩු කරන කොටස (Deduct Balance)
        double newBalance = user.getAccountBalance() - amount;
        user.setAccountBalance(newBalance);

        // Cloud Database එකේ යූසර්ගේ බැලන්ස් එක අප්ඩේට් වෙන්නේ මෙතනදී
        userRepository.save(user);

        // 5. මේ ගෙවීම් වාර්තාව Transaction Table එකේ සේව් කරන කොටස
        Transaction tx = new Transaction();
        tx.setUsername(username);
        tx.setBillType(billType);
        tx.setAmount(amount);
        tx.setStatus("SUCCESS");
        tx.setTimestamp(LocalDateTime.now());

        tx.setAccountNumber(accountNumber);
        tx.setNicNumber(nicNumber);
        tx.setPhoneNumber(phoneNumber);

        // Cloud Database එකේ transactions table එකට සේව් වෙනවා
        transactionRepository.save(tx);

        // වැඩේ ඉවර නිසා මේ OTP එක storage එකෙන් අයින් කරනවා
        paymentOtpStorage.remove(user.getPhoneNumber());

        // 6. Response එක සහ Simulated SMS එක සකස් කිරීම
        response.put("status", "SUCCESS");
        response.put("message", "Payment processed successfully!");
        response.put("simulatedSMS", "[BANK ALERT] Debited LKR " + amount + " for " + billType + ". New Balance: LKR " + newBalance);

        return response;
    }
    @GetMapping("/receipt-summary")
    public Map<String, Object> getReceiptSummary(@RequestParam String username) {
        // 1. Get the current user account balance
        user currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User context missing"));

        // 2. Fetch the most recent success transaction for this user
        // Spring Data JPA lets us find by custom criteria.
        // To keep it simple, we can filter all records or look them up manually.
        List<Transaction> allTransactions = transactionRepository.findAll();
        Transaction latestTx = null;

        // Filter for the absolute last successful record belonging to this user
        for (int i = allTransactions.size() - 1; i >= 0; i--) {
            Transaction t = allTransactions.get(i);
            if (t.getUsername().equals(username) && "SUCCESS".equals(t.getStatus())) {
                latestTx = t;
                break;
            }
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("currentBalance", currentUser.getAccountBalance());

        if (latestTx != null) {
            summary.put("billType", latestTx.getBillType());
            summary.put("amountPaid", latestTx.getAmount());
            summary.put("accountNo", latestTx.getAccountNumber());
            summary.put("nicNo", latestTx.getNicNumber());
            summary.put("phoneNo", latestTx.getPhoneNumber());
            summary.put("timestamp", latestTx.getTimestamp().toString());
        }

        return summary;
    }

}
