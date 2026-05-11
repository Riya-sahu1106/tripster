package com.hotel.Hotel_Booking_System.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.Hotel_Booking_System.model.Hotel;
import com.hotel.Hotel_Booking_System.model.SavedHotel;
import com.hotel.Hotel_Booking_System.model.User;

public interface SavedHotelRepository extends JpaRepository<SavedHotel, Long> {

	 List<SavedHotel> findByUser(User user);

	    Optional<SavedHotel> findByUserAndHotel(User user, Hotel hotel);
}
