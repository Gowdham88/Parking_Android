package com.polsec.pyrky.pojo;

public class UserLocationData {

    private String cameraLat;
    private String cameraLong;
    private String cameraImageUrl;
    private String cameraLocationName;
    private String carCategory;
    private String parkingTypes;

    public UserLocationData(String cameraLat, String cameraLong, String cameraImageUrl, String cameraLocationName) {
        this.cameraLat = cameraLat;
        this.cameraLong = cameraLong;
        this.cameraImageUrl=cameraImageUrl;
        this.cameraLocationName=cameraLocationName;

    }

    public String getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(String carCategory) {
        this.carCategory = carCategory;
    }

    public String getParkingTypes() {
        return parkingTypes;
    }

    public void setParkingTypes(String parkingTypes) {
        this.parkingTypes = parkingTypes;
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
