package com.polsec.pyrky.pojo;

import java.util.List;

public class Camera {

    private String cameraLat;
    private String cameraLong;
    private String parkingTypes;
    private String cameraID;

    private String carCategory;
    private String SecurityRatings;
    private List<ParkingRules> parkingRules;
    private String cameraLocationName;
    private String videoUrl;
    private String roadWidth;
    private String cameraFacingDirection;
    private String ListofcarsPosition;
    private String ListofObstacles;
    private String cameraImageUrl;

    public Camera() {

    }

    public Camera(String cameraLat, String cameraLong, String parkingType, String securityRatings, List<ParkingRules> parkingRules, String cameraLocationName, String videoUrl, String roadWidth,
                  String cameraFacingDirection, String listofcarsPosition, String listofObstacles, String cameraImageUrl, String cameraID) {
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

    public String getParkingType() {
        return parkingTypes;
    }

    public void setParkingType(String parkingType) {
        this.parkingTypes = parkingType;
    }

    public String getCameraID() {
        return cameraID;
    }

    public void setCameraID(String cameraID) {
        this.cameraID = cameraID;
    }

    public String getSecurityRatings() {
        return SecurityRatings;
    }

    public void setSecurityRatings(String securityRatings) {
        this.SecurityRatings = securityRatings;
    }

    public List<ParkingRules> getListofparkingRules() {
        return parkingRules;
    }

    public void setListofparkingRules(List<ParkingRules> listofparkingRules) {
        parkingRules = listofparkingRules;
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
}
