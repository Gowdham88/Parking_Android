package com.polsec.pyrky.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.NearestLocMapsActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by czsm4 on 09/08/18.
 */

public class CarouselDetailMapAdapter extends RecyclerView.Adapter<CarouselDetailMapAdapter.ViewHolder>{

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
    ArrayList<String> ruls = new ArrayList<>();
    ArrayList<Double> mLocationDistancesmtrs = new ArrayList<>();
    List<Address> yourAddresses;
    List<Address> yourAddress = null;
//    private ListAdapterListener mListener;
    private RecyclerView parentRecycler;
    int postionval;
    int distance,distanceval;

    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";


//    public interface ListAdapterListener {
//        void onClickimageButton(int position, String actionLikeButtonClicked, String s, String s1, String mapvalues, String s2, String s3);
////        void onClickimageButton(int position, String actionLikeButtonClicked, String s, String s1); // create an interface
//
//
//
//
//
////
//    }
    public CarouselDetailMapAdapter(Context context, ArrayList<String> nearimg, ArrayList<String> nearlat1, ArrayList<String> nearlong1, ArrayList<String> distances1, ArrayList<String> Placename, ArrayList<String> ruls, ArrayList<Double> mLocationDistancesmtrs, int distance, NearestLocMapsActivity nearestLocMapsActivity) {
        this.context = context;
        this.nearimg = nearimg;
        this.nearlat1 = nearlat1;
        this.nearlong1 = nearlong1;
        this.distances1 = distances1;
        this.Placename=Placename;
        this.ruls=ruls;
        this.distance=distance;
        this.mLocationDistancesmtrs=mLocationDistancesmtrs;
//        this.mListener=mListener;
        notifyDataSetChanged();
    }

//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        parentRecycler = recyclerView;
//    }

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

        Picasso.with(context).load(nearimg.get(position))
                .fit()
                .into(holder.nearestPlaceImage);

        postionval =holder.getAdapterPosition();
        if(distance>0){
            if(distance>0 && distance <=100 ){
                holder.nearestPlaceDistance.setText("0 - 100m");
            }
            else if(distance>100 && distance <=200 ){
                holder.nearestPlaceDistance.setText("100 - 200m");
            }
            else if(distance>200 && distance <=300 ){
                holder.nearestPlaceDistance.setText("200 - 300m");
            }

            else if(distance>300 && distance <=400 ){
                holder.nearestPlaceDistance.setText("300 - 400m");
            }

            else if(distance>400 && distance <=500 ){
                holder.nearestPlaceDistance.setText("400 - 500m");
            }

            else if(distance>500 && distance <=600 ){
                holder.nearestPlaceDistance.setText("500 - 600m");
            }

            else if(distance>600 && distance <=700 ){
                holder.nearestPlaceDistance.setText("600 - 700m");
            }

            else if(distance>700 && distance <=800 ){
                holder.nearestPlaceDistance.setText("700 - 800m");
            }

            else if(distance>800 && distance <=900 ){
                holder.nearestPlaceDistance.setText("800 - 900m");
            }

            else {
                holder.nearestPlaceDistance.setText("900 - 1000m");
            }
        }
        else {
            double disval=mLocationDistancesmtrs.get(position);
            Log.e("val", String.valueOf(disval));


            distanceval=(int) Double.parseDouble(String.valueOf(disval));
            Log.e("distanceval", String.valueOf(distanceval));
            if(!mLocationDistancesmtrs.get(position).equals(null)){
                if(distanceval>0 && distanceval <=100 ){
                    holder.nearestPlaceDistance.setText("0 - 100m");
                }
                else if(distanceval>100 && distanceval <=200 ){
                    holder.nearestPlaceDistance.setText("100 - 200m");
                }
                else if(distanceval>200 && distanceval <=300 ){
                    holder.nearestPlaceDistance.setText("200 - 300m");
                }

                else if(distanceval>300 && distanceval <=400 ){
                    holder.nearestPlaceDistance.setText("300 - 400m");
                }

                else if(distanceval>400 && distanceval <=500 ){
                    holder.nearestPlaceDistance.setText("400 - 500m");
                }

                else if(distanceval>500 && distanceval <=600 ){
                    holder.nearestPlaceDistance.setText("500 - 600m");
                }

                else if(distanceval>600 && distanceval <=700 ){
                    holder.nearestPlaceDistance.setText("600 - 700m");
                }

                else if(distanceval>700 && distanceval <=800 ){
                    holder.nearestPlaceDistance.setText("700 - 800m");
                }

                else if(distanceval>800 && distanceval <=900 ){
                    holder.nearestPlaceDistance.setText("800 - 900m");
                }

                else {
                    holder.nearestPlaceDistance.setText("900 - 1000m");
                }
            }

        }



//        holder.nearestPlaceAve.setText(mAve[position]);
//        holder.nearestPlaceCity.setText(mCity[position]);

//        S val= Integer.parseInt(distances1.get(position));

        final double latitude = Double.parseDouble(nearlat1.get(position));
        final double  longitude = Double.parseDouble(nearlong1.get(position));

        Geocoder geocoder;

        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            yourAddresses= geocoder.getFromLocation(Double.parseDouble(nearlat1.get(position)),Double.parseDouble(nearlong1.get(position)) , 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (yourAddresses.size() > 0)
        {
            String yourAddress = yourAddresses.get(0).getAddressLine(0);
            String yourCity = yourAddresses.get(0).getAddressLine(1);
            String yourCountry = yourAddresses.get(0).getAddressLine(2);


        }

//    Toast.makeText(context, (int) latitude, Toast.LENGTH_SHORT).show();
        holder.nearestPlaceAve.setText(Placename.get(position));
        holder.nearestPlaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
                String mapvalues="mapvalues";
//                mListener.onClickimageButton(position,ACTION_LIKE_BUTTON_CLICKED,nearlat1.get(position),nearlong1.get(position),mapvalues,Placename.get(position),ruls.get(position));

            }
        });
    }



    @Override
    public int getItemCount() {
        return nearlat1.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView nearestPlaceImage;
        TextView nearestPlaceAve,nearestPlaceCity,nearestPlaceDistance;
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
