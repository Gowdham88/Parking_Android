package com.polsec.pyrky.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by czsm4 on 10/10/18.
 */

public class Compact {
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("rectSlot")
    @Expose
    private List<Object> rectSlot = null;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public List<Object> getRectSlot() {
        return rectSlot;
    }

    public void setRectSlot(List<Object> rectSlot) {
        this.rectSlot = rectSlot;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
