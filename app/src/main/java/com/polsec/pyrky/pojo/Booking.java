package com.polsec.pyrky.pojo;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Booking implements Serializable {

    private String Current_User_UID;
    private String DestLat;
    private String DestLong;
    private String DestName;
    private double DateTime;
    private String cameraId;

    private Boolean bookingStatus;


    private String documentID;
    private double parkingSpaceRating;
    private Boolean protectCar;

    public Booking() {
        Current_User_UID="";
        DestLat="";
        DestLong="";
        DestName="";
        DateTime= 0.0;
    }

    public Booking(String Current_User_UID, String DestLat, String DestLong, String DestName, double DateTime, Boolean bookingStatus, String cameraId, String documentID, double parkingSpaceRating, Boolean protectCar) {
        this.Current_User_UID = Current_User_UID;
        this.DestLat = DestLat;
        this.DestLong = DestLong;
        this.DestName = DestName;
        this.DateTime = DateTime;
        this.bookingStatus = bookingStatus;
        this.documentID=documentID;
        this.parkingSpaceRating = parkingSpaceRating;
        this.protectCar = protectCar;
        this.cameraId=cameraId;
    }
    @PropertyName("Current_User_UID")
    public String getCurrent_User_UID() {
        return Current_User_UID;
    }
    @PropertyName("Current_User_UID")
    public void setCurrent_User_UID(String current_User_UID) {
        Current_User_UID = current_User_UID;
    }

    @PropertyName("DestLat")
    public String getDestLat() {
        return DestLat;
    }
    @PropertyName("DestLat")
    public void setDestLat(String destLat) {
        DestLat = destLat;
    }

    @PropertyName("DestLong")
    public String getDestLong() {
        return DestLong;
    }
    @PropertyName("DestLong")
    public void setDestLong(String destLong) {
        DestLong = destLong;
    }

    @PropertyName("DestName")
    public String getDestName() {
        return DestName;
    }
    @PropertyName("DestName")
    public void setDestName(String destName) {
        DestName = destName;
    }


    @PropertyName("DateTime")
    public double getDateTime() {
        return DateTime;
    }
    @PropertyName("DateTime")
    public void setDateTime(double dateTime) {
        DateTime = dateTime;
    }


    public Boolean getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(Boolean bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }




    public Boolean getProtectCar() {
        return protectCar;
    }

    public void setProtectCar(Boolean protectCar) {
        this.protectCar = protectCar;
    }


    public double getParkingSpaceRating() {
        return parkingSpaceRating;
    }

    public void setParkingSpaceRating(double parkingSpaceRating) {
        this.parkingSpaceRating = parkingSpaceRating;
    }


    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

}
