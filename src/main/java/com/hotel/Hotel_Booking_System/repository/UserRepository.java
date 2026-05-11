package com.hotel.Hotel_Booking_System.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.Hotel_Booking_System.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	
    User findByEmail(String gmail);

}