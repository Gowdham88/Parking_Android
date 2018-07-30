package com.pyrky_android.pojo;

public class Booking {

    private String currentUserUid;
    private String destLat;
    private String destLong;
    private String destName;
    private String dateTime;
    private String bookingStatus;
    private String parkingSpaceRating;
    private String protectCar;

    public Booking(String currentUserUid, String destLat, String destLong, String destName, String dateTime, String bookingStatus, String parkingSpaceRating, String protectCar) {
        this.currentUserUid = currentUserUid;
        this.destLat = destLat;
        this.destLong = destLong;
        this.destName = destName;
        this.dateTime = dateTime;
        this.bookingStatus = bookingStatus;
        this.parkingSpaceRating = parkingSpaceRating;
        this.protectCar = protectCar;
    }

    public String getCurrentUserUid() {
        return currentUserUid;
    }

    public void setCurrentUserUid(String currentUserUid) {
        this.currentUserUid = currentUserUid;
    }

    public String getDestLat() {
        return destLat;
    }

    public void setDestLat(String destLat) {
        this.destLat = destLat;
    }

    public String getDestLong() {
        return destLong;
    }

    public void setDestLong(String destLong) {
        this.destLong = destLong;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
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
