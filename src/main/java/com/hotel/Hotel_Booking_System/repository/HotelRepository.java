package com.hotel.Hotel_Booking_System.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.Hotel_Booking_System.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long>{
       
	List<Hotel> findByCityIgnoreCase(String city);
	
}

 
