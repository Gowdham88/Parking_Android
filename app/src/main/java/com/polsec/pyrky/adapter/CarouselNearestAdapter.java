package com.polsec.pyrky.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.NearestLocMapsActivity;
import com.polsec.pyrky.fragment.NotificationVideoFragment;
import com.polsec.pyrky.fragment.ProfileFragment;
import com.polsec.pyrky.pojo.NearestData;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by czsm4 on 07/08/18.
 */

public class CarouselNearestAdapter extends RecyclerView.Adapter<CarouselNearestAdapter.ViewHolder> {

    private Context context;
    List<NearestData> mNearestDataList = new ArrayList<NearestData>();
    List<Address> yourAddresses;
    String value = "carousel", stringcartypeval;
    int distanceval, cartype;

    public CarouselNearestAdapter(Context context, List<NearestData> mNearestDataList) {

        this.context = context;
        this.mNearestDataList = mNearestDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearest_places_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//        holder.nearestPlaceImage.setImageResource(mLocationImage[position]);
        int adapterPosition = holder.getAdapterPosition();
        Picasso.with(context).load(mNearestDataList.get(adapterPosition).getCameraImageUrl())
                .fit()
                .into(holder.nearestPlaceImage);

        Double disval = Double.valueOf(String.valueOf(mNearestDataList.get(position).getLocationDistance().toString()));
        Log.e("val", String.valueOf(disval));

        cartype = Integer.parseInt(PreferencesHelper.getPreference(context, PreferencesHelper.PREFERENCE_PROFILE_CAR));
        if (cartype == 0) {
            stringcartypeval = "Compact";
        } else if (cartype == 1) {
            stringcartypeval = "Small";
        } else if (cartype == 2) {
            stringcartypeval = "Mid size";
        } else if (cartype == 3) {
            stringcartypeval = "Full";
        } else {
            stringcartypeval = "Van/Pick-up";
        }
        distanceval = (int) Double.parseDouble(String.valueOf(disval));

        Log.e("distanceval", String.valueOf(distanceval));
        if (!mNearestDataList.get(position).equals(null)) {
            if (distanceval > 0 && distanceval <= 100) {
                holder.nearestPlaceDistance.setText("0 - 100m");
            } else if (distanceval > 100 && distanceval <= 200) {
                holder.nearestPlaceDistance.setText("100 - 200m");
            } else if (distanceval > 200 && distanceval <= 300) {
                holder.nearestPlaceDistance.setText("200 - 300m");
            } else if (distanceval > 300 && distanceval <= 400) {
                holder.nearestPlaceDistance.setText("300 - 400m");
            } else if (distanceval > 400 && distanceval <= 500) {
                holder.nearestPlaceDistance.setText("400 - 500m");
            } else if (distanceval > 500 && distanceval <= 600) {
                holder.nearestPlaceDistance.setText("500 - 600m");
            } else if (distanceval > 600 && distanceval <= 700) {
                holder.nearestPlaceDistance.setText("600 - 700m");
            } else if (distanceval > 700 && distanceval <= 800) {
                holder.nearestPlaceDistance.setText("700 - 800m");
            } else if (distanceval > 800 && distanceval <= 900) {
                holder.nearestPlaceDistance.setText("800 - 900m");
            } else {
                holder.nearestPlaceDistance.setText("1000m and above");
            }
        }


//        holder.nearestPlaceDistance.setText(caldis1.get(position));


        final double latitude = Double.parseDouble(mNearestDataList.get(position).getCameraLat());
        final double longitude = Double.parseDouble(mNearestDataList.get(position).getCameraLong());

        Geocoder geocoder;

        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            yourAddresses = geocoder.getFromLocation(Double.parseDouble(mNearestDataList.get(position).getCameraLat()), Double.parseDouble(mNearestDataList.get(position).getCameraLong()), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (yourAddresses.size() > 0) {
            String yourAddress = yourAddresses.get(0).getAddressLine(0);
            String yourCity = yourAddresses.get(0).getAddressLine(1);
            String yourCountry = yourAddresses.get(0).getAddressLine(2);


        }

        holder.nearestPlaceAve.setText(mNearestDataList.get(adapterPosition).getCameraLocationName());
        holder.nearestPlaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("latt", mNearestDataList.get(position).getCameraLat());
                args.putString("longg", mNearestDataList.get(position).getCameraLong());
                args.putString("carousel", value);
                args.putInt("adapterPosition", holder.getAdapterPosition());
                args.putString("place", mNearestDataList.get(position).getCameraLocationName());
                args.putInt("distance", distanceval);
                args.putString("imgurl", mNearestDataList.get(position).getCameraImageUrl());
                args.putString("cameraid", mNearestDataList.get(position).getCameraID());
                args.putString("parkingtype", mNearestDataList.get(position).getParkingTypes());
                args.putSerializable("rulslist", mNearestDataList.get(position).getParkingRules());
                args.putString("cartype", stringcartypeval);
                context.startActivity(new Intent(context, NearestLocMapsActivity.class).putExtra("mapBundle", args));
                Log.e("position", String.valueOf(mNearestDataList.get(position).getCameraID()));
                Log.e("positionlatlong", String.valueOf(mNearestDataList.get(position).getCameraLat() + "," + mNearestDataList.get(position).getCameraLong()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNearestDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView nearestPlaceImage;
        TextView nearestPlaceAve, nearestPlaceCity, nearestPlaceDistance;

        ViewHolder(View itemView) {
            super(itemView);
            nearestPlaceImage = itemView.findViewById(R.id.nearest_place_image);
            nearestPlaceAve = itemView.findViewById(R.id.nearest_place_ave);
            nearestPlaceCity = itemView.findViewById(R.id.nearest_place_city);
            nearestPlaceDistance = itemView.findViewById(R.id.nearest_place_distance);
        }
    }
}