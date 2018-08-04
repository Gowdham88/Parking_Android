package com.pyrky_android.pojo;

public class UserLocationData {

    private String cameraLat;

    private String cameraLong;


    public UserLocationData(String cameraLat, String cameraLong) {
        this.cameraLat = cameraLat;
        this.cameraLong = cameraLong;

    }


    public String getCameraLat() {
        return cameraLat;
    }

    public void setCameraLat(String cameraLat) {
        this.cameraLat = cameraLat;
    }

    public String getCameraLong() {
        return cameraLong;
    }

    public void setCameraLong(String cameraLong) {
        this.cameraLong = cameraLong;
    }

    public UserLocationData() {
    }



}
