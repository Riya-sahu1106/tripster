package com.hotel.Hotel_Booking_System.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotel.Hotel_Booking_System.model.Booking;
import com.hotel.Hotel_Booking_System.model.Hotel;
import com.hotel.Hotel_Booking_System.model.User;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUser(User user);

    @Query("SELECT COUNT(b) FROM Booking b " +
           "WHERE b.user = :user " +
           "AND b.hotel = :hotel " +
           "AND b.status = 'CONFIRMED' " +
           "AND b.checkIn < :checkOut " +
           "AND b.checkOut > :checkIn")
    long countOverlappingBookings(
            @Param("user")     User user,
            @Param("hotel")    Hotel hotel,
            @Param("checkIn")  LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}