package com.hotel.Hotel_Booking_System.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotel.Hotel_Booking_System.model.Booking;

public interface CheckInReminderRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b " +
           "JOIN FETCH b.user " +
           "JOIN FETCH b.hotel " +
           "WHERE b.checkIn = :checkInDate " +
           "AND b.status = 'CONFIRMED'")
    List<Booking> findConfirmedBookingsByCheckIn(@Param("checkInDate") LocalDate checkInDate);
}