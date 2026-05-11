package com.hotel.Hotel_Booking_System.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="review")
public class Review {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
     private Long id;
     private int rating;
     private String comment;
     
     @JoinColumn(name="user_name")
     private String userName;
     
     @ManyToOne
     @JoinColumn(name = "hotel_id")
     private Hotel hotel;
     
	 public Hotel getHotel() {
		return hotel;
	}
	 public void setHotel(Hotel hotel) {
		 this.hotel = hotel;
	 }
	 public Long getId() {
		return id;
	}
	 public void setId(Long id) {
		 this.id = id;
	 }
	 public String getUserName() {
		return userName;
	}
	 public void setUserName(String userName) {
		 this.userName = userName;
	 }
	 
	 public int getRating() {
		 return rating;
	 }
	 public void setRating(int rating) {
		 this.rating = rating;
	 }
	 public String getComment() {
		 return comment;
	 }
	 public void setComment(String comment) {
		 this.comment = comment;
	 }
	 
	 
     
     
}
