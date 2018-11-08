package com.polsec.pyrky.pojo;

/**
 * Created by czsm4 on 02/11/18.
 */

public class Locationlatlong {

    private double Startlatitude;
    private double Startlongitude;


    private double Endlatitude;
    private double Endlongitude;

    public Locationlatlong() {

}

    public Locationlatlong(double Startlatitude, double Startlongitude, double Endlatitude, double Endlongitude) {
        this.Startlatitude = Startlatitude;
        this.Startlongitude = Startlongitude;
        this.Endlatitude = Endlatitude;
        this.Endlongitude = Endlongitude;

    }


    public double getStartlatitude() {
        return Startlatitude;
    }

    public void setStartlatitude(double startlatitude) {
        Startlatitude = startlatitude;
    }

    public double getStartlongitude() {
        return Startlongitude;
    }

    public void setStartlongitude(double startlongitude) {
        Startlongitude = startlongitude;
    }

    public double getEndlatitude() {
        return Endlatitude;
    }

    public void setEndlatitude(double endlatitude) {
        Endlatitude = endlatitude;
    }

    public double getEndlongitude() {
        return Endlongitude;
    }

    public void setEndlongitude(double endlongitude) {
        Endlongitude = endlongitude;
    }
}
