package com.hotel.Hotel_Booking_System.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="hotels")
public class Hotel {
    
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String city;
	private int price;
	
	@Column(name="image_url")
	private String imageUrl;
	
	private String description;
	
	private String image1;
	private String image2;
	private String image3;
	private String image4;
	private String image5;
	
	private String location;
	
	private int reviews;
	private double rating;
	private int singlePrice;
	private int doublePrice;
	private int suitePrice;
	
	public int getSinglePrice() {
		return singlePrice;
	}

	public void setSinglePrice(int singlePrice) {
		this.singlePrice = singlePrice;
	}

	public int getDoublePrice() {
		return doublePrice;
	}

	public void setDoublePrice(int doublePrice) {
		this.doublePrice = doublePrice;
	}

	public int getSuitePrice() {
		return suitePrice;
	}

	public void setSuitePrice(int suitePrice) {
		this.suitePrice = suitePrice;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getReviews() {
		return reviews;
	}

	public void setReviews(int reviews) {
		this.reviews = reviews;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getImage4() {
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	public String getImage5() {
		return image5;
	}

	public void setImage5(String image5) {
		this.image5 = image5;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public Hotel() {}

	public Long getId() {
		return id;
	}

	public void setId(long hotel_id) {
		this.id = hotel_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
	
	
	
