package com.hotel.Hotel_Booking_System.model;



import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate  checkIn;
    private LocalDate  checkOut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @JoinColumn(name="total_price")
    private Double totalPrice;

    @jakarta.persistence.Column(name="status")
    private String status = "CONFIRMED";

    @jakarta.persistence.Column(name="booking_ref")
    private String bookingRef;
    
    private LocalDate cancelDate;
    
    @Column(name="room_type")
    private String roomType;
    
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public String getBookingRef() { return bookingRef; }
	public void setBookingRef(String bookingRef) { this.bookingRef = bookingRef; }

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}
	public LocalDate getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(LocalDate checkIn) {
		this.checkIn = checkIn;
	}

	public LocalDate getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(LocalDate checkOut) {
		this.checkOut = checkOut;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}



    
}
