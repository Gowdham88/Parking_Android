package com.polsec.pyrky.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.signin.SignInActivity;
import com.polsec.pyrky.helper.RecyclerDiffCallBack;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.pojo.Camera;
import com.polsec.pyrky.pojo.Reports;
import com.polsec.pyrky.pojo.ratingval;
import com.polsec.pyrky.preferences.PreferencesHelper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>{
    private Context context;
    private int currenRating;
    int count = 0;
    private android.support.v7.app.AlertDialog dialog;
    String docid,uid,cameraid,lat,longi,bookingidval;
    private RatingBar ratingBar;
           TextView ratingbar1,viewCar;
    FirebaseFirestore db;

    FirebaseAuth mAuth;

    Map<String, Object> bookingid1=new HashMap<>();
    List<Camera>CameraList = new ArrayList<Camera>();
    ArrayList<ratingval> vehList = new ArrayList<ratingval>();

    List<Booking> bookingList = new ArrayList<Booking>();
    String Datetime;

    HistoryRecyclerAdapter recyclerAdapter;
    RecyclerView mRecyclerView;
    private int mPosition;

    public HistoryRecyclerAdapter(Context context, List<Booking> bookingList, Map<String, Object> bookingid1, List<Camera> cameraList, HistoryRecyclerAdapter recyclerAdapter, RecyclerView mRecyclerView) {
        this.context = context;
        this.bookingList = bookingList;
        this.mRecyclerView=mRecyclerView;

    }

    public HistoryRecyclerAdapter(Context context, List<Booking> bookingList, RecyclerView mRecyclerView) {
        this.context = context;
        this.bookingList = bookingList;
        this.mRecyclerView=mRecyclerView;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_booking_list, parent, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = PreferencesHelper.getPreference(context, PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        docid=PreferencesHelper.getPreference(context, PreferencesHelper.PREFERENCE_DOCUMENTID);
//        Toast.makeText(context, docid, Toast.LENGTH_SHORT).show();


        //view.setOnClickListener(HomeActivity.myOnClickListener);

        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView city,dateTime;
        RelativeLayout Rating_lay;

        ViewHolder(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.place);
            dateTime = itemView.findViewById(R.id.date_time);
            viewCar = itemView.findViewById(R.id.view_car_text);
            ratingBar = itemView.findViewById(R.id.history_ratings);
            ratingbar1 = itemView.findViewById(R.id.history_ratingsanother);
            Rating_lay=itemView.findViewById(R.id.view_car_text_lay);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        mPosition = position;

        double time= Double.parseDouble(String.valueOf(bookingList.get(position).getDateTime()));
        int vali= (int) time;

        long dv = Long.valueOf(String.valueOf(vali))*1000;// its need to be in milisecond
        Date df = new Date(Long.valueOf((long) dv));
        Datetime= new SimpleDateFormat("dd MMM,  hh:mma").format(df);
        String str = Datetime.replace("AM", "am").replace("PM","pm");


//        Log.e("vv", String.valueOf(str));
        holder.dateTime.setText(str);

        bookingidval=bookingList.get(position).getDocumentID();

        holder.city.setText(bookingList.get(position).getDestName());


        cameraid=bookingList.get(position).getCameraId();
        lat=bookingList.get(position).getDestLat();
        longi=bookingList.get(position).getDestLong();
        String latlong=lat+","+longi;
        ratingBar.setRating((int) bookingList.get(position).getParkingSpaceRating());


//        ratingBar.setRating((float) bookingList.get(position).getParkingSpaceRating());
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#00B9AB"), PorterDuff.Mode.SRC_ATOP);
        ratingBar.setIsIndicator(true);

     ratingBar.setTag(bookingList.get(position));

//        String ratval= String.valueOf(bookingList.get(position).getParkingSpaceRating());

        if(bookingList.get(position).getParkingSpaceRating()<=0){
            ratingBar.setVisibility(View.GONE);
            viewCar.setVisibility(View.GONE);
            ratingbar1.setVisibility(View.VISIBLE);
            ratingbar1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    String current = bookingList.get(position).getDocumentID();

                    showDialog(uid,cameraid,latlong,current,position);

                    return false;
                }
            });

        }
        else{

            ratingBar.setVisibility(View.VISIBLE);
            ratingbar1.setVisibility(View.GONE);
            viewCar.setVisibility(View.VISIBLE);
        }

//
    }


    private void showDialog(String uid, String cameraid, String latlongi, String current, int position) {

        Dialog dialog = new Dialog(context);
        LayoutInflater factory = LayoutInflater.from(context);
        View bottomSheetView = factory.inflate(R.layout.history_ratingalert, null);
        dialog.setContentView(bottomSheetView);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        TextView Oktaxt = (TextView) dialog.findViewById(R.id.cancel_txt);
        RatingBar ratingbar = (RatingBar) dialog.findViewById(R.id.rtbHighscr);
        TextView canceltxt = (TextView) dialog.findViewById(R.id.ok_txt);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                currenRating = (int) v;

//                ratingBar.setRating((int) currenRating);

//                Toast.makeText(context, "New default rating: " + v, Toast.LENGTH_SHORT).show();
            }
        });

//        GalleryIcon = (ImageView) bottomSheetView.findViewById(R.id.gallery_icon);
//        CameraIcon = (ImageView) bottomSheetView.findViewById(R.id.camera_image);
        Oktaxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> rating = new HashMap<>();
                rating.put("parkingSpaceRating", currenRating );
//                Toast.makeText(context, "New default rating: " + currenRating, Toast.LENGTH_SHORT).show();


                db.collection("Bookings").document(current).update(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        List<Booking> mRatingUpdatedList = new ArrayList<Booking>(bookingList);
                        mRatingUpdatedList.get(position).setParkingSpaceRating(currenRating);

                        HistoryRecyclerAdapter historyRecyclerAdapter = new HistoryRecyclerAdapter(context,bookingList,mRecyclerView);
                        mRecyclerView.setAdapter(historyRecyclerAdapter);




                        hideProgressDialog();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressDialog();
                    }
                });

                ratingbar1.setVisibility(View.GONE);
                        ratingBar.setVisibility(View.VISIBLE);
                        ratingBar.setRating(currenRating);
                        ratingBar.setIsIndicator(true);
                        Drawable drawable = ratingBar.getProgressDrawable();
                        drawable.setColorFilter(Color.parseColor("#00B9AB"), PorterDuff.Mode.SRC_ATOP);


                ratingval vehiclePojoObject=new ratingval();
                vehiclePojoObject.setUser_ID(uid);
                vehiclePojoObject.setRatings(String.valueOf(currenRating));

                vehList.add(vehiclePojoObject);
//
                Reports reports=new Reports(vehList,cameraid);
//
                db.collection("Reports").document(latlongi).set(reports).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                            public void onSuccess(Void aVoid) {
                                hideProgressDialog();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });



//

                dialog.dismiss();

            }
        });

        canceltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideProgressDialog();

                dialog.dismiss();
            }
        });

    }

    public void updateBookingListItems(List<Booking> employees) {
        final RecyclerDiffCallBack diffCallback = new RecyclerDiffCallBack(this.bookingList, employees);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.bookingList.clear();
        this.bookingList.addAll(employees);
        diffResult.dispatchUpdatesTo(this);
    }
//

    public void swapItems(List<Booking> bookingList){
        this.bookingList = bookingList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void showProgressDialog() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void hideProgressDialog(){
        if(dialog!=null)
            dialog.dismiss();
    }
}
