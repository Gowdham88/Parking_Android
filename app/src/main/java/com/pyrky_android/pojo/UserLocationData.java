package com.pyrky_android.pojo;

public class UserLocationData {

    private String cameraLat;


    private String cameraLong;
    private String cameraImageUrl;


    public UserLocationData(String cameraLat, String cameraLong, String cameraImageUrl) {
        this.cameraLat = cameraLat;
        this.cameraLong = cameraLong;
        this.cameraImageUrl=cameraImageUrl;

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
    public String getCameraImageUrl() {
        return cameraImageUrl;
    }

    public void setCameraImageUrl(String cameraImageUrl) {
        this.cameraImageUrl = cameraImageUrl;
    }



}
