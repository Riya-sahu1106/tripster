package com.hotel.Hotel_Booking_System.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.Hotel_Booking_System.model.Review;
import com.hotel.Hotel_Booking_System.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // ✅ SAVE NEW REVIEW
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    // ✅ DELETE
    public void deleteReviewById(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
        } else {
            throw new RuntimeException("Review not found");
        }
    }

    // ✅ GET BY ID
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    // ✅ UPDATE (SAFE VERSION)
    public void updateReview(Review updatedReview) {

        if (!reviewRepository.existsById(updatedReview.getId())) {
            throw new RuntimeException("Review not found");
        }

        Review existing = reviewRepository.findById(updatedReview.getId()).orElse(null);

        if (existing != null) {
            existing.setRating(updatedReview.getRating());
            existing.setComment(updatedReview.getComment());

            reviewRepository.save(existing);
        }
    }

    public List<Review> getReviewsByHotelId(Long hotelId) {
        return reviewRepository.findByHotelId(hotelId);
    }

    public boolean reviewExists(Long id) {
        return reviewRepository.existsById(id);
    }
    
    public double getAverageRating(Long hotelId) {
        List<Review> reviews = reviewRepository.findByHotelId(hotelId);

        if (reviews.isEmpty()) return 0;

        double sum = 0;
        for (Review r : reviews) {
            sum += r.getRating();
        }

        return sum / reviews.size();
    }
    
    public List<Review> getReviewsByUser(String userName) {
        return reviewRepository.findByUserName(userName);
    }  
    
}