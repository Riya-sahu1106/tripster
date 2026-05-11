package com.hotel.Hotel_Booking_System.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hotel.Hotel_Booking_System.model.Hotel;
import com.hotel.Hotel_Booking_System.model.SavedHotel;
import com.hotel.Hotel_Booking_System.model.User;
import com.hotel.Hotel_Booking_System.repository.HotelRepository;
import com.hotel.Hotel_Booking_System.repository.SavedHotelRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class SavedController {

    @Autowired
    private SavedHotelRepository savedHotelRepository;
    
    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping("/saved")
    public String showSaved(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        List<SavedHotel> savedList = savedHotelRepository.findByUser(user);

        model.addAttribute("savedHotels", savedList);

        return "saved"; 
    }
    
    @PostMapping("/save/{id}")
    public String saveHotel(@PathVariable Long id, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        Hotel hotel = hotelRepository.findById(id).orElseThrow();

        Optional<SavedHotel> existing =
                savedHotelRepository.findByUserAndHotel(user, hotel);

        if (existing.isPresent()) {
            savedHotelRepository.delete(existing.get());
        } else {
            SavedHotel saved = new SavedHotel();
            saved.setUser(user);
            saved.setHotel(hotel);
            savedHotelRepository.save(saved);
        }

        return "redirect:/home";
    }  
    
    @PostMapping("/removeSaved/{id}")
    public String removeSaved(@PathVariable Long id) {

        savedHotelRepository.deleteById(id);

        return "redirect:/saved";
    }
}
