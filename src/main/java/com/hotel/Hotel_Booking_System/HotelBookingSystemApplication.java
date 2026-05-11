package com.hotel.Hotel_Booking_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableAsync
public class HotelBookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelBookingSystemApplication.class, args);
    }
}