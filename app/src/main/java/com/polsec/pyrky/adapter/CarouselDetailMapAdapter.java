package com.polsec.pyrky.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.polsec.pyrky.R;
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
    List<Address> yourAddresses;
    List<Address> yourAddress = null;
    private ListAdapterListener mListener;
    private RecyclerView parentRecycler;
    int postionval;

    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";


    public interface ListAdapterListener {
        void onClickimageButton(int position, String actionLikeButtonClicked, String s, String s1, String mapvalues, String s2, String s3);
//        void onClickimageButton(int position, String actionLikeButtonClicked, String s, String s1); // create an interface





//
    }
    public CarouselDetailMapAdapter(Context context, ArrayList<String> nearimg, ArrayList<String> nearlat1, ArrayList<String> nearlong1, ArrayList<String> distances1, ArrayList<String> Placename, ArrayList<String> ruls, ListAdapterListener mListener) {
        this.context = context;
        this.nearimg = nearimg;
        this.nearlat1 = nearlat1;
        this.nearlong1 = nearlong1;
        this.distances1 = distances1;
        this.Placename=Placename;
        this.ruls=ruls;
        this.mListener=mListener;
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
                .inflate(R.layout.nearest_places_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//        holder.nearestPlaceImage.setImageResource(mLocationImage[position]);
        this.avatarSize = context.getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);

        Picasso.with(context).load(nearimg.get(position))
                .fit()
                .into(holder.nearestPlaceImage);

        postionval =holder.getAdapterPosition();
//        holder.nearestPlaceAve.setText(mAve[position]);
//        holder.nearestPlaceCity.setText(mCity[position]);

//        S val= Integer.parseInt(distances1.get(position));
        holder.nearestPlaceDistance.setText(distances1.get(position));

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
                mListener.onClickimageButton(position,ACTION_LIKE_BUTTON_CLICKED,nearlat1.get(position),nearlong1.get(position),mapvalues,Placename.get(position),ruls.get(position));

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
