package com.hotel.Hotel_Booking_System.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.Hotel_Booking_System.model.Booking;
import com.hotel.Hotel_Booking_System.model.Hotel;
import com.hotel.Hotel_Booking_System.model.User;
import com.hotel.Hotel_Booking_System.repository.BookingRepository;
import com.hotel.Hotel_Booking_System.repository.HotelRepository;
import com.hotel.Hotel_Booking_System.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    // ===================== DASHBOARD STATS =====================

    public long getTotalHotels() {
        return hotelRepository.count();
    }

    public long getTotalBookings() {
        return bookingRepository.count();
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public double getTotalRevenue() {
        return bookingRepository.findAll()
                .stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .mapToDouble(b -> b.getTotalPrice() != null ? b.getTotalPrice() : 0)
                .sum();
    }

    // ===================== HOTEL MANAGEMENT =====================

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElse(null);
    }

    public void addHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public void updateHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    // ===================== BOOKING MANAGEMENT =====================

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public void cancelBooking(Integer id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
        }
    }

    public void confirmBooking(Integer id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            booking.setStatus("CONFIRMED");
            bookingRepository.save(booking);
        }
    }

    // ===================== USER MANAGEMENT =====================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public void makeAdmin(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setRole("ADMIN");
            userRepository.save(user);
        }
    }

    public void removeAdmin(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setRole("USER");
            userRepository.save(user);
        }
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}