package com.polsec.pyrky.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.NearestLocMapsActivity;
import com.polsec.pyrky.pojo.Booking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class CurrentBookingRecyclerAdapter extends RecyclerView.Adapter<CurrentBookingRecyclerAdapter.ViewHolder> {
    private Context context;

    List<Booking> bookingList = new ArrayList<Booking>();
    String Datetime;
    public CurrentBookingRecyclerAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_current_booking_list, parent, false);

        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        long time= Long.parseLong(bookingList.get(position).getDateTime());

        long dv = Long.valueOf(time)*1000;// its need to be in milisecond
        Date df = new Date(dv);
        Datetime= new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(df);

        Log.e("vv",Datetime);


        Boolean status= Boolean.valueOf(bookingList.get(position).getDestName());
        if(status){
            holder.city.setText(bookingList.get(position).getDestName());
            holder.dateTime.setText(Datetime);
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView city,dateTime,viewCar;
        ViewHolder(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.place);
            dateTime = itemView.findViewById(R.id.date_time);
            viewCar = itemView.findViewById(R.id.view_car_text);
        }
    }
}
