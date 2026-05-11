package com.hotel.Hotel_Booking_System.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.Hotel_Booking_System.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByHotelId(Long hotelId);
    List<Review> findByUserName(String userName);
    
}