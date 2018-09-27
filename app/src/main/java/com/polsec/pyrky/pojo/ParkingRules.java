package com.polsec.pyrky.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParkingRules {
    @SerializedName("0")
    private String rule;
    @SerializedName("1")
    private String timing;
    @SerializedName("2")
    private String charges;
    @SerializedName("3")
    private String message;

    public ParkingRules(String rule, String timing, String charges, String message) {
        this.rule = rule;
        this.timing = timing;
        this.charges = charges;
        this.message = message;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
