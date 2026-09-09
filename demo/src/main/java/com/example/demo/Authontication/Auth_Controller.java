package com.example.demo.Authontication;
import com.example.demo.User.user;
import com.example.demo.User.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/auth")
public class Auth_Controller {
    @Autowired
    private userRepo userRepo;
    private Map<String,String > otpstorage=new HashMap<>();


    @PostMapping("/register")
    public Map<String,String> regiterUser(@RequestBody Map<String, String> requestData){
        String useranme=requestData.get("username");
        String phone=requestData.get("phoneNumber");
        String password=requestData.get("password");

        user newUser= new user();
        newUser.setUsername(useranme);
        newUser.setPhoneNumber(phone);
        newUser.setPassword(password);



        int randomPin=new Random().nextInt(1000000);
        String otp =String.format("%06d",randomPin);




        Map<String,String> response=new HashMap<>();
        response.put("status","success");
        response.put("massage", "OTP code sent to "+ phone);
        response.put("simulatedSMS "," Your OTP is "+otp);

        userRepo.save(newUser);
        otpstorage.put(phone, otp);
        return response;
    }
    @PostMapping("verify-otp")
    public Map<String ,String> verifyOtp(@RequestBody Map<String, String> requestData){

        String phone=requestData.get("phoneNumber");
        String enteredOtp=requestData.get("otp");

        String coretOtp=otpstorage.get(phone);
        Map<String,String> response=new HashMap<>();

        if(coretOtp != null && coretOtp.equals(enteredOtp)){
            response.put("status","SUCCESS");
            response.put("massage","Verified!");
        }
        else{
            response.put("status","FAILED");
            response.put("massage", "Invaild OTP !");
        }
        return response;
    }
    @PostMapping("/login")
    public Map<String,String> login(@RequestBody Map<String, String> requestData){
        String identifier=requestData.get("identifier");
        String password=requestData.get("password");

        Optional<user> user=userRepo.findByUsername(identifier).or(() -> userRepo.findByphoneNumber(identifier));

        Map<String,String> response=new HashMap<>();
        if(user.isPresent()&& user.get().getPassword().equals(password)){
            response.put("status","SUCCESS");
            response.put("massage","Login Success!");
            response.put("username",user.get().getUsername());
        }
        else{
            response.put("status","FAILED");
            response.put("massage", "Invalid Credentials !");
        }
        return response;


    }
    @PostMapping("/regenerate-otp")
    public Map<String, String> regenerateOtp(@RequestBody Map<String, String> requestData) {
        String phone = requestData.get("phoneNumber");

        // Generate a fresh code
        int randomPin = new Random().nextInt(1000000);
        String freshOtp = String.format("%06d", randomPin);

        // Replace the old key in our in-memory map
        otpstorage.put(phone, freshOtp);

        Map<String, String> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("simulatedSMS", "[REGENERATED SMS] Your fresh OTP is: " + freshOtp);
        return response;
    }



}



