package com.hotel.Hotel_Booking_System.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.Hotel_Booking_System.model.Hotel;
import com.hotel.Hotel_Booking_System.repository.HotelRepository;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElse(null);
    }
}