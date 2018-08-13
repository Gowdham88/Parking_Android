package com.polsec.pyrky.pojo;

public class Camera {

    private String cameraLat;
    private String cameraLong;
    private String parkingType;


    private String securityRatings;
    private String ListofparkingRules;
    private String cameraLocationName;
    private String videoUrl;
    private String roadWidth;
    private String cameraFacingDirection;
    private String ListofcarsPosition;
    private String ListofObstacles;
    private String cameraImageUrl;

    public Camera(String cameraLat, String cameraLong, String parkingType, String securityRatings, String listofparkingRules, String cameraLocationName, String videoUrl, String roadWidth,
                  String cameraFacingDirection, String listofcarsPosition, String listofObstacles, String cameraImageUrl) {
        this.cameraLat = cameraLat;
        this.cameraLong = cameraLong;
        this.parkingType = parkingType;
        this.securityRatings = securityRatings;
        ListofparkingRules = listofparkingRules;
        this.cameraLocationName = cameraLocationName;
        this.videoUrl = videoUrl;
        this.roadWidth = roadWidth;
        this.cameraFacingDirection = cameraFacingDirection;
        ListofcarsPosition = listofcarsPosition;
        ListofObstacles = listofObstacles;
        this. cameraImageUrl= cameraImageUrl;
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
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getSecurityRatings() {
        return securityRatings;
    }

    public void setSecurityRatings(String securityRatings) {
        this.securityRatings = securityRatings;
    }

    public String getListofparkingRules() {
        return ListofparkingRules;
    }

    public void setListofparkingRules(String listofparkingRules) {
        ListofparkingRules = listofparkingRules;
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
