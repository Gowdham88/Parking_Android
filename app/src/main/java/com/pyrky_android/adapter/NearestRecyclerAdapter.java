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

import com.google.android.gms.maps.model.LatLng;
import com.pyrky_android.R;
import com.pyrky_android.activity.NearestLocMapsActivity;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by thulirsoft on 7/3/18.
 */

public class NearestRecyclerAdapter extends RecyclerView.Adapter<NearestRecyclerAdapter.ViewHolder> {

    private Context context;
    private int images[];
    private String[] mAve,mCity;
    double lat[] = {70.01383623,56.50329796,1.23736985,-24.33605988,11.38350584,
            -58.68375965,44.87310434,147.64797704,-3.02408824,-21.33447419};
    double lng[] = {-24.21957723,56.50329796,-163.58662616,16.88948658,62.62863347,
            -43.46925429,-91.28527609,85.94545339,-82.49033554,-175.53067807};
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    public NearestRecyclerAdapter(Context context, int[] images, String[] mAve, String[] mCity) {
        this.context = context;
        this.images = images;
        this.mAve = mAve;
        this.mCity = mCity;
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
        holder.nearestPlaceImage.setImageResource(images[position]);
        holder.nearestPlaceAve.setText(mAve[position]);
        holder.nearestPlaceCity.setText(mCity[position]);
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
        TextView nearestPlaceAve,nearestPlaceCity;
        ViewHolder(View itemView) {
            super(itemView);
            nearestPlaceImage = itemView.findViewById(R.id.nearest_place_image);
            nearestPlaceAve = itemView.findViewById(R.id.nearest_place_ave);
            nearestPlaceCity = itemView.findViewById(R.id.nearest_place_city);
        }
    }
}
