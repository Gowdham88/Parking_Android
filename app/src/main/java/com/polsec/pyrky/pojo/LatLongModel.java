package com.polsec.pyrky.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by czsm4 on 11/10/18.
 */

public class LatLongModel {

//    @SerializedName("latlong")
//    @Expose

  private HashMap<String,List<SlotTypes>>latlong;

    LatLongModel(){

    }
    LatLongModel( HashMap<String,List<SlotTypes>>latlong){
        this.latlong=latlong;


    }


    public HashMap<String,List<SlotTypes>>latlong() {
        return latlong;
    }

    public void setLatlong(HashMap<String,List<SlotTypes>>latlong) {
        this.latlong = latlong;
    }
}
