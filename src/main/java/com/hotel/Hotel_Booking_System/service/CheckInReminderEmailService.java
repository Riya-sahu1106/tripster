package com.hotel.Hotel_Booking_System.service;

import com.hotel.Hotel_Booking_System.model.Booking;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CheckInReminderEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendThreeDayReminderEmail(Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(booking.getUser().getEmail());
            helper.setSubject("⏰ Check-in in 3 Days – " + booking.getHotel().getName() + " | Tripster");
            helper.setText(buildThreeDayHtml(booking), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("[CheckInReminder] 3-day email failed: " + e.getMessage());
        }
    }

    @Async
    public void sendOneDayReminderEmail(Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(booking.getUser().getEmail());
            helper.setSubject("🚨 Tomorrow is Check-in Day! – " + booking.getHotel().getName() + " | Tripster");
            helper.setText(buildOneDayHtml(booking), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("[CheckInReminder] 1-day email failed: " + e.getMessage());
        }
    }

    private String buildThreeDayHtml(Booking booking) {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
        if (nights == 0) nights = 1;
        return """
            <div style="font-family:Segoe UI,sans-serif;max-width:600px;margin:auto;border-radius:12px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.1)">
              <div style="background:linear-gradient(135deg,#f7971e,#ffd200);padding:30px;text-align:center">
                <h1 style="color:white;margin:0">⏰ 3 Days to Go!</h1>
                <p style="color:white;margin:8px 0 0">Your hotel stay is just around the corner</p>
              </div>
              <div style="padding:30px;background:white">
                <p>Hi <strong>%s</strong>, your check-in at <strong>%s</strong> is in <strong>3 days</strong>!</p>
                <div style="background:#fff8e1;border:2px dashed #f7971e;border-radius:10px;padding:15px;text-align:center;margin:15px 0">
                  <div style="font-size:11px;color:#888;text-transform:uppercase">Booking Reference</div>
                  <div style="font-size:22px;font-weight:bold;color:#e65c00;font-family:monospace">%s</div>
                </div>
                <table style="width:100%%;border-collapse:collapse;margin:15px 0">
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">🏨 Hotel</td><td style="padding:10px;font-weight:bold">%s</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">📍 City</td><td style="padding:10px">%s</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">📅 Check-in</td><td style="padding:10px;color:#28a745;font-weight:bold">%s</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">📅 Check-out</td><td style="padding:10px">%s</td></tr>
                  <tr><td style="padding:10px;color:#888">🌙 Nights</td><td style="padding:10px">%d night(s)</td></tr>
                </table>
                <div style="background:#e8f5e9;border-radius:8px;padding:15px;margin:15px 0">
                  <strong style="color:#2e7d32">✅ Pre-check-in Checklist</strong>
                  <ul style="color:#444;line-height:2;margin-top:8px">
                    <li>Carry a valid government-issued photo ID</li>
                    <li>Keep your booking reference handy</li>
                    <li>Check hotel check-in time (usually 2:00 PM)</li>
                  </ul>
                </div>
                <a href="http://localhost:8080/my-bookings" style="display:inline-block;margin-top:10px;padding:12px 28px;background:#f7971e;color:white;text-decoration:none;border-radius:8px;font-weight:bold">View My Booking →</a>
              </div>
              <div style="background:#f8f9fa;padding:15px;text-align:center;color:#888;font-size:12px">© 2025 Tripster. All rights reserved.</div>
            </div>
            """.formatted(
                booking.getUser().getName(), booking.getHotel().getName(),
                booking.getBookingRef(), booking.getHotel().getName(),
                booking.getHotel().getCity(), booking.getCheckIn(),
                booking.getCheckOut(), nights);
    }

    private String buildOneDayHtml(Booking booking) {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
        if (nights == 0) nights = 1;
        return """
            <div style="font-family:Segoe UI,sans-serif;max-width:600px;margin:auto;border-radius:12px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.1)">
              <div style="background:linear-gradient(135deg,#1a73e8,#0d47a1);padding:30px;text-align:center">
                <h1 style="color:white;margin:0">🚨 Check-in Tomorrow!</h1>
                <p style="color:#b3d4ff;margin:8px 0 0">Get ready for an amazing stay</p>
              </div>
              <div style="padding:30px;background:white">
                <p>Hi <strong>%s</strong>, your check-in at <strong>%s</strong> is <strong>tomorrow</strong>! 🎉</p>
                <div style="background:#e8f0fe;border:2px dashed #1a73e8;border-radius:10px;padding:15px;text-align:center;margin:15px 0">
                  <div style="font-size:11px;color:#888;text-transform:uppercase">Booking Reference</div>
                  <div style="font-size:22px;font-weight:bold;color:#0d47a1;font-family:monospace">%s</div>
                </div>
                <table style="width:100%%;border-collapse:collapse;margin:15px 0">
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">🏨 Hotel</td><td style="padding:10px;font-weight:bold">%s</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">📍 City</td><td style="padding:10px">%s</td></tr>
                  <tr style="background:#e8f0fe"><td style="padding:10px;color:#888;font-weight:bold">📅 Check-in</td><td style="padding:10px;color:#1a73e8;font-weight:bold">%s (TOMORROW)</td></tr>
                  <tr style="border-bottom:1px solid #eee"><td style="padding:10px;color:#888">📅 Check-out</td><td style="padding:10px">%s</td></tr>
                  <tr><td style="padding:10px;color:#888">🌙 Nights</td><td style="padding:10px">%d night(s)</td></tr>
                </table>
                <div style="background:#fff3cd;border-radius:8px;padding:15px;margin:15px 0;border-left:4px solid #ffc107">
                  <strong style="color:#856404">⚠️ Important Reminders</strong>
                  <ul style="color:#444;line-height:2;margin-top:8px">
                    <li>Standard check-in time: <strong>2:00 PM</strong></li>
                    <li>Carry original government-issued photo ID</li>
                    <li>Keep booking reference for smooth check-in</li>
                  </ul>
                </div>
                <a href="http://localhost:8080/my-bookings" style="display:inline-block;margin-top:10px;padding:12px 28px;background:#1a73e8;color:white;text-decoration:none;border-radius:8px;font-weight:bold">View My Booking →</a>
              </div>
              <div style="background:#f8f9fa;padding:15px;text-align:center;color:#888;font-size:12px">© 2025 Tripster. All rights reserved.</div>
            </div>
            """.formatted(
                booking.getUser().getName(), booking.getHotel().getName(),
                booking.getBookingRef(), booking.getHotel().getName(),
                booking.getHotel().getCity(), booking.getCheckIn(),
                booking.getCheckOut(), nights);
    }
}