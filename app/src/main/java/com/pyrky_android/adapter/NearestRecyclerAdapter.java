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

/**
 * Created by thulirsoft on 7/3/18.
 */

public class NearestRecyclerAdapter extends RecyclerView.Adapter<NearestRecyclerAdapter.ViewHolder> {

    private Context context;
    private int images[];
    private String[] mAve,mCity;
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

        holder.nearestPlaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, NearestLocMapsActivity.class));
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
