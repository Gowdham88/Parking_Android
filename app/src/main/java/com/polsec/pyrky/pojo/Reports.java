package com.polsec.pyrky.pojo;

import java.util.ArrayList;

/**
 * Created by czsm4 on 29/08/18.
 */

public class Reports {

    private ArrayList<ratingval> Ratings;
   private String cameraId;



    public Reports() {

    }

    public Reports(ArrayList<ratingval> Ratings, String cameraId) {
        this.Ratings = Ratings;
        this.cameraId = cameraId;

    }

    public ArrayList<ratingval> getRatings() {
        return Ratings;
    }

    public void setRatings(ArrayList<ratingval> ratings) {
        Ratings = ratings;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }


}
