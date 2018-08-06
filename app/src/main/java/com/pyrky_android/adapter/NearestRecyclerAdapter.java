package com.pyrky_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pyrky_android.R;
import com.pyrky_android.activity.NearestLocMapsActivity;
import com.pyrky_android.pojo.UserLocationData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thulirsoft on 7/3/18.
 */

public class NearestRecyclerAdapter extends RecyclerView.Adapter<NearestRecyclerAdapter.ViewHolder> {

    private Context context;
    private int images[];
    private String[] mAve,mCity;
    private double lat[] = {70.01383623,56.50329796,1.23736985,-24.33605988,11.38350584,
            -58.68375965,44.87310434,147.64797704,-3.02408824,-21.33447419};
    private double lng[] = {-24.21957723,56.50329796,-163.58662616,16.88948658,62.62863347,
            -43.46925429,-91.28527609,85.94545339,-82.49033554,-175.53067807};
    private String mDistance[] = {"10 - 30m","50 - 100m","70 - 400m","500 - 550m","60 - 600m",
            "150 - 300m","80 - 90m","150 - 155m","30 - 35m","800 - 850m"};
    private int mLocationImage[];
    private int avatarSize;
    List<UserLocationData> datalist = new ArrayList<UserLocationData>();
    ArrayList<String> nearlat1 = new ArrayList<>();
    ArrayList<String> nearlong1 = new ArrayList<>();
    ArrayList<Double> distances1 = new ArrayList<>();

    public NearestRecyclerAdapter(Context context, int[] images, String[] mAve, String[] mCity, int[] mLocationImage) {
        this.context = context;
        this.images = images;
        this.mAve = mAve;
        this.mCity = mCity;
        this.mLocationImage = mLocationImage;
    }

    @NonNull
    @Override
    public NearestRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearest_places_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NearestRecyclerAdapter.ViewHolder holder, int position) {
//        holder.nearestPlaceImage.setImageResource(mLocationImage[position]);
        this.avatarSize = context.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);

        Picasso.with(context).load(this.mLocationImage[position])
                .fit()
                .into(holder.nearestPlaceImage);
        holder.nearestPlaceAve.setText(mAve[position]);
        holder.nearestPlaceCity.setText(mCity[position]);
        holder.nearestPlaceDistance.setText(mDistance[position]);
        final double latitude = lat[position];
        final double  longitude = lng[position];

        holder.nearestPlaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NearestLocMapsActivity.class);
                intent.putExtra("lat",latitude);
                intent.putExtra("lng",longitude);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView nearestPlaceImage;
        TextView nearestPlaceAve,nearestPlaceCity,nearestPlaceDistance;
        ViewHolder(View itemView) {
            super(itemView);
            nearestPlaceImage = itemView.findViewById(R.id.nearest_place_image);
            nearestPlaceAve = itemView.findViewById(R.id.nearest_place_ave);
            nearestPlaceCity = itemView.findViewById(R.id.nearest_place_city);
            nearestPlaceDistance = itemView.findViewById(R.id.nearest_place_distance);
        }
    }
}
//Picasso.with(context).load(this.datalist.get(position).getCameraImageUrl())
//        .fit()
//        .into(holder.nearestPlaceImage);
//final double latitude = Double.parseDouble(nearlat1.get(position));
//final double  longitude =  Double.parseDouble(nearlong1.get(position));
//        Geocoder geocoder;
//        List<Address> addresses = null;
//        geocoder = new Geocoder(context, Locale.getDefault());
//
//        try {
//        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//        } catch (IOException e) {
//        e.printStackTrace();
//        }
//
//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();
//        holder.nearestPlaceAve.setText(address);
////        holder.nearestPlaceCity.setText(mCity[position]);
//        holder.nearestPlaceDistance.setText(String.valueOf(distances1.get(position)));
//
//
//        holder.nearestPlaceImage.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Intent intent = new Intent(context,NearestLocMapsActivity.class);
//        intent.putExtra("lat",latitude);
//        intent.putExtra("lng",longitude);
//        context.startActivity(intent);
//        }
//        });