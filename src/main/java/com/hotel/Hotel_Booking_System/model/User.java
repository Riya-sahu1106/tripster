package com.hotel.Hotel_Booking_System.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 30, message = "Name should have 2 or 30 characters")
    private String name;
    
    @NotBlank(message = "Email cannot be empty")
    @Pattern(
    	    regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
    	    message = "Please enter a valid email address like example@gmail.com"
    	)
    private String email;
    
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password minimum 6 characters ka hona chahiye")
    private String password;

    @Column(nullable = false)        
    private String role = "USER";    

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(this.role); }
}