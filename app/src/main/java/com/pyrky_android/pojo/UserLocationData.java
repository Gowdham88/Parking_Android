package com.pyrky_android.pojo;

public class UserLocationData {

    private double userLat;

    private double userLong;
    private String cameraLocationName;



    public UserLocationData() {
    }

    public UserLocationData(double userLat, double userLong, String cameraLocationName) {
        this.userLat = userLat;
        this.userLong = userLong;
        this.cameraLocationName=cameraLocationName;
    }

    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public double getUserLong() {
        return userLong;
    }

    public void setUserLong(double userLong) {
        this.userLong = userLong;
    }

    public String getCameraLocationName() {
        return cameraLocationName;
    }

    public void setCameraLocationName(String cameraLocationName) {
        this.cameraLocationName = cameraLocationName;
    }

}
