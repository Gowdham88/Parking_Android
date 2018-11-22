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
        List<String> nearimg = new ArrayList<>();
        ArrayList<String> nearlat1 = new ArrayList<>();
        ArrayList<String> nearlong1 = new ArrayList<>();
        ArrayList<String> distances1 = new ArrayList<>();
    ArrayList<String> Placename = new ArrayList<>();
    ArrayList<Double> caldis1 = new ArrayList<>();
    ArrayList<String> mCameraID = new ArrayList<>();
    List<NearestData> mNearestDataList=new ArrayList<NearestData>();
    ArrayList<String> parkingTypes=new ArrayList<>();
    ArrayList<HashMap<String, Object>> parkingRules  = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> mrlslist=new HashMap<String, Object>();
    List<Address> yourAddresses;
    List<Address> yourAddress = null;
    String value="carousel",stringcartypeval ;
    int distanceval,cartype;
//public CarouselNearestAdapter(Context context, ArrayList<String> nearimg, ArrayList<String> nearlat1, ArrayList<String> nearlong1, ArrayList<String> distances1, ArrayList<String> Placename, ArrayList<Double> caldis1, ArrayList<String> mCameraID, ArrayList<HashMap<String, Object>> parkingRules, ArrayList<String> parkingTypes) {
//        this.context = context;
//        this.nearimg = nearimg;
//        this.nearlat1 = nearlat1;
//        this.nearlong1 = nearlong1;
//        this.distances1 = distances1;
//        this.Placename=Placename;
//        this.caldis1=caldis1;
//        this.mCameraID=mCameraID;
//        this.parkingRules=parkingRules;
//        this.parkingTypes=parkingTypes;
//        }

    public  CarouselNearestAdapter(Context context, List<NearestData> mNearestDataList) {

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
        this.avatarSize =context.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
    int adapterPosition = holder.getAdapterPosition();
        Picasso.with(context).load(mNearestDataList.get(adapterPosition).getCameraImageUrl())
        .fit()
        .into(holder.nearestPlaceImage);

    Double disval= Double.valueOf(String.valueOf(mNearestDataList.get(position).getLocationDistance().toString()));
    Log.e("val", String.valueOf(disval));

    cartype = Integer.parseInt(String.valueOf(PreferencesHelper.getPreference(context, PreferencesHelper.PREFERENCE_PROFILE_CAR)));
    if(cartype==0){
        stringcartypeval="Compact";
    }
    else if(cartype==1){
        stringcartypeval="Small";
    }
    else if(cartype==2){
        stringcartypeval="Mid size";
    }
    else if(cartype==3){
        stringcartypeval="Full";
    }
    else {
        stringcartypeval="Van/Pick-up";
    }
    distanceval= (int) Double.parseDouble(String.valueOf(disval));

    Log.e("distanceval", String.valueOf(distanceval));
    if(!mNearestDataList.get(position).equals(null)){
        if(distanceval>0 && distanceval <=1000 ){
            holder.nearestPlaceDistance.setText("0 - 1000m");
        }
        else if(distanceval>1000 && distanceval <=2000 ){
            holder.nearestPlaceDistance.setText("1000 - 2000m");
        }
        else if(distanceval>2000 && distanceval <=3000 ){
            holder.nearestPlaceDistance.setText("2000 - 3000m");
        }

        else if(distanceval>3000 && distanceval <=4000 ){
            holder.nearestPlaceDistance.setText("3000 - 4000m");
        }

        else if(distanceval>4000 && distanceval <=5000 ){
            holder.nearestPlaceDistance.setText("4000 - 5000m");
        }

        else if(distanceval>5000 && distanceval <=6000 ){
            holder.nearestPlaceDistance.setText("5000 - 6000m");
        }

        else if(distanceval>6000 && distanceval <=7000 ){
            holder.nearestPlaceDistance.setText("6000 - 7000m");
        }

        else if(distanceval>7000 && distanceval <=8000 ){
            holder.nearestPlaceDistance.setText("7000 - 8000m");
        }

        else if(distanceval>8000 && distanceval <=9000 ){
            holder.nearestPlaceDistance.setText("800 - 900m");
        }

        else {
            holder.nearestPlaceDistance.setText("10000m and above");
        }
    }


//        holder.nearestPlaceDistance.setText(caldis1.get(position));


final double latitude = Double.parseDouble(mNearestDataList.get(position).getCameraLat());
final double  longitude = Double.parseDouble(mNearestDataList.get(position).getCameraLong());

//    Geocoder geocoder;
//
//    geocoder = new Geocoder(context, Locale.getDefault());
//    try {
//        yourAddresses= geocoder.getFromLocation(Double.parseDouble(mNearestDataList.get(position).getCameraLat()),Double.parseDouble(mNearestDataList.get(position).getCameraLong()) , 1);
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//
//    if (yourAddresses.size() > 0)
//    {
//        String yourAddress = yourAddresses.get(0).getAddressLine(0);
//        String yourCity = yourAddresses.get(0).getAddressLine(1);
//        String yourCountry = yourAddresses.get(0).getAddressLine(2);
//
//
//    }

//    Toast.makeText(context, (int) latitude, Toast.LENGTH_SHORT).show();
    holder.nearestPlaceAve.setText(mNearestDataList.get(adapterPosition).getCameraLocationName());
        holder.nearestPlaceImage.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {


    FragmentTransaction transaction =  ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.main_frame_layout, NearestLocMapsActivity.newInstance(mNearestDataList.get(position).getCameraLat(),mNearestDataList.get(position).getCameraLong(),value,holder.getAdapterPosition(),mNearestDataList.get(position).getCameraLocationName(),distanceval,mNearestDataList.get(position).getCameraImageUrl()
            ,mNearestDataList.get(position).getCameraID(),mNearestDataList.get(position).getParkingRules(),mNearestDataList.get(position).getParkingTypes(),stringcartypeval));
    transaction.addToBackStack(null).commit();

    Log.e("position", String.valueOf(mNearestDataList.get(position).getCameraID()));
    Log.e("positionlatlong", String.valueOf(mNearestDataList.get(position).getCameraLat()+","+mNearestDataList.get(position).getCameraLong()));
        }
        });
        }

@Override
public int getItemCount() {
        return mNearestDataList.size();
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