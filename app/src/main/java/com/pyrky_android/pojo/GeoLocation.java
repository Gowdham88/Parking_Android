package com.pyrky_android.pojo;

import java.util.List;

public class GeoLocation {

    List<Camera> cameraId;

    public GeoLocation(List<Camera> cameraId) {
        this.cameraId = cameraId;
    }

    public List<Camera> getCameraId() {
        return cameraId;
    }

    public void setCameraId(List<Camera> cameraId) {
        this.cameraId = cameraId;
    }
}
