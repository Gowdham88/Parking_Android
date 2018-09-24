package com.polsec.pyrky.network;

import com.polsec.pyrky.pojo.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Amal Krishnan on 06-03-2017.
 */

public interface RetrofitInterface {

//
//    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/maps/api/directions/json?")
    Call<Example> getDirections(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key);
//            @Query("sensor") Boolean sensor);

    @GET("/maps/api/place/nearbysearch/json?")
    Call<PoiResponse> listPOI(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("key") String key
    );

    @GET("/maps/api/place/details/json?")
    Call<PlaceResponse> getPlaceDetail(
            @Query("placeid") String location,
            @Query("key") String key
    );

    @GET("/maps/api/geocode/json?")
    Call<GeocodeResponse> getGecodeData(
            @Query("address") String address,
            @Query("key") String key
    );

    @GET("/maps/api/geocode/json?")
    Call<GeocodeResponse> getRevGecodeData(
            @Query("latlng") String latlng,
            @Query("key") String key
    );


}

