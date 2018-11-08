package com.polsec.pyrky.pojo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by czsm4 on 12/10/18.
 */

public class Parkingslotsmodel {


    private HashMap<String,ArrayList<SlotTypes>> parkingSlots;

    public Parkingslotsmodel() {

    }

    public Parkingslotsmodel(HashMap<String,ArrayList<SlotTypes>> parkingSlots) {
        this.parkingSlots = parkingSlots;

    }

    public HashMap<String, ArrayList<SlotTypes>> getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(HashMap<String, ArrayList<SlotTypes>> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

}
