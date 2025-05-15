# 🏦 Bank Security Microservice

This is a Spring Boot-based backend microservice for handling secure banking operations such as account creation, fund transfers, authentication, and transaction logs. It integrates secure OTP verification, user authentication, and transaction management using RESTful APIs.

## 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Security & JWT
- Swagger (OpenAPI)
- Maven
- AWS SDK (Configured securely)
- MySQL (or any JDBC-compatible DB)
- Lombok

## 📌 Project Structure

- `auth-controller`: Handles registration, login, password changes, and email/OTP verification.
- `bank-controller`: Manages banking operations such as deposits, withdrawals, transfers, and balance checks.
- `user-controller`: Handles user profile and security features.
- `transaction-log-controller`: Logs and fetches transaction history.
- `otp-controller`: Generates OTPs for secure transactions.

## 🧪 API Documentation

> Swagger UI available at:  
> **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### 🔐 Auth Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register a new user |
| POST | `/api/v1/auth/login` | Authenticate and receive a JWT |
| POST | `/api/v1/auth/refresh-token` | Refresh access token |
| POST | `/api/v1/auth/changePassword/{email}` | Change password by email |
| POST | `/api/v1/auth/verifyOtp/{otp}/{email}` | Verify OTP for a user |
| POST | `/api/v1/auth/verifyMail/{email}` | Verify a user's email address |

---

### 🏦 Bank Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/bank/transfer` | Transfer funds between accounts |
| POST | `/api/v1/bank/deposit` | Deposit funds into an account |
| POST | `/api/v1/bank/debit` | Withdraw funds from an account |
| POST | `/api/v1/bank/create/{email}` | Create a bank profile for a user |
| POST | `/api/v1/bank/create/account/{email}` | Create a bank account |
| POST | `/api/v1/bank/accounts/unlock` | Unlock a user’s account |
| POST | `/api/v1/bank/accounts/lock` | Lock a user’s account |
| GET  | `/api/v1/bank/nameEnquiry` | Check account holder name |
| GET  | `/api/v1/bank/findByAccountNumber` | Find account details by account number |
| GET  | `/api/v1/bank/balanceEnquiry` | Get account balance info |

---

### 📲 OTP Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/otp/generate` | Generate OTP for verification |

---

### 👤 User Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| PATCH | `/api/v1/users/changePassword` | Change password for logged-in user |

---

### 📜 Transaction Log Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/bankLog/transactions` | Fetch user's transaction history |

---

## 🔧 How to Run Locally

### Prerequisites

- Java 17+
- Maven
- MySQL or your configured DB

### Steps

```bash
# Clone the repo
git clone https://github.com/mahmood-alselawe/Bank_security_ms.git
cd Bank_security_ms

# Run the application
mvn spring-boot:run
