# 🏦 Bank Security Microservice

This project is a Spring Boot-based microservice providing authentication, authorization, bank operations (deposit, transfer, etc.), OTP validation, and user account management. It includes integrated Swagger UI for API documentation.

## 📌 Server Information

- **Base URL (Dev):** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

---

## 📂 Project Features

- 🔐 User Authentication and Authorization
- 🔁 JWT Token Refresh
- 📧 Email Verification and OTP for secure login and password reset
- 💳 Bank account management (create, lock, unlock)
- 💰 Transactions (deposit, debit, transfer)
- 📜 Transaction Logs
- 📊 Swagger API Documentation

---

## 🚀 API Endpoints

### 🔐 Auth Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/auth/register` | Register a new user |
| `POST` | `/api/v1/auth/login` | Log in with email and password |
| `POST` | `/api/v1/auth/refresh-token` | Refresh the access token |
| `POST` | `/api/v1/auth/verifyOtp/{otp}/{email}` | Verify OTP for a user |
| `POST` | `/api/v1/auth/verifyMail/{email}` | Verify a user's email |
| `POST` | `/api/v1/auth/changePassword/{email}` | Change password using email |

---

### 🔄 Forgot Password Flow

1. **Verify Email**  
   `POST /api/v1/auth/verifyMail/{email}`  
   → Check if user exists and send OTP to the email.

2. **Verify OTP**  
   `POST /api/v1/auth/verifyOtp/{otp}/{email}`  
   → Validate the OTP for the provided email.

3. **Reset Password**  
   `POST /api/v1/auth/changePassword/{email}`  
   → Update the password after successful OTP verification.

---

### 🏦 Bank Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/bank/transfer` | Transfer money between accounts |
| `POST` | `/api/v1/bank/deposit` | Deposit funds to an account |
| `POST` | `/api/v1/bank/debit` | Withdraw funds from an account |
| `POST` | `/api/v1/bank/create/{email}` | Create a bank account for a user |
| `POST` | `/api/v1/bank/create/account/{email}` | Alternate endpoint for creating account |
| `POST` | `/api/v1/bank/accounts/unlock` | Unlock a user’s account |
| `POST` | `/api/v1/bank/accounts/lock` | Lock a user’s account |
| `GET` | `/api/v1/bank/nameEnquiry` | Get account name by number |
| `GET` | `/api/v1/bank/findByAccountNumber` | Find account details by number |
| `GET` | `/api/v1/bank/balanceEnquiry` | Get account balance |

---

### 📬 OTP Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/otp/generate` | Generate and send OTP to user email |

---

### 👤 User Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| `PATCH` | `/api/v1/users/changePassword` | Change password for a logged-in user |

---

### 📈 Transaction Log Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/bankLog/transactions` | Fetch user’s transaction logs |

---

## 🧰 Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Security
- JWT (JSON Web Tokens)
- Swagger / OpenAPI
- AWS SES (for sending OTPs via email)
- Lombok
- Maven

---

## 📄 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

**Mahmood Alselawe**  
[GitHub Repository](https://github.com/mahmood-alselawe/Bank_security_ms)

