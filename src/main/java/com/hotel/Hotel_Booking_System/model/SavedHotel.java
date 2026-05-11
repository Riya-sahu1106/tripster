package com.hotel.Hotel_Booking_System.model;

import jakarta.persistence.*;

@Entity
@Table(name = "saved_hotel") 
public class SavedHotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    // getters setters
    public Long getId() { return id; }

    public User getUser() { return user; }

    public Hotel getHotel() { return hotel; }

    public void setId(Long id) { this.id = id; }

    public void setUser(User user) { this.user = user; }

    public void setHotel(Hotel hotel) { this.hotel = hotel; }
}
