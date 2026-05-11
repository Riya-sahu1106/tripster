package com.hotel.Hotel_Booking_System.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hotel.Hotel_Booking_System.model.Booking;
import com.hotel.Hotel_Booking_System.repository.CheckInReminderRepository;

@Service
public class CheckInReminderScheduler {

    @Autowired
    private CheckInReminderRepository checkInReminderRepository;

    @Autowired
    private CheckInReminderEmailService checkInReminderEmailService;

    @Scheduled(cron = "0 0 9 * * ?") 
    public void sendCheckInReminders() {

        LocalDate today = LocalDate.now();

        // 3-day reminders
        List<Booking> threeDayBookings =
                checkInReminderRepository.findConfirmedBookingsByCheckIn(today.plusDays(3));
        for (Booking booking : threeDayBookings) {
            checkInReminderEmailService.sendThreeDayReminderEmail(booking);
        }

        // 1-day reminders
        List<Booking> oneDayBookings =
                checkInReminderRepository.findConfirmedBookingsByCheckIn(today.plusDays(1));
        for (Booking booking : oneDayBookings) {
            checkInReminderEmailService.sendOneDayReminderEmail(booking);
        }

        System.out.println("[CheckInReminder] 3-day=" + threeDayBookings.size()
                + " | 1-day=" + oneDayBookings.size());
    }
}