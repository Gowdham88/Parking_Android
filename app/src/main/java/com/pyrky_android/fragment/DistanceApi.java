package com.pyrky_android.fragment;

import com.pyrky_android.pojo.TimeModelClass;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by czsm4 on 31/07/18.
 */

public interface DistanceApi{

    @GET
    Call<TimeModelClass> getUsers(@Url String url);

}