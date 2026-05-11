package com.hotel.Hotel_Booking_System.controller;

import java.time.LocalDate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.hotel.Hotel_Booking_System.ai.AIService;
import com.hotel.Hotel_Booking_System.model.*;
import com.hotel.Hotel_Booking_System.repository.*;
import com.hotel.Hotel_Booking_System.service.BookingService;
import com.hotel.Hotel_Booking_System.service.EmailService;
import com.hotel.Hotel_Booking_System.service.ReviewService;

@Controller
public class PageController {

    @Autowired private UserRepository userRepository;
    @Autowired private HotelRepository hotelRepository;
    @Autowired private ReviewService reviewService;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private AIService aiService;
    @Autowired private EmailService emailService;
    @Autowired private BookingService bookingService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String showHotels(@RequestParam(value = "city", required = false) String city, Model model) {

        List<Hotel> hotels = (city != null && !city.trim().isEmpty())
                ? hotelRepository.findByCityIgnoreCase(city)
                : hotelRepository.findAll();

        model.addAttribute("hotels", hotels);
        return "index";
    }

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }

    @PostMapping("/api/chat")
    @ResponseBody
    public ResponseEntity<Map<String, String>> chatAPI(@RequestBody Map<String, String> request) {

        String userMessage = request.get("message");

        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.ok(Map.of("reply", "Please type something! 😊"));
        }

        List<Hotel> hotels = hotelRepository.findAll();
        StringBuilder data = new StringBuilder("Available Hotels:\n");

        for (Hotel h : hotels) {
            data.append("- ").append(h.getName())
                .append(" in ").append(h.getCity())
                .append(", Price: ₹").append(h.getPrice()).append("\n");
        }

        String aiReply = aiService.getResponse(userMessage, data.toString());
        return ResponseEntity.ok(Map.of("reply", aiReply));
    }

    @PostMapping("/home")
    public String chatFromHome(@RequestParam("message") String message, Model model, HttpSession session) {

    	
        List<Hotel> hotels = hotelRepository.findAll();
        String response = aiService.getResponse(message, "Hotels data");

        model.addAttribute("userMessage", message);
        model.addAttribute("aiResponse", response);
        model.addAttribute("hotels", hotels);

        return "index";
    }

   
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("gmail") String gmail,
                            @RequestParam("password") String password,
                            HttpSession session) {

        User user = userRepository.findByEmail(gmail);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            session.setAttribute("loggedUser", user);
            return "redirect:/home";
        }
        return "login";
    }

    //  SIGNUP 
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }
    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            return "signup";
        }

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            model.addAttribute("emailError", "This email is already registered! Please login.");
            return "signup";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        emailService.sendWelcomeEmail(user);

        return "redirect:/login";
    }

    // REVIEW 
    @PostMapping("/addReview")
    public String addReview(@RequestParam("hotel_id") int hotel_id,
                            @RequestParam("rating") int rating,
                            @RequestParam("comment") String comment,
                            HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setUserName(user != null ? user.getName() : "Guest");

        Hotel hotel = new Hotel();
        hotel.setId((long) hotel_id);
        review.setHotel(hotel);

        reviewService.saveReview(review);

        return "redirect:/hotel/" + hotel_id;
    }

    //  HOTEL 
    @GetMapping("/hotel/{id}")
    public String hotelDetails(@PathVariable("id") Long id, Model model, HttpSession session) {

        Hotel hotel = hotelRepository.findById(id).orElse(null);

        model.addAttribute("hotel", hotel);
        model.addAttribute("reviews", reviewService.getReviewsByHotelId(id));
        model.addAttribute("avgRating", reviewService.getAverageRating(id));
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));

        return "hotel-details";
    }

    //  PROFILE 
    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam("id") Integer id,
                                @RequestParam("name") String name,
                                HttpSession session) {

        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            user.setName(name);
            userRepository.save(user);
            session.setAttribute("loggedUser", user);
        }

        return "redirect:/profile";
    }

    @GetMapping("/booking")
    public String bookingPage(@RequestParam Long hotelId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        model.addAttribute("hotel", hotel);       // ← Prices ke liye
        model.addAttribute("hotelId", hotelId);
        return "booking";
    }
    
    @GetMapping("/payment/{hotelId}")
    public String showPaymentPage(Model model,
                                  @PathVariable("hotelId") Long hotelId,
                                  @RequestParam("checkIn") String checkIn,
                                  @RequestParam("checkOut") String checkOut) {
 
        LocalDate inDate = LocalDate.parse(checkIn);
        LocalDate outDate = LocalDate.parse(checkOut);
 
        // ✅ DATE VALIDATION
        LocalDate today = LocalDate.now();
        if (inDate.isBefore(today)) {
            model.addAttribute("hotelId", hotelId);
            model.addAttribute("error", "Check-in date cannot be in the past.");
            return "booking";
        }
        if (!outDate.isAfter(inDate)) {
            model.addAttribute("hotelId", hotelId);
            model.addAttribute("error", "Check-out date must be after Check-in date.");
            return "booking";
        }
 
        long days = ChronoUnit.DAYS.between(inDate, outDate);
        if (days == 0) days = 1;
 
        int pricePerDay = 2000; 
        long totalAmount = days * pricePerDay;
 
        model.addAttribute("hotelId", hotelId);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("totalAmount", totalAmount);
 
        return "payment"; 
    }
    
    //  PAYMENT 
    @PostMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("hotelId") Long hotelId,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            @RequestParam("totalAmount") Long totalAmount,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);

        LocalDate inDate = LocalDate.parse(checkIn);
        LocalDate outDate = LocalDate.parse(checkOut);

        if (bookingService.isDuplicateBooking(user, hotel, inDate, outDate)) {
            redirectAttributes.addFlashAttribute(
                "duplicateError",
                "Booking already exists for these dates."
            );
            return "redirect:/book/" + hotelId;
        }
        
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setCheckIn(LocalDate.parse(checkIn));
        booking.setCheckOut(LocalDate.parse(checkOut));
        booking.setTotalPrice(totalAmount.doubleValue());

        String ref = "HTL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        booking.setBookingRef(ref);
        
        bookingRepository.save(booking);

        model.addAttribute("hotelName", hotel.getName());
        model.addAttribute("hotelId", hotelId);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("totalAmount", totalAmount);

        emailService.sendBookingConfirmationEmail(booking);

        return "success";
    }  
}