package com.hotel.Hotel_Booking_System.service;

import com.hotel.Hotel_Booking_System.model.Booking;
import com.hotel.Hotel_Booking_System.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // 1. Welcome Email on Signup
    @Async
    public void sendWelcomeEmail(User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to Tripster - Your Journey Begins!");
            helper.setText(buildWelcomeHtml(user.getName()), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Welcome email failed: " + e.getMessage());
        }
    }

    // 2. Booking Confirmation Email
    @Async
    public void sendBookingConfirmationEmail(Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(booking.getUser().getEmail());
            helper.setSubject("Booking Confirmed - " + booking.getHotel().getName() + " | Tripster");
            helper.setText(buildBookingHtml(booking), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Booking email failed: " + e.getMessage());
        }
    }

    // 3. Cancellation Email
    @Async
    public void sendCancellationEmail(Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(booking.getUser().getEmail());
            helper.setSubject("Booking Cancelled - " + booking.getHotel().getName() + " | Tripster");
            helper.setText(buildCancelHtml(booking), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Cancellation email failed: " + e.getMessage());
        }
    }

    private String buildWelcomeHtml(String name) {
        return """
            <div style="font-family:Segoe UI,sans-serif;max-width:600px;margin:auto;border-radius:12px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.1)">
              <div style="background:linear-gradient(135deg,#1a73e8,#0d47a1);padding:30px;text-align:center">
                <h1 style="color:white;margin:0">🏨 Tripster</h1>
              </div>
              <div style="padding:30px;background:white">
                <h2 style="color:#1a73e8">Welcome, %s! 🎉</h2>
                <p style="color:#444">Thank you for joining Tripster! You can now:</p>
                <ul style="color:#444;line-height:2">
                  <li>🔍 Search hotels by city</li>
                  <li>📅 Book hotels instantly</li>
                  <li>🤖 Chat with AI assistant for travel tips</li>
                  <li>⭐ Read and write reviews</li>
                  <li>❌ Cancel bookings anytime</li>
                </ul>
                <a href="http://localhost:8080/home" style="display:inline-block;margin-top:20px;padding:12px 28px;background:#1a73e8;color:white;text-decoration:none;border-radius:8px;font-weight:bold">Explore Hotels →</a>
              </div>
              <div style="background:#f8f9fa;padding:15px;text-align:center;color:#888;font-size:12px">© 2025 Tripster. All rights reserved.</div>
            </div>
            """.formatted(name);
    }

    private String buildBookingHtml(Booking booking) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
        if (days == 0) days = 1;
        return """
            <div style="font-family:Segoe UI,sans-serif;max-width:600px;margin:auto;border-radius:12px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.1)">
              <div style="background:linear-gradient(135deg,#28a745,#1e7e34);padding:30px;text-align:center">
                <h1 style="color:white;margin:0">✅ Booking Confirmed!</h1>
              </div>
              <div style="padding:30px;background:white">
                <p style="color:#444">Hi <strong>%s</strong>, your booking is confirmed!</p>
                <div style="background:#e8f5e9;border:2px dashed #28a745;border-radius:10px;padding:15px;text-align:center;margin:15px 0">
                  <div style="font-size:11px;color:#555;text-transform:uppercase;letter-spacing:1px">Booking Reference</div>
                  <div style="font-size:22px;font-weight:bold;color:#1e7e34;font-family:monospace">%s</div>
                </div>
                <table style="width:100%%;border-collapse:collapse;margin:15px 0">
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">🏨 Hotel</td><td style="padding:10px;font-weight:bold">%s</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">📍 City</td><td style="padding:10px">%s</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">📅 Check-in</td><td style="padding:10px">%s</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">📅 Check-out</td><td style="padding:10px">%s</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">🌙 Nights</td><td style="padding:10px">%d night(s)</td></tr>
                  <tr style="background:#f0f7ff"><td style="padding:10px;color:#888;font-weight:bold">💰 Total</td><td style="padding:10px;font-size:18px;font-weight:bold;color:#1a73e8">₹ %.2f</td></tr>
                </table>
                <p style="color:#555;font-size:13px;background:#fff8e1;padding:12px;border-radius:8px">💡 You can cancel this booking anytime from <em>My Bookings</em> section.</p>
                <a href="http://localhost:8080/my-bookings" style="display:inline-block;margin-top:15px;padding:12px 28px;background:#28a745;color:white;text-decoration:none;border-radius:8px;font-weight:bold">View My Bookings →</a>
              </div>
              <div style="background:#f8f9fa;padding:15px;text-align:center;color:#888;font-size:12px">© 2025 Tripster. All rights reserved.</div>
            </div>
            """.formatted(booking.getUser().getName(), booking.getBookingRef(),
                booking.getHotel().getName(), booking.getHotel().getCity(),
                booking.getCheckIn(), booking.getCheckOut(), days, booking.getTotalPrice());
    }

    private String buildCancelHtml(Booking booking) {
        return """
            <div style="font-family:Segoe UI,sans-serif;max-width:600px;margin:auto;border-radius:12px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.1)">
              <div style="background:linear-gradient(135deg,#dc3545,#c82333);padding:30px;text-align:center">
                <h1 style="color:white;margin:0">❌ Booking Cancelled</h1>
              </div>
              <div style="padding:30px;background:white">
                <p style="color:#444">Hi <strong>%s</strong>, your booking has been cancelled.</p>
                <div style="background:#fdecea;border:2px dashed #dc3545;border-radius:10px;padding:15px;text-align:center;margin:15px 0">
                  <div style="font-size:11px;color:#555;text-transform:uppercase;letter-spacing:1px">Cancelled Booking</div>
                  <div style="font-size:22px;font-weight:bold;color:#c82333;font-family:monospace">%s</div>
                </div>
                <table style="width:100%%;border-collapse:collapse;margin:15px 0">
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">🏨 Hotel</td><td style="padding:10px;font-weight:bold">%s</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">📅 Check-in</td><td style="padding:10px">%s</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">📅 Check-out</td><td style="padding:10px">%s</td></tr>
                  <tr style="background:#fdecea"><td style="padding:10px;color:#888">💰 Refund</td><td style="padding:10px;font-weight:bold;color:#c82333">₹ %.2f (5-7 business days)</td></tr>
                </table>
                <a href="http://localhost:8080/home" style="display:inline-block;margin-top:15px;padding:12px 28px;background:#1a73e8;color:white;text-decoration:none;border-radius:8px;font-weight:bold">Browse Hotels Again →</a>
              </div>
              <div style="background:#f8f9fa;padding:15px;text-align:center;color:#888;font-size:12px">© 2025 Tripster. All rights reserved.</div>
            </div>
            """.formatted(booking.getUser().getName(), booking.getBookingRef(),
                booking.getHotel().getName(), booking.getCheckIn(), booking.getCheckOut(),
                booking.getTotalPrice());
    }
    @Async
    public void sendOtpEmail(String toEmail, String otp, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Password Reset OTP - Tripster");
            helper.setText(buildOtpHtml(userName, otp), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("OTP email failed: " + e.getMessage());
        }
    }

    private String buildOtpHtml(String name, String otp) {
        return "<div style='font-family:Arial;max-width:500px;margin:auto;padding:20px;"
             + "border:1px solid #ddd;border-radius:10px;'>"
             + "<h2 style='color:#4facfe;text-align:center;'>Tripster</h2>"
             + "<h3>Hello, " + name + "!</h3>"
             + "<p>Your password reset OTP is:</p>"
             + "<div style='font-size:36px;font-weight:bold;text-align:center;"
             + "color:#0d6efd;letter-spacing:10px;padding:20px;background:#f0f8ff;"
             + "border-radius:8px;'>" + otp + "</div>"
             + "<p style='color:#888;margin-top:15px;'>This OTP is valid for <b>15 minutes</b>.</p>"
             + "<p style='color:#888;'>If you did not request this, ignore this email.</p>"
             + "</div>";
    }
}
