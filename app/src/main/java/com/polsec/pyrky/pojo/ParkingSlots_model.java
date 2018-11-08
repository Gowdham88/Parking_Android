package com.polsec.pyrky.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by czsm4 on 12/10/18.
 */

public class ParkingSlots_model {
    @SerializedName("compact")
    @Expose
    private List<Compact> compact = null;
    @SerializedName("van")
    @Expose
    private List<Van> van = null;
    @SerializedName("mid")
    @Expose
    private List<Mid> mid = null;
    @SerializedName("small")
    @Expose
    private List<Small> small = null;
    @SerializedName("full")
    @Expose
    private List<Full> full = null;

    public List<Compact> getCompact() {
        return compact;
    }

    public void setCompact(List<Compact> compact) {
        this.compact = compact;
    }

    public List<Van> getVan() {
        return van;
    }

    public void setVan(List<Van> van) {
        this.van = van;
    }

    public List<Mid> getMid() {
        return mid;
    }

    public void setMid(List<Mid> mid) {
        this.mid = mid;
    }

    public List<Small> getSmall() {
        return small;
    }

    public void setSmall(List<Small> small) {
        this.small = small;
    }

    public List<Full> getFull() {
        return full;
    }

    public void setFull(List<Full> full) {
        this.full = full;
    }

}