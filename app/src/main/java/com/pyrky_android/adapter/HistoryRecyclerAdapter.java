package com.pyrky_android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pyrky_android.R;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>{
    Context context;
    String places[];
    String dateTime[];
    int[] currentRating;
    int mPosition;
    AlertDialog.Builder popDialog;
    Boolean isPopUpShowing = false;
    public HistoryRecyclerAdapter(Context context, String[] places, String[] mTimeDate, int[] mCurrentRating) {
        this.context = context;
        this.places = places;
        this.dateTime = mTimeDate;
        this.currentRating = mCurrentRating;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_booking_list, parent, false);

        //view.setOnClickListener(HomeActivity.myOnClickListener);

        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        mPosition = position;
        holder.city.setText(places[position]);
        holder.dateTime.setText(dateTime[position]);
        holder.ratingBar.setNumStars(currentRating[position]);
        holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

               /* if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = holder.ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    holder.ratingBar.setRating(stars);

//                    Toast.makeText(MainActivity.this, String.valueOf("test"), Toast.LENGTH_SHORT).show();
                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }*/
                if (!isPopUpShowing){
                    showDialog();
                }

                return true;
            }
        });

    }
    public void showDialog(){
        isPopUpShowing = false;
        popDialog = new AlertDialog.Builder(context);

        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        final RatingBar rating = new RatingBar(context);
        rating.setLayoutParams(lp);
        rating.setMax(5);
        rating.setNumStars(currentRating[mPosition]);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(rating);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                System.out.println("Rated val:"+v);
            }
        });

        popDialog.setTitle("Rate the space");
        popDialog.setView(linearLayout);
        popDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isPopUpShowing = false;
            }
        });
        popDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                isPopUpShowing = false;
            }
        });
        popDialog.setCancelable(false);
        popDialog.create();
        popDialog.show();

    }

    @Override
    public int getItemCount() {
        return places.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView city,dateTime,viewCar;
        RatingBar ratingBar;
        public ViewHolder(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.place);
            dateTime = itemView.findViewById(R.id.date_time);
            viewCar = itemView.findViewById(R.id.view_car_text);
            ratingBar = itemView.findViewById(R.id.history_ratings);
        }
    }
}
