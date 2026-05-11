package com.hotel.Hotel_Booking_System.controller;

import java.time.LocalDate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotel.Hotel_Booking_System.model.Booking;
import com.hotel.Hotel_Booking_System.model.Hotel;
import com.hotel.Hotel_Booking_System.model.User;
import com.hotel.Hotel_Booking_System.repository.BookingRepository;
import com.hotel.Hotel_Booking_System.repository.HotelRepository;
import com.hotel.Hotel_Booking_System.service.BookingService;
import com.hotel.Hotel_Booking_System.service.EmailService;
import com.hotel.Hotel_Booking_System.service.PdfService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BookingService bookingService;

    @PostMapping("/confirmBooking")
    public String confirmBooking(@RequestParam int hotelId,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam String roomType,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

User user = (User) session.getAttribute("loggedUser");
if (user == null) return "redirect:/login";

Hotel hotel = hotelRepository.findById((long) hotelId).orElse(null);
if (hotel == null) {
model.addAttribute("error", "Hotel nahi mila.");
return "error";
}

LocalDate today = LocalDate.now();


if (checkIn.isBefore(today)) {
 model.addAttribute("dateError", "Check-in date aaj se pehle nahi ho sakti!");
 model.addAttribute("hotelId", hotelId);
 model.addAttribute("hotel", hotel);
 return "booking";
}

if (!checkIn.isBefore(checkOut)) {
 model.addAttribute("dateError", "Check-in date, Check-out se pehle honi chahiye!");
 model.addAttribute("hotelId", hotelId);
 model.addAttribute("hotel", hotel);
 return "booking";
}

// ✅ BUG 3 — Duplicate check baad mein (aur query mein status fix hoga)
if (bookingService.isDuplicateBooking(user, hotel, checkIn, checkOut)) {
redirectAttributes.addFlashAttribute(
"duplicateError",
"Aapne in dates pe pehle se booking ki hui hai!"
);
return "redirect:/hotel/" + hotelId; // ✅ BUG 6 FIX — sahi route
}

int pricePerNight;
switch (roomType) {
case "DOUBLE": pricePerNight = hotel.getDoublePrice(); break;
case "SUITE":  pricePerNight = hotel.getSuitePrice();  break;
default:       pricePerNight = hotel.getSinglePrice(); break;
}

Booking booking = new Booking();
booking.setHotel(hotel);
booking.setUser(user);
booking.setCheckIn(checkIn);
booking.setCheckOut(checkOut);
booking.setRoomType(roomType);

long days = bookingService.calculateDays(checkIn, checkOut);
double totalPrice = days * pricePerNight;
booking.setTotalPrice(totalPrice);

bookingRepository.save(booking);

String bookingId = "BOOK" + System.currentTimeMillis();
booking.setBookingRef(bookingId);
bookingRepository.save(booking);

emailService.sendBookingConfirmationEmail(booking);

model.addAttribute("bookingId", bookingId);
model.addAttribute("checkIn", checkIn);
model.addAttribute("checkOut", checkOut);
model.addAttribute("days", days);
model.addAttribute("price", pricePerNight);
model.addAttribute("totalPrice", totalPrice);
model.addAttribute("roomType", roomType); // ✅ BUG 5 FIX

return "booking-success";
}

    @PostMapping("/book-hotel")
    public String bookHotel(@RequestParam Long hotelId,
                             @RequestParam LocalDate checkIn,
                             @RequestParam LocalDate checkOut,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel == null) return "redirect:/";

        if (bookingService.isDuplicateBooking(user, hotel, checkIn, checkOut)) {
            redirectAttributes.addFlashAttribute(
                "duplicateError",
                "You already booked for these dates."
            );
            return "redirect:/hotel-detail?id=" + hotelId;
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);

        long days = bookingService.calculateDays(checkIn, checkOut);
        double totalPrice = days * hotel.getPrice();
        booking.setTotalPrice(totalPrice);

        bookingRepository.save(booking);

        String ref = "BOOK" + System.currentTimeMillis();
        booking.setBookingRef(ref);
        bookingRepository.save(booking);

        emailService.sendBookingConfirmationEmail(booking);

        return "redirect:/my-bookings";
    }

    @GetMapping("/my-bookings")
    public String myBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("bookings", bookingRepository.findByUser(user));
        return "my-bookings";
    }

    @PostMapping("/cancel-booking")
    public String cancelBooking(@RequestParam("bookingId") Integer bookingId,
                                HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null && booking.getUser().getId() == user.getId()) {
            booking.setStatus("CANCELLED");
            booking.setCancelDate(LocalDate.now());
            bookingRepository.save(booking);
            emailService.sendCancellationEmail(booking);
        }

        return "redirect:/my-bookings";
    }
    
    @Autowired
    private PdfService pdfService;

    @GetMapping("/download-receipt/{bookingId}")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Integer bookingId,
                                                   HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null || booking.getUser().getId() != user.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        byte[] pdf = pdfService.generateBookingReceipt(booking);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                "receipt-" + booking.getBookingRef() + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}