package com.polsec.pyrky.pojo;

import java.util.Map;

/**
 * Created by czsm4 on 21/08/18.
 */

public class UsersBooking {
    private String username;


    private String email;
    private String password;


    private String carCategory;
    private String profileImageURL;
    public Map<String, Boolean> Booking_ID;

    public UsersBooking() {

    }

    public UsersBooking(String username, String email, String profileImageUrl, String carCategory, Map<String, Boolean> Booking_ID) {
        this.username = username;
        this.email = email;
        this.profileImageURL = profileImageUrl;
        this.carCategory = carCategory;
        this.Booking_ID=Booking_ID;

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(String carCategory) {
        this.carCategory = carCategory;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}