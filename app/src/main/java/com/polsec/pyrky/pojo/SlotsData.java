package com.polsec.pyrky.pojo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by czsm4 on 23/10/18.
 */

public class SlotsData {

    private Double LocationDistance;
    private String cameraLat;
    private String cameraLong;
    private String parkingTypes;
    private HashMap<String,Object> parkingRules;
    private String cameraID;
    private String cameraLocationName;
    private String cameraImageUrl;

    public SlotsData(){


    }

    public SlotsData(Double LocationDistance,String cameraLat, String cameraLong, String parkingType,String cameraID, HashMap<String,Object> parkingRules,
                                 String cameraLocationName, String cameraImageUrl){
        this.LocationDistance=LocationDistance;
        this.cameraLat = cameraLat;
        this.cameraLong = cameraLong;
        this.parkingTypes = parkingType;
        this.cameraID=cameraID;
        this.parkingRules = parkingRules;
        this.cameraLocationName = cameraLocationName;
        this.cameraImageUrl=cameraImageUrl;



    }



    public Double getLocationDistance() {
        return LocationDistance;
    }

    public void setLocationDistance(Double locationDistance) {
        LocationDistance = locationDistance;
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



    public String getCameraLocationName() {
        return cameraLocationName;
    }

    public void setCameraLocationName(String cameraLocationName) {
        this.cameraLocationName = cameraLocationName;
    }

    public String getCameraImageUrl() {
        return cameraImageUrl;
    }

    public void setCameraImageUrl(String cameraImageUrl) {
        this.cameraImageUrl = cameraImageUrl;
    }
    public HashMap<String, Object> getParkingRules() {
        return parkingRules;
    }

    public void setParkingRules(HashMap<String, Object> parkingRules) {
        this.parkingRules = parkingRules;
    }


}
