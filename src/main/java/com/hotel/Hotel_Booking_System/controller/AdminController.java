package com.hotel.Hotel_Booking_System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hotel.Hotel_Booking_System.model.Booking;
import com.hotel.Hotel_Booking_System.model.Hotel;
import com.hotel.Hotel_Booking_System.model.User;
import com.hotel.Hotel_Booking_System.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ==================== ADMIN CHECK ====================

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        return user != null && user.isAdmin();
    }

    // ==================== DASHBOARD ====================

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("totalHotels",   adminService.getTotalHotels());
        model.addAttribute("totalBookings", adminService.getTotalBookings());
        model.addAttribute("totalUsers",    adminService.getTotalUsers());
        model.addAttribute("totalRevenue",  adminService.getTotalRevenue());

        return "admin-dashboard";
    }

    // ==================== HOTEL MANAGEMENT ====================

    @GetMapping("/admin/hotels")
    public String hotelsList(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("hotels", adminService.getAllHotels());
        return "admin-hotels";
    }

    @GetMapping("/admin/hotels/add")
    public String showAddHotelForm(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin-add-hotel";
    }

    @PostMapping("/admin/hotels/add")
    public String addHotel(HttpSession session,
                           @RequestParam String name,
                           @RequestParam String city,
                           @RequestParam int price,
                           @RequestParam String description,
                           @RequestParam String imageUrl,
                           @RequestParam String location,
                           @RequestParam int singlePrice,
                           @RequestParam int doublePrice,
                           @RequestParam int suitePrice) {

        if (!isAdmin(session)) return "redirect:/login";

        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setCity(city);
        hotel.setPrice(price);
        hotel.setDescription(description);
        hotel.setImageUrl(imageUrl);
        hotel.setLocation(location);
        hotel.setSinglePrice(singlePrice);
        hotel.setDoublePrice(doublePrice);
        hotel.setSuitePrice(suitePrice);

        adminService.addHotel(hotel);
        return "redirect:/admin/hotels";
    }

    @GetMapping("/admin/hotels/edit/{id}")
    public String showEditHotelForm(@PathVariable Long id,
                                    HttpSession session,
                                    Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        Hotel hotel = adminService.getHotelById(id);
        if (hotel == null) return "redirect:/admin/hotels";

        model.addAttribute("hotel", hotel);
        return "admin-add-hotel";
    }

    @PostMapping("/admin/hotels/edit/{id}")
    public String updateHotel(@PathVariable Long id,
                               HttpSession session,
                               @RequestParam String name,
                               @RequestParam String city,
                               @RequestParam int price,
                               @RequestParam String description,
                               @RequestParam String imageUrl,
                               @RequestParam String location,
                               @RequestParam int singlePrice,
                               @RequestParam int doublePrice,
                               @RequestParam int suitePrice) {

        if (!isAdmin(session)) return "redirect:/login";

        Hotel hotel = adminService.getHotelById(id);
        if (hotel != null) {
            hotel.setName(name);
            hotel.setCity(city);
            hotel.setPrice(price);
            hotel.setDescription(description);
            hotel.setImageUrl(imageUrl);
            hotel.setLocation(location);
            hotel.setSinglePrice(singlePrice);
            hotel.setDoublePrice(doublePrice);
            hotel.setSuitePrice(suitePrice);
            adminService.updateHotel(hotel);
        }

        return "redirect:/admin/hotels";
    }

    @PostMapping("/admin/hotels/delete/{id}")
    public String deleteHotel(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        adminService.deleteHotel(id);
        return "redirect:/admin/hotels";
    }

    // ==================== BOOKING MANAGEMENT ====================

    @GetMapping("/admin/bookings")
    public String bookingsList(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("bookings", adminService.getAllBookings());
        return "admin-bookings";
    }

    @PostMapping("/admin/bookings/cancel/{id}")
    public String cancelBooking(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        adminService.cancelBooking(id);
        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/bookings/confirm/{id}")
    public String confirmBooking(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        adminService.confirmBooking(id);
        return "redirect:/admin/bookings";
    }

    // ==================== USER MANAGEMENT ====================

    @GetMapping("/admin/users")
    public String usersList(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("users", adminService.getAllUsers());
        return "admin-users";
    }

    @PostMapping("/admin/users/makeAdmin/{id}")
    public String makeAdmin(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        adminService.makeAdmin(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/removeAdmin/{id}")
    public String removeAdmin(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        adminService.removeAdmin(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        adminService.deleteUser(id);
        return "redirect:/admin/users";
    }
}