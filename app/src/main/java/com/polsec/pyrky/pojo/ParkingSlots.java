package com.polsec.pyrky.pojo;

import java.util.HashMap;

/**
 * Created by czsm4 on 10/10/18.
 */

public class ParkingSlots {

    private String slotLat;
    private String slotLong;


    public ParkingSlots(){


    }

    public ParkingSlots(String slotLat, String slotLong){
        this.slotLat=slotLat;
        this.slotLong=slotLong;


    }


    public String getSlotLat() {
        return slotLat;
    }

    public void setSlotLat(String slotLat) {
        this.slotLat = slotLat;
    }

    public String getSlotLong() {
        return slotLong;
    }

    public void setSlotLong(String slotLong) {
        this.slotLong = slotLong;
    }

}
