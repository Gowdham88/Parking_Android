package com.polsec.pyrky.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by czsm4 on 12/10/18.
 */

public class latExample {
    @SerializedName("parkingSlots")
    @Expose
    private SlotTypes parkingSlots;

    public SlotTypes getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(SlotTypes parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

}
