package com.polsec.pyrky.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.ViewImage.ViewImageActivity;

import io.fabric.sdk.android.services.common.SafeToast;

/**
 * Created by czsm4 on 09/08/18.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    Context context;
    LayoutInflater inflater=null;
    RelativeLayout ViewRelLay;


    public CustomInfoWindowGoogleMap(Context context,LayoutInflater inflater) {
        this.context = context;
        this.inflater=inflater;


    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
       View  view =inflater.inflate(
                R.layout.ruls_layout, null);
//
//        ViewRelLay=(RelativeLayout)view.findViewById(R.id.view_lay);
//
//        ViewRelLay.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
//    }
//});



        return view;
    }



}

