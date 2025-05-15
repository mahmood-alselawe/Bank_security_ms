# 🏦 Secure Banking Application

This is a Spring Boot application that handles user authentication, authorization, OTP verification, and basic banking operations like account creation, transfer, deposit, debit, and transaction logging. It includes a built-in Swagger UI for testing the APIs.

## 🌐 Server Environment

- **Base URL (Development):** `http://localhost:8080`
- **API Documentation:** `http://localhost:8080/swagger-ui/index.html`

---

## ✅ Features

- Secure user registration and login
- JWT-based authentication
- Email verification and OTP flow
- Password reset functionality
- Bank account creation and management
- Money transfer, deposit, and debit
- Lock and unlock bank accounts
- Transaction logs
- Swagger UI for API testing

---



## 🔗 API Endpoints

### Auth Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/auth/register` | Register a new user |
| `POST` | `/api/v1/auth/login` | Log in user |
| `POST` | `/api/v1/auth/refresh-token` | Refresh JWT token |
| `POST` | `/api/v1/auth/verifyMail/{email}` | Verify user's email |
| `POST` | `/api/v1/auth/verifyOtp/{otp}/{email}` | Verify OTP for user |
| `POST` | `/api/v1/auth/changePassword/{email}` | Reset password via email |

---
## 🔐 Forgot Password Flow

To implement a forgot password feature, follow these steps:

1. **Verify Email**
   - **Endpoint:** `POST /api/v1/auth/verifyMail/{email}`
   - **Purpose:** Check if the user exists and send an OTP to the provided email.

2. **Verify OTP**
   - **Endpoint:** `POST /api/v1/auth/verifyOtp/{otp}/{email}`
   - **Purpose:** Verify the OTP sent to the user's email.

3. **Change Password**
   - **Endpoint:** `POST /api/v1/auth/changePassword/{email}`
   - **Purpose:** Reset the user password after OTP verification.

---


### User Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| `PATCH` | `/api/v1/users/changePassword` | Authenticated user password change |

---

### OTP Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/otp/generate` | Generate and send OTP to email |

---

### Bank Controller

> **Note:** To use `Transfer`, `Deposit`, and `Debit` operations, you need to generate and verify an OTP first using the OTP API.

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/bank/transfer` | Transfer money (OTP required) |
| `POST` | `/api/v1/bank/deposit` | Deposit money (OTP required) |
| `POST` | `/api/v1/bank/debit` | Debit (withdraw) money (OTP required) |
| `POST` | `/api/v1/bank/create/{email}` | Create bank account |
| `POST` | `/api/v1/bank/create/account/{email}` | Alternate account creation with image |
| `POST` | `/api/v1/bank/accounts/unlock` | Unlock user account |
| `POST` | `/api/v1/bank/accounts/lock` | Lock user account |
| `GET` | `/api/v1/bank/nameEnquiry` | Get account name by number |
| `GET` | `/api/v1/bank/findByAccountNumber` | Get details by account number |
| `GET` | `/api/v1/bank/balanceEnquiry` | Check account balance |

---

### Transaction Log Controller

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/bankLog/transactions` | Retrieve user transaction logs |

---

## ⚙️ Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Security
- JWT (JSON Web Tokens)
- Swagger/OpenAPI
- Lombok
- Maven
- java mail sender (for send Email)
- AWS S3 (for save image in AWS)

---

## 🙏 Thank You

Thank you for using this Secure Banking Application!  
If you have any questions or feedback, feel free to open an issue or reach out.

---

## 🧾 License

This project is licensed under the MIT License.

---

## 👤 Author


**Mahmood Alselawe** 
**mahmoodselawe5@gmail.com** 
[GitHub Repository](https://github.com/mahmood-alselawe/Bank_security_ms)

