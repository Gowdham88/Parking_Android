package com.polsec.pyrky.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by czsm4 on 10/10/18.
 */

public class Small {
    @SerializedName("latitude")
    @Expose
    private String  latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

//    public Small() {
//
//    }
//
//    public Small(String  latitude,String longitude) {
//        this.latitude=latitude;
//        this.longitude=longitude;
//
//    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}