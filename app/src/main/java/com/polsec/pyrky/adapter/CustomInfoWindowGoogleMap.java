package com.polsec.pyrky.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.polsec.pyrky.R;

import io.fabric.sdk.android.services.common.SafeToast;

/**
 * Created by czsm4 on 09/08/18.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    Context context;
    LayoutInflater inflater;
    RelativeLayout ViewRelLay;
    private View view;

    public CustomInfoWindowGoogleMap(Context context) {
        this.context = context;


    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        view = ((Activity) context).getLayoutInflater().inflate(
                R.layout.rules_layout, null);

        ViewRelLay=(RelativeLayout)view.findViewById(R.id.view_lay);

//        ViewRelLay.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
//    }
//});

//        ViewLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        return view;
    }



}

