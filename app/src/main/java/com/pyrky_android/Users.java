package com.pyrky_android;

/**
 * Created by thulirsoft on 7/3/18.
 */

public class Users {

    private String userName;
    private String email;
    private String password;
    private String carCategory;
    private String profileImageUrl;
    private String platform;
    public Users(String userName, String email, String password, String carCategory) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.carCategory = carCategory;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(String carCategory) {
        this.carCategory = carCategory;
    }
}
