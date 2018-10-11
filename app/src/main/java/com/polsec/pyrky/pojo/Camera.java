package com.polsec.pyrky.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Camera {

    private String cameraLat;
    private String cameraLong;
    private String parkingTypes;
    private String cameraID;

    private String carCategory;
    private String SecurityRatings;
    private HashMap<String,Object> parkingRules;
    private String cameraLocationName;
    private String videoUrl;
    private String roadWidth;
    private String cameraFacingDirection;
    private String ListofcarsPosition;
    private String ListofObstacles;
    private String cameraImageUrl;
//    private  ArrayList<HashMap<String, Object>>parkingSlots;
    private HashMap<String,ArrayList<Compact>> parkingSlots;
//    private ArrayList<ParkingSlots> parkingSlots;


    public Camera() {

    }

    public Camera(String cameraLat, String cameraLong, String parkingType, String securityRatings, HashMap<String,Object> parkingRules, String cameraLocationName, String videoUrl, String roadWidth,
                  String cameraFacingDirection, String listofcarsPosition, String listofObstacles, String cameraImageUrl, String cameraID,HashMap<String,ArrayList<Compact>> parkingSlots) {
        this.cameraLat = cameraLat;
        this.cameraLong = cameraLong;
        this.parkingTypes = parkingType;
        this.SecurityRatings = securityRatings;
        this.parkingRules = parkingRules;
        this.cameraLocationName = cameraLocationName;
        this.videoUrl = videoUrl;
        this.roadWidth = roadWidth;
        this.cameraFacingDirection = cameraFacingDirection;
        ListofcarsPosition = listofcarsPosition;
        ListofObstacles = listofObstacles;
        this.cameraImageUrl= cameraImageUrl;
        this.cameraID= cameraID;
        this.parkingSlots=parkingSlots;
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

    public String getParkingTypes() {
        return parkingTypes;
    }

    public void setParkingTypes(String parkingTypes) {
        this.parkingTypes = parkingTypes;
    }

    public String getCameraID() {
        return cameraID;
    }

    public void setCameraID(String cameraID) {
        this.cameraID = cameraID;
    }

    public String getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(String carCategory) {
        this.carCategory = carCategory;
    }

    public String getSecurityRatings() {
        return SecurityRatings;
    }

    public void setSecurityRatings(String securityRatings) {
        SecurityRatings = securityRatings;
    }

    public HashMap<String,Object> getParkingRules() {
        return parkingRules;
    }

    public void setParkingRules(HashMap<String,Object> parkingRules) {
        this.parkingRules = parkingRules;
    }

    public String getCameraLocationName() {
        return cameraLocationName;
    }

    public void setCameraLocationName(String cameraLocationName) {
        this.cameraLocationName = cameraLocationName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getRoadWidth() {
        return roadWidth;
    }

    public void setRoadWidth(String roadWidth) {
        this.roadWidth = roadWidth;
    }

    public String getCameraFacingDirection() {
        return cameraFacingDirection;
    }

    public void setCameraFacingDirection(String cameraFacingDirection) {
        this.cameraFacingDirection = cameraFacingDirection;
    }

    public String getListofcarsPosition() {
        return ListofcarsPosition;
    }

    public void setListofcarsPosition(String listofcarsPosition) {
        ListofcarsPosition = listofcarsPosition;
    }

    public String getListofObstacles() {
        return ListofObstacles;
    }

    public void setListofObstacles(String listofObstacles) {
        ListofObstacles = listofObstacles;
    }

    public String getCameraImageUrl() {
        return cameraImageUrl;
    }

    public void setCameraImageUrl(String cameraImageUrl) {
        this.cameraImageUrl = cameraImageUrl;
    }


    public HashMap<String,ArrayList<Compact>> getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(HashMap<String,ArrayList<Compact>> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

}
