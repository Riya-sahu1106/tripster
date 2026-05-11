package com.hotel.Hotel_Booking_System.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HotelCustomRepository {
	 @Autowired
	    private JdbcTemplate jdbc;

	    public void updateRating(int hotelId, double rating, int reviews) {
	        String sql = "UPDATE hotels SET rating=?, reviews=? WHERE id=?";
	        jdbc.update(sql, rating, reviews, hotelId);
	    }
}
