package com.pyrky_android.pojo;

public class UserLocationData {

    private String cameraLat;


    private String cameraLong;
    private String cameraImageUrl;
    private String cameraLocationName;


    public UserLocationData(String cameraLat, String cameraLong, String cameraImageUrl, String cameraLocationName) {
        this.cameraLat = cameraLat;
        this.cameraLong = cameraLong;
        this.cameraImageUrl=cameraImageUrl;
        this.cameraLocationName=cameraLocationName;

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

    public String getCameraLocationName() {
        return cameraLocationName;
    }

    public void setCameraLocationName(String cameraLocationName) {
        this.cameraLocationName = cameraLocationName;
    }


}
