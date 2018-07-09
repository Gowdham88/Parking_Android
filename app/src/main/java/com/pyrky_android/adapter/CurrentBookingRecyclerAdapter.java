package com.pyrky_android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pyrky_android.R;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class CurrentBookingRecyclerAdapter extends RecyclerView.Adapter<CurrentBookingRecyclerAdapter.ViewHolder> {
    Context context;
    String places[];
    String dateTime[];
    public CurrentBookingRecyclerAdapter(Context context, String[] places,String[] dateTime) {
        this.context = context;
        this.places = places;
        this.dateTime = dateTime;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_list, parent, false);

        //view.setOnClickListener(HomeActivity.myOnClickListener);

        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.city.setText(places[position]);
        holder.dateTime.setText(dateTime[position]);
    }

    @Override
    public int getItemCount() {
        return places.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView city,dateTime,viewCar;
        public ViewHolder(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.place);
            dateTime = itemView.findViewById(R.id.date_time);
            viewCar = itemView.findViewById(R.id.view_car_text);
        }
    }
}
