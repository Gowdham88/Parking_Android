package com.polsec.pyrky.pojo;

/**
 * Created by macbook on 27/07/16.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Element {

    @SerializedName("distance")
    @Expose
    private Distance1 distance;
    @SerializedName("duration")
    @Expose
    private Duration1 duration;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     *
     * @return
     * The distance
     */
    public Distance1 getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     * The distance
     */
    public void setDistance(Distance1 distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     * The duration
     */
    public Duration1 getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     * The duration
     */
    public void setDuration(Duration1 duration) {
        this.duration = duration;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

}

