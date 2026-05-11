package com.hotel.Hotel_Booking_System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotel.Hotel_Booking_System.model.Hotel;
import com.hotel.Hotel_Booking_System.model.Review;
import com.hotel.Hotel_Booking_System.model.User;
import com.hotel.Hotel_Booking_System.service.HotelService;
import com.hotel.Hotel_Booking_System.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {
	
	@Autowired
	private HotelService hotelService;

	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/myReviews")
	public String showMyReviews(HttpSession session, Model model) {

	    User user = (User) session.getAttribute("loggedUser");

	    if (user == null) {
	        return "redirect:/login";
	    }

	    List<Review> reviews = reviewService.getReviewsByUser(user.getName());

	    model.addAttribute("reviews", reviews);

	    return "reviews";
	}
}
