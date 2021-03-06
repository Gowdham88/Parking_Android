package com.polsec.pyrky.adapter;

import android.content.Context;
import android.location.Address;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.NearestLocMapsActivity;
import com.polsec.pyrky.pojo.SlotsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czsm4 on 09/08/18.
 */

public class CarouselDetailMapAdapter extends RecyclerView.Adapter<CarouselDetailMapAdapter.ViewHolder> {

    private Context context;
    private int images[];
    private String[] mAve, mCity;
    private double lat[] = {70.01383623, 56.50329796, 1.23736985, -24.33605988, 11.38350584,
            -58.68375965, 44.87310434, 147.64797704, -3.02408824, -21.33447419};
    private double lng[] = {-24.21957723, 56.50329796, -163.58662616, 16.88948658, 62.62863347,
            -43.46925429, -91.28527609, 85.94545339, -82.49033554, -175.53067807};
    private String mDistance[] = {"10 - 30m", "50 - 100m", "70 - 400m", "500 - 550m", "60 - 600m",
            "150 - 300m", "80 - 90m", "150 - 155m", "30 - 35m", "800 - 850m"};


    private List<SlotsData> mNearestDataList = new ArrayList<SlotsData>();
    private int distance;


    public CarouselDetailMapAdapter(Context context, List<SlotsData> mNearestDataList, int distance) {

        this.context = context;
        this.mNearestDataList = mNearestDataList;
        this.distance = distance;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearest_loc_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//        holder.nearestPlaceImage.setImageResource(mLocationImage[position]);
//        this.avatarSize = context.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);

//        Picasso.with(context).load(mNearestDataList.get(position).getCameraImageUrl())
//                .fit()
//                .into(holder.nearestPlaceImage);
        Glide.with(context)
                .load(mNearestDataList.get(position).getCameraImageUrl())
                .into(holder.nearestPlaceImage);

        int postionval = holder.getAdapterPosition();
        if (distance > 0) {

            if (distance > 0 && distance <= 100) {
                holder.nearestPlaceDistance.setText("0 - 100m");
            } else if (distance > 100 && distance <= 200) {
                holder.nearestPlaceDistance.setText("100 - 200m");
            } else if (distance > 200 && distance <= 300) {
                holder.nearestPlaceDistance.setText("200 - 300m");
            } else if (distance > 300 && distance <= 400) {
                holder.nearestPlaceDistance.setText("300 - 400m");
            } else if (distance > 400 && distance <= 500) {
                holder.nearestPlaceDistance.setText("400 - 500m");
            } else if (distance > 500 && distance <= 600) {
                holder.nearestPlaceDistance.setText("500 - 600m");
            } else if (distance > 600 && distance <= 700) {
                holder.nearestPlaceDistance.setText("600 - 700m");
            } else if (distance > 700 && distance <= 800) {
                holder.nearestPlaceDistance.setText("700 - 800m");
            } else if (distance > 800 && distance <= 900) {
                holder.nearestPlaceDistance.setText("800 - 900m");
            } else {
                holder.nearestPlaceDistance.setText("1000m above");
            }
        } else {
            if (!mNearestDataList.get(position).getLocationDistance().toString().equals(null)) {
                Double disval = Double.valueOf(String.valueOf(mNearestDataList.get(position).getLocationDistance().toString()));
                Log.e("val", String.valueOf(disval));

                int distanceval1 = (int) Double.parseDouble(String.valueOf(disval));

                Log.e("distanceval", String.valueOf(distanceval1));
                if (!mNearestDataList.get(position).equals(null)) {
                    if (distanceval1 > 0 && distanceval1 <= 100) {
                        holder.nearestPlaceDistance.setText("0 - 100m");
                    } else if (distanceval1 > 100 && distanceval1 <= 200) {
                        holder.nearestPlaceDistance.setText("100 - 200m");
                    } else if (distanceval1 > 200 && distanceval1 <= 300) {
                        holder.nearestPlaceDistance.setText("200 - 300m");
                    } else if (distanceval1 > 300 && distanceval1 <= 400) {
                        holder.nearestPlaceDistance.setText("300 - 400m");
                    } else if (distanceval1 > 400 && distanceval1 <= 500) {
                        holder.nearestPlaceDistance.setText("400 - 500m");
                    } else if (distanceval1 > 500 && distanceval1 <= 600) {
                        holder.nearestPlaceDistance.setText("500 - 600m");
                    } else if (distanceval1 > 600 && distanceval1 <= 700) {
                        holder.nearestPlaceDistance.setText("600 - 700m");
                    } else if (distanceval1 > 700 && distanceval1 <= 800) {
                        holder.nearestPlaceDistance.setText("700 - 800m");
                    } else if (distanceval1 > 800 && distanceval1 <= 900) {
                        holder.nearestPlaceDistance.setText("800 - 900m");
                    } else {
                        holder.nearestPlaceDistance.setText("1000m above");
                    }
                }


            }

        }


//        final double latitude = Double.parseDouble(mNearestDataList.get(position).getCameraLat());
//        final double  longitude = Double.parseDouble(mNearestDataList.get(position).getCameraLong());
//
//        Geocoder geocoder;
//
//        geocoder = new Geocoder(context, Locale.getDefault());
//        try {
//            yourAddresses= geocoder.getFromLocation(Double.parseDouble(mNearestDataList.get(position).getCameraLat()),Double.parseDouble(mNearestDataList.get(position).getCameraLong()) , 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (yourAddresses.size() > 0)
//        {
//            String yourAddress = yourAddresses.get(0).getAddressLine(0);
//            String yourCity = yourAddresses.get(0).getAddressLine(1);
//            String yourCountry = yourAddresses.get(0).getAddressLine(2);
//
//
//        }

//    Toast.makeText(context, (int) latitude, Toast.LENGTH_SHORT).show();
        holder.nearestPlaceAve.setText(mNearestDataList.get(position).getCameraLocationName());
        holder.nearestPlaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
                String mapvalues = "mapvalues";
//                mListener.onClickimageButton(position,ACTION_LIKE_BUTTON_CLICKED,nearlat1.get(position),nearlong1.get(position),mapvalues,Placename.get(position),ruls.get(position));

            }
        });
    }


    @Override
    public int getItemCount() {
        return mNearestDataList.size();
    }

    public void updateDate(List<SlotsData> mNearestDataList){
        this.mNearestDataList=mNearestDataList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView nearestPlaceImage;
        TextView nearestPlaceAve, nearestPlaceCity, nearestPlaceDistance;
        RelativeLayout RelayLay;

        ViewHolder(View itemView) {
            super(itemView);
            nearestPlaceImage = itemView.findViewById(R.id.nearest_place_image);
            nearestPlaceAve = itemView.findViewById(R.id.nearest_place_ave);
            nearestPlaceCity = itemView.findViewById(R.id.nearest_place_city);
            nearestPlaceDistance = itemView.findViewById(R.id.nearest_place_distance);
            RelayLay = itemView.findViewById(R.id.container);
        }
    }


}
