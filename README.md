# 🔒 Multi-Stage Secure Payment Gateway Simulator

A functional, production-modeged simulated Web Payment Gateway architecture built using **Java Spring Boot**, **HTML5/JavaScript**, and fully structured inside an enterprise-grade **Cloud Database (MySQL via Aiven Cloud)**. 

This repository was designed specifically for academic evaluation to demonstrate end-to-end user session tracking, secure data state management across separate page boundaries, and Multi-Factor Authentication (MFA) logic patterns during financial transactional loops.

---

## 🛠️ Tech Stack & Key Elements

- **Backend Logic Core:** Java 17 + Spring Boot 3.x (Web, Data JPA)
- **Persistent Infrastructure Data Layer:** Cloud Managed MySQL Instance (Aiven Cloud Services)
- **Frontend Presentation Layer:** Decoupled Semantic HTML5 + Vanilla Core JavaScript (Async/Fetch APIs)
- **Deployment Platform Architecture:** Containerized Application Cluster via Docker inside Render Cloud Services

---

## 🧭 Multi-Page Core Architecture Flow

The workflow is intentionally segmented across 5 isolated web interface nodes to prevent state pollution and preserve true system navigation tracking scopes:

1. **`index.html` (Home Entrance Node):** User Identity Authentication Gateway. Handles explicit credential inputs and reads active checking rows out of the cloud infrastructure storage cluster.
2. **`signup.html` (Identity Enrollment Node):** Collects standard credentials and initial mobile identity registrations. Drops unauthenticated session traces directly down onto the remote engine.
3. **`verify.html` (Device Out-of-Band Enrollment Node):** Implements simulated out-of-band mobile verification. Enforces hardware-linked possession confirmations via explicit token confirmation hooks. Integrates an adaptive key generation layer allowing manual verification token resets.
4. **`payment.html` (Dynamic Invoice Selection Node):** Dynamically alters form schemas based on context preferences (Telecommunication, Electricity, or Custom items). Grabs parameters (NIC, Mobile, Account Identifiers) and pushes them safely into local storage caches.
5. **`checkout.html` (Clearance node):** Collects raw transaction card targets and handles structural rule logic validation matches before reducing account balance variables on successful responses.
6. **`receipt.html` (Dynamic Invoicing Ledger Node):** Pulls the remaining account balance values and metadata strings out of the transaction repository logs to output an auditing statement summary.

---

## ⚠️ Architectural Weaknesses & Future Mitigations

Since this implementation prioritizes basic system functional components for university evaluation purposes, specific structural shortcuts were adopted. Below is a comprehensive list of architectural flaws and their corresponding enterprise-grade security mitigations:

### 1. Plaintext Password Dispositions
- **Current Weakness:** Passwords are committed and saved inside the remote cloud database table instances using pure plaintext strings. If an unprivileged cloud operator or threat actor compromises the database layer, consumer password credentials would be fully exposed.
- **Future Mitigation:** Implement strong asymmetric structural cryptography hashes. Integrate **Spring Security Core** using the **BCryptPasswordEncoder** cryptographic hashing engine to apply unique multi-round crypt-salts prior to storing arrays inside the database columns.

### 2. State Exposure inside Volatile LocalStorage Caches
- **Current Weakness:** Sensitive application tokens, transaction calculation parameters, and identification parameters (like NIC or Mobile records) are stored inside the browser's unencrypted `localStorage` cluster. Malicious scripts or Cross-Site Scripting (XSS) vectors could access these variables.
- **Future Mitigation:** Move state data out of volatile localized cache elements. Utilize cryptographically signed **JSON Web Tokens (JWT)** handled inside HTTP-Only, Secure, SameSite cookies to obscure active session values completely from clients.

### 3. Basic Hardcoded Testing Card Logic
- **Current Weakness:** The transaction validation rules depend on an explicit hardcoded testing sequence comparison condition match string (`4242424242424242`). Threat agents analyzing compilation assemblies could extract validation strings easily.
- **Future Mitigation:** Integrate deep structural Luhn Algorithm checks to algorithmically establish mathematical integrity parameters, or route validation calls directly through detached sandbox payment gateway endpoints (e.g., Stripe Sandbox).

### 4. Vulnerable In-Memory Token Management
- **Current Weakness:** One-Time Passwords (OTPs) are stored inside standard Java `HashMap` clusters residing entirely inside active RAM space. If the host environment reboots unexpectedly or drops network connection links, all active user authorization loops instantly crash.
- **Future Mitigation:** Transition system state memory management over to an independent data node caching layer such as **Redis Server Services**. Set explicit Time-To-Live (TTL) limits on transactional keys to guarantee data destruction after expiration.

### 5. Absence of True Transport Layer Protections
- **Current Weakness:** While connections to the Cloud Database run via forced SSL query strings, internal development routes are deployed across normal unencrypted endpoints which allows network traffic sniffers to view plain text contents.
- **Future Mitigation:** Enforce explicit application routing validations requiring **HTTPS Transport Security layers** bound under modern **TLS 1.3 protocol architectures**.

### 6. Vulnerability to Transaction Injection (Brute-Force Verification)
- **Current Weakness:** The API controller routes do not track verification execution limits. Malicious automation programs could script infinite verification calls to guess active 6-digit payment tokens.
- **Future Mitigation:** Inject automated protective tracking middleware to implement **Rate Limiting (Token Bucket Throttling Algorithms)** or introduce Captcha protection loops to disrupt non-human execution paths.

---

## 🚀 Execution & Local Boot Sequence

To instantiate the codebase environment stack within a localized execution context window inside IntelliJ IDEA:

1. Clone or import files securely onto a standard Java 17 workspace project path directory structure.
2. Provide your managed cloud database credential string variables directly within `src/main/resources/application.properties`.
3. Launch the central compilation lifecycle execution routine wrapper using the green run trigger button.
4. Point a clean browser layout scope instance onto the standard port deployment node destination context path location: `http://localhost:8080/index.html`.
