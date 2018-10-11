package com.polsec.pyrky.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by czsm4 on 11/10/18.
 */

public class LatLongModel {

  private HashMap<String,Object>latlong;

    LatLongModel(){

    }
    LatLongModel( HashMap<String,Object>latlong){
        this.latlong=latlong;


    }


    public HashMap<String, Object> getLatlong() {
        return latlong;
    }

    public void setLatlong(HashMap<String, Object> latlong) {
        this.latlong = latlong;
    }
}
