package com.pyrky_android.pojo;

public class UserLocationData {

    private String userLat;
    private String userLong;

    public UserLocationData(String userLat, String userLong) {
        this.userLat = userLat;
        this.userLong = userLong;
    }

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUserLong() {
        return userLong;
    }

    public void setUserLong(String userLong) {
        this.userLong = userLong;
    }
}
