package com.hotel.Hotel_Booking_System.controller;

import com.hotel.Hotel_Booking_System.model.User;
import com.hotel.Hotel_Booking_System.repository.UserRepository;
import com.hotel.Hotel_Booking_System.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ── Step 1: Forgot Password Page ──
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    // ── Step 2: Send OTP ──
    @PostMapping("/forgot-password")
    public String sendOtp(@RequestParam("email") String email,
                          HttpSession session,
                          Model model) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "No account found with this email.");
            return "forgot-password";
        }

        // 6 digit OTP generate karo
        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        // Session mein store karo
        session.setAttribute("resetOtp", otp);
        session.setAttribute("resetEmail", email);
        session.setAttribute("otpExpiry", System.currentTimeMillis() + 15 * 60 * 1000); // 15 min

        
        // Email bhejo
        emailService.sendOtpEmail(email, otp, user.getName());

        model.addAttribute("success", "OTP sent to " + email);
        return "verify-otp";
    }

    // ── Step 3: Verify OTP Page ──
    @GetMapping("/verify-otp")
    public String verifyOtpPage() {
        return "verify-otp";
    }

    // ── Step 4: Verify OTP Submit ──
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,
                            HttpSession session,
                            Model model) {

        String savedOtp    = (String) session.getAttribute("resetOtp");
        Long   expiry      = (Long)   session.getAttribute("otpExpiry");

        if (savedOtp == null || expiry == null) {
            model.addAttribute("error", "Session expired. Please try again.");
            return "forgot-password";
        }

        if (System.currentTimeMillis() > expiry) {
            model.addAttribute("error", "OTP expired. Please request a new one.");
            return "forgot-password";
        }

        if (!savedOtp.equals(otp)) {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            return "verify-otp";
        }

        // OTP sahi — reset password page pe bhejo
        session.setAttribute("otpVerified", true);
        return "reset-password";
    }

    // ── Step 5: Reset Password Page ──
    @GetMapping("/reset-password")
    public String resetPasswordPage(HttpSession session) {
        Boolean verified = (Boolean) session.getAttribute("otpVerified");
        if (verified == null || !verified) return "redirect:/forgot-password";
        return "reset-password";
    }

    // ── Step 6: Reset Password Submit ──
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword,
                                HttpSession session,
                                Model model) {

        Boolean verified = (Boolean) session.getAttribute("otpVerified");
        String  email    = (String)  session.getAttribute("resetEmail");

        if (verified == null || !verified || email == null) {
            return "redirect:/forgot-password";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "reset-password";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "reset-password";
        }

        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }

        // Session clear karo
        session.removeAttribute("resetOtp");
        session.removeAttribute("resetEmail");
        session.removeAttribute("otpExpiry");
        session.removeAttribute("otpVerified");

        return "redirect:/login?resetSuccess=true";
    }
}