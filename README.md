# 🏨 Tripster 

A full-stack web application built with **Spring Boot** and **Thymeleaf** that allows users to search, book, and manage hotel reservations. Features include AI-powered hotel assistant, automated email notifications, PDF booking confirmations, and a complete admin dashboard.

---

## ✨ Features

### User Features
- User registration, login, and profile management
- Browse and search hotels with detailed listings
- Room booking with check-in / check-out date selection
- Payment flow with UPI support
- PDF booking confirmation download
- Save / Wishlist favourite hotels
- Write and manage hotel reviews & ratings
- View booking history
- Forgot password with OTP email verification
- Automated check-in reminder emails

### Admin Features
- Admin dashboard with overview statistics
- Add, edit, and delete hotels
- Manage all user bookings
- Manage registered users

### AI Assistant
- AI-powered hotel chatbot (Groq API — LLaMA 3.3)
- Answers questions about hotels, bookings, travel tips
- Context-aware responses based on available hotel data

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 4.0, Spring Data JPA |
| Frontend | Thymeleaf, HTML, CSS |
| Database | MySQL |
| Security | Spring Security |
| AI | Groq API (LLaMA 3.3 70B) |
| Email | Spring Mail (Gmail SMTP) |
| PDF | OpenPDF |
| Other | Lombok, Maven, Docker |

---

## 📁 Project Structure

```
src/
├── ai/
│   └── AIService.java                   # Groq AI API integration
├── config/
│   ├── AppConfig.java
│   └── SecurityConfig.java              # Spring Security config
├── controller/
│   ├── AdminController.java             # Admin panel
│   ├── BookingController.java           # Booking flow
│   ├── PageController.java              # Main pages
│   ├── ReviewController.java            # Reviews
│   ├── SavedController.java             # Saved/wishlist hotels
│   └── ForgotPasswordController.java    # OTP password reset
├── model/
│   ├── Hotel.java
│   ├── Booking.java
│   ├── User.java
│   ├── Review.java
│   └── SavedHotel.java
├── repository/
│   ├── HotelRepository.java
│   ├── BookingRepository.java
│   ├── UserRepository.java
│   ├── ReviewRepository.java
│   └── SavedHotelRepository.java
├── service/
│   ├── BookingService.java
│   ├── AdminService.java
│   ├── EmailService.java                # Email notifications
│   ├── PdfService.java                  # PDF generation
│   ├── ReviewService.java
│   ├── CheckInReminderScheduler.java    # Scheduled reminders
│   └── RefundScheduler.java
└── templates/                           # Thymeleaf HTML pages
    ├── index.html
    ├── booking.html
    ├── my-bookings.html
    ├── hotel-details.html
    ├── admin-dashboard.html
    ├── admin-hotels.html
    ├── admin-bookings.html
    ├── admin-users.html
    ├── login.html
    ├── signup.html
    └── ...
```

---

## ⚙️ Setup Instructions

### Prerequisites
- Java 21
- MySQL
- Maven

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/Riya-sahu1106/tripster.git
cd tripster
```

**2. Create MySQL database**
```sql
CREATE DATABASE hotel_db;
```

**3. Configure environment variables**

Set the following environment variables on your system or add directly to `application.properties`:

```
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
GROQ_API_KEY=your_groq_api_key
MAIL_USERNAME=your_gmail_address
MAIL_PASSWORD=your_gmail_app_password
```

**4. Run the application**
```bash
mvn spring-boot:run
```

**5. Open in browser**
```
http://localhost:8080
```

---

## 🐳 Run with Docker

```bash
docker build -t tripster .
docker run -p 8080:8080 \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=yourpassword \
  -e GROQ_API_KEY=yourkey \
  -e MAIL_USERNAME=youremail \
  -e MAIL_PASSWORD=yourpassword \
  tripster
```

---

## 🔑 Getting API Keys

**Groq API Key (Free)**
1. Go to [console.groq.com](https://console.groq.com)
2. Sign up and create an API key

**Gmail App Password**
1. Go to your Google Account → Security
2. Enable 2-Step Verification
3. Generate an App Password for Mail

---

## 📸 Pages Overview

| Page | Description |
|------|-------------|
| `/` | Home page with hotel listings |
| `/hotel/{id}` | Hotel details page |
| `/booking/{id}` | Booking form |
| `/my-bookings` | User booking history |
| `/saved` | Saved/wishlist hotels |
| `/account` | User profile |
| `/admin/dashboard` | Admin overview |
| `/admin/hotels` | Manage hotels |
| `/admin/bookings` | Manage bookings |
| `/admin/users` | Manage users |
