package com.polsec.pyrky.pojo;

public class Notification {

    private String currentUserUid;
    private String notificationText;
    private String cameraId;

    public Notification(String currentUserUid, String notificationText, String cameraId) {
        this.currentUserUid = currentUserUid;
        this.notificationText = notificationText;
        this.cameraId = cameraId;
    }

    public String getCurrentUserUid() {
        return currentUserUid;
    }

    public void setCurrentUserUid(String currentUserUid) {
        this.currentUserUid = currentUserUid;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }
}
