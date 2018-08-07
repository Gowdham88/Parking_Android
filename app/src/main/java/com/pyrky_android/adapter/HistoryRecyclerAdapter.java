package com.pyrky_android.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pyrky_android.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>{
    private Context context;
    private String places[];
    private String dateTime[];
    private int[] currentRating;
    private int mPosition;
    private Boolean isPopUpShowing = false;
    int[] currenRating;
    int count = 0;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        mPosition = position;
        holder.city.setText(places[position]);
        holder.dateTime.setText(dateTime[position]);
//        holder.ratingBar.setRating(Integer.parseInt(String.valueOf(currenRating)));
//       ratingBar.setRating(currenRating);
//        holder.ratingBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDialog();
//            }
//        });
//        holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                if (!isPopUpShowing){
//                     showDialog();
//
////                }
////
//                return false;
//            }
//        });
//
    }

    private void showDialog() {

        Dialog dialog = new Dialog(context);
        LayoutInflater factory = LayoutInflater.from(context);
        View bottomSheetView = factory.inflate(R.layout.history_ratingalert, null);
        dialog.setContentView(bottomSheetView);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        TextView Camera = (TextView) dialog.findViewById(R.id.ok_txt);
        RatingBar ratingbar = (RatingBar) dialog.findViewById(R.id.rtbHighscr);
        TextView Gallery = (TextView) dialog.findViewById(R.id.cancel_txt);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
//       ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                currenRating = Float.valueOf(decimalFormat.format((currenRating * count + v)
//                        / ++count));
//
//
//            }
//        });
//        GalleryIcon = (ImageView) bottomSheetView.findViewById(R.id.gallery_icon);
//        CameraIcon = (ImageView) bottomSheetView.findViewById(R.id.camera_image);
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(context, "New default rating: " + currenRating, Toast.LENGTH_SHORT).show();


                dialog.dismiss();
//
//                if (hasPermissions()) {
//                    captureImage();
//                } else {
//                    EasyPermissions.requestPermissions(getActivity(), "Permissions required", PERMISSIONS_REQUEST_CAMERA, CAMERA);
//                }
            }
        });

        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (hasPermissions()) {
//                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, RC_PICK_IMAGE);
//                } else {
//                    EasyPermissions.requestPermissions(getActivity(), "Permissions required", PERMISSIONS_REQUEST_GALLERY, CAMERA);
//                }

                dialog.dismiss();
            }
        });

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetDialog.dismiss();
//            }
//        });


    }

    //    public void showDialog(int currentRating, View v){
//        isPopUpShowing = true;
//        AlertDialog.Builder popDialog = new AlertDialog.Builder(context);
//
//        LinearLayout linearLayout = new LinearLayout(context);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        final RatingBar rating = new RatingBar(context);
//        rating.setLayoutParams(lp);
//        rating.setMax(5);
//        rating.setNumStars(5);
//        rating.setRating(currentRating);
//        linearLayout.setGravity(Gravity.CENTER);
//        linearLayout.addView(rating);
//
//        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
////                currentRating[mPosition] = ( int ) v;
//                System.out.println("Rated val:"+v);
//            }
//        });
//
//        popDialog.setTitle("Rate the space");
//
//        popDialog.setView(linearLayout);
//        popDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                int pos = getItemCount();
//                if (pos != RecyclerView.NO_POSITION){
//
//                }
//                String stars = String.valueOf(rating.getRating());
//                Toast.makeText(context, stars, Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//                isPopUpShowing = false;
//            }
//        });
//        popDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.cancel();
//                isPopUpShowing = false;
//            }
//        });
//        popDialog.setCancelable(false);
//        popDialog.create();
//        popDialog.show();
//
//    }
//


    @Override
    public int getItemCount() {
        return places.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView city,dateTime,viewCar;
        RatingBar ratingBar;
        ViewHolder(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.place);
            dateTime = itemView.findViewById(R.id.date_time);
            viewCar = itemView.findViewById(R.id.view_car_text);
            ratingBar = itemView.findViewById(R.id.history_ratings);
        }
    }
}
