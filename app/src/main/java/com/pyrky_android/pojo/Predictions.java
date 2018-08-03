package com.pyrky_android.pojo;

import android.gesture.Prediction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by czsm4 on 03/08/18.
 */

public class Predictions {
    @SerializedName("predictions")
    @Expose
    private List<placeids> predictions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<placeids> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<placeids> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}