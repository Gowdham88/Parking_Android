package com.polsec.pyrky.pojo;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Booking implements Serializable {

    private String Current_User_UID;
    private String DestLat;
    private String DestLong;
    private String DestName;
    private String DateTime;
    private String bookingStatus;
    private String documentID;
    private String parkingSpaceRating;
    private String protectCar;

    public Booking() {
        Current_User_UID="";
        DestLat="";
        DestLong="";
        DestName="";
        DateTime="";
    }

    public Booking(String Current_User_UID, String DestLat, String DestLong, String DestName, String DateTime, String bookingStatus, String documentID, String parkingSpaceRating, String protectCar) {
        this.Current_User_UID = Current_User_UID;
        this.DestLat = DestLat;
        this.DestLong = DestLong;
        this.DestName = DestName;
        this.DateTime = DateTime;
        this.bookingStatus = bookingStatus;
        this.documentID=documentID;
        this.parkingSpaceRating = parkingSpaceRating;
        this.protectCar = protectCar;
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
    public String getDateTime() {
        return DateTime;
    }
    @PropertyName("DateTime")
    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }


    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }


    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }


    public String getParkingSpaceRating() {
        return parkingSpaceRating;
    }

    public void setParkingSpaceRating(String parkingSpaceRating) {
        this.parkingSpaceRating = parkingSpaceRating;
    }


    public String getProtectCar() {
        return protectCar;
    }

    public void setProtectCar(String protectCar) {
        this.protectCar = protectCar;
    }


}
