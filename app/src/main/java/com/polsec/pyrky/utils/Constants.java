package com.polsec.pyrky.utils;

import android.location.Location;

import com.polsec.pyrky.pojo.NearestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thulirsoft on 7/2/18.
 */

public class Constants {


    public static final String IMAGE_DIRECTORY_NAME = "Pyrky";

    public static Boolean IS_AR_ENABLED = false;

    public static List<String> SECURITY_RATINGS = new ArrayList<>();

    public static List<String> CAR_CATEGORY = new ArrayList<>();

    public static List<String> PARKING_TYPES = new ArrayList<>();

    public static Location currentLocation;

    public static List<NearestData> mNearestDataList = new ArrayList<>();

}