package com.pyrky_android.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.pyrky_android.R;
import com.pyrky_android.activity.ViewImage.ViewImageActivity;

/**
 * Created by czsm4 on 09/08/18.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter,View.OnClickListener {

    Context context;
    LayoutInflater inflater;
    RelativeLayout ViewLay,NavigateLay;
    TextView ViewTxt,NavigateTxt;
    public CustomInfoWindowGoogleMap(Context context) {
        this.context = context;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
    @SuppressLint("ServiceCast")
    @Override
    public View getInfoWindow(Marker marker) {
        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // R.layout.echo_info_window is a layout in my
        // res/layout folder. You can provide your own
        View v = inflater.inflate(R.layout.rules_layout, null);
        ViewLay=(RelativeLayout) v.findViewById(R.id.view_lay);
        ViewTxt=(TextView) v.findViewById(R.id.view_txt);
        NavigateLay=(RelativeLayout)v.findViewById(R.id.navi_lay);
        NavigateTxt=(TextView)v.findViewById(R.id.navi_txt);

        ViewLay.setOnClickListener(this);
        ViewTxt.setOnClickListener(this);
        NavigateLay.setOnClickListener(this);
        NavigateTxt.setOnClickListener(this);
//        ViewLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        return v;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.view_lay :
                Intent intent=new Intent(context,ViewImageActivity.class) ;
                context.startActivity(intent);
                //once logged in, load the main page

            case R.id.view_txt :
                Intent intent1=new Intent(context,ViewImageActivity.class) ;
                context.startActivity(intent1);
                break;

            case R.id.navi_lay :

                break;

            case R.id.navi_txt :

                break;

        }

//        Intent intent=new Intent(context,ViewImageActivity.class);
//        context.startActivity(intent);
    }
}

