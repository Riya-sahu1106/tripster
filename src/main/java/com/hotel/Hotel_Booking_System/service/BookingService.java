package com.hotel.Hotel_Booking_System.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.Hotel_Booking_System.model.Booking;
import com.hotel.Hotel_Booking_System.model.Hotel;
import com.hotel.Hotel_Booking_System.model.User;
import com.hotel.Hotel_Booking_System.repository.BookingRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public boolean isDuplicateBooking(User user, Hotel hotel,
                                      LocalDate checkIn, LocalDate checkOut) {
        long count = bookingRepository.countOverlappingBookings(
                user, hotel, checkIn, checkOut);
        return count > 0;
    }

    public void cancelBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    public long calculateDays(LocalDate checkIn, LocalDate checkOut) {
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        return days == 0 ? 1 : days;
    }
}