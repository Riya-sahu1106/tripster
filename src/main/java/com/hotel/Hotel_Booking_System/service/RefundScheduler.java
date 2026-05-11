package com.hotel.Hotel_Booking_System.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hotel.Hotel_Booking_System.model.Booking;
import com.hotel.Hotel_Booking_System.repository.BookingRepository;

@Service
public class RefundScheduler {

    @Autowired
    private BookingRepository bookingRepository;

    // Daily run hoga (midnight)
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoRefund() {

        List<Booking> bookings = bookingRepository.findAll();

        for (Booking booking : bookings) {

            if ("CANCELLED".equals(booking.getStatus()) && booking.getCancelDate() != null) {

                if (booking.getCancelDate().plusDays(5).isBefore(LocalDate.now())) {

                    booking.setStatus("REFUNDED");
                    bookingRepository.save(booking);
                }
            }
        }
    }
}