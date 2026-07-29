# 🔐 Spring Boot: User Authentication

A Spring Boot REST API application that demonstrates a complete **User Authentication System** using **JWT (JSON Web Token)**, **Spring Security**, and **Email-Based Password Setup**.

The application follows clean architecture principles with DTOs, request validation, global exception handling, BCrypt password encryption, secure email workflows, and MySQL integration.

This project serves as a solid foundation for building secure authentication systems in enterprise Spring Boot applications.

---

## ✨ Features

✔ User Registration

✔ Email-Based Password Setup

✔ User Login using JWT Authentication

✔ Reset Password

✔ View, Update and Delete User Profile

✔ JWT Authentication

✔ BCrypt Password Encryption

✔ Spring Security Integration

✔ Stateless Authentication

✔ HTML Email Templates

✔ Global Exception Handling

✔ DTO-Based Request & Response

---

## 🛠️ Tech Stack

| Category | Technologies |
|----------|--------------|
| **Backend** | Java 17, Spring Boot 3, Spring Security |
| **Authentication** | JWT (JSON Web Token), BCrypt Password Encoder |
| **Database** | MySQL, Spring Data JPA, Hibernate |
| **Email** | Spring Mail (JavaMailSender), HTML Email Templates |
| **Validation** | Jakarta Bean Validation |
| **Build Tool** | Maven |
| **API Testing** | Postman |
| **IDE** | IntelliJ IDEA |

---

## 📡 REST API Endpoints

| Method | Endpoint | Authentication | Description |
|---------|----------|---------------|-------------|
| POST | `/api/auth/register` | ❌ | Register a new user |
| POST | `/api/auth/set-password` | ❌ | Create password using email link |
| POST | `/api/auth/login` | ❌ | Login and receive JWT token |
| GET | `/api/auth/profile` | ✅ Bearer Token | Get logged-in user profile |
| PUT | `/api/auth/update/{id}` | ✅ Bearer Token | Update user profile |
| DELETE | `/api/auth/delete/{id}` | ✅ Bearer Token | Delete user account |

---

## ⚙️ How It Works

<p align="center">
  <img src="how_it_works.png" alt="Spring Boot Authentication Flow" width="100%">
</p>

---

## 🎯 Conclusion

This project showcases my understanding of modern authentication and backend development using Java and Spring Boot. It demonstrates secure API development with JWT, Spring Security, email-based password workflows, validation, exception handling, and clean architecture principles that are commonly used in enterprise applications.

---

## 👨‍💻 Author

**Pavan Sonawane**

GitHub: https://github.com/pavan0209

LinkedIn: https://www.linkedin.com/in/pavansonawane0209

---

<div align="center">

⭐ Thank You for Visiting This Repository ⭐

</div>