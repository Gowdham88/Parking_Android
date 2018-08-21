package com.polsec.pyrky.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
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
import com.polsec.pyrky.R;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.preferences.PreferencesHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 String currenRating;
    int count = 0;
    String docid,uid,Rating;
    RatingBar ratingBar,ratingbar1;
    FirebaseFirestore db;

    FirebaseAuth mAuth;
    String mUid;
    Map<String, Object> bookingid = new HashMap<>();

    Map<String, Object> bookingid1=new HashMap<>();



    List<Booking> bookingList = new ArrayList<Booking>();
    String Datetime;
    public HistoryRecyclerAdapter(Context context, List<Booking> bookingList, Map<String, Object> bookingid1) {
        this.context = context;
        this.bookingList = bookingList;
        this.bookingid1=bookingid1;

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
        TextView city,dateTime,viewCar;
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

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        mPosition = position;
        double time= bookingList.get(position).getDateTime();

//        long dv = Long.valueOf(String.valueOf(time))*1000;// its need to be in milisecond
//        Date df = new Date(dv);
//        Datetime= new SimpleDateFormat("dd MMM,  hh:mma").format(df);
//        String strh = Datetime.replace("AM", "am").replace("PM","pm");
//        Log.e("vv",strh);
//        Dateday= new SimpleDateFormat("").format(df);
//        Datetime= new SimpleDateFormat("").format(df);
//        Datemothname=getMonthShortName(Integer.parseInt(Datemonth));

         ratingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    showDialog();
                    return false;
                }
            });


        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
        Boolean status= Boolean.valueOf(bookingList.get(position).getBookingStatus());

//        if(!status){
//            holder.city.setText(bookingList.get(position).getDestName());
//        }
        for (Map.Entry<String, Object> entry : bookingid1.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());

            Boolean val = (Boolean) entry.getValue();
            String values = String.valueOf(val);

            Log.e("valuesc", values);
            if(val){
                holder.city.setText(bookingList.get(position).getDestName());
            }


//                                Toast.makeText(getActivity(), followcount, Toast.LENGTH_SHORT).show();

        }


//        final FirebaseUser user = mAuth.getCurrentUser();
//        DocumentReference docRef = db.collection("users").document(user.getUid());
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//
//                if (documentSnapshot.exists()){
//
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//
////                    Toast.makeText(ViewImageActivity.this, "ok", Toast.LENGTH_SHORT).show();
//
//
//                } else {
//
//
//
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Log.w("Error", "Error adding document", e);
//                Toast.makeText(context,"Login failed", Toast.LENGTH_SHORT).show();
//            }
//        });



//
//        if(!status){
//            holder.city.setText(bookingList.get(position).getDestName());
////            holder.dateTime.setText(strh);
//
//            double rate=bookingList.get(position).getParkingSpaceRating();
////            Toast.makeText(context, rate, Toast.LENGTH_SHORT).show();
//
//            ratingBar.setRating((float) rate);
//            Drawable drawable = ratingBar.getProgressDrawable();
//            drawable.setColorFilter(Color.parseColor("#00B9AB"), PorterDuff.Mode.SRC_ATOP);
////            ratingBar.setRating(Float.parseFloat(rate));
////            Drawable drawable = ratingBar.getProgressDrawable();
////            drawable.setColorFilter(Color.parseColor("#00B9AB"), PorterDuff.Mode.SRC_ATOP);
//
//        }
//        else{
//            holder.itemView.setVisibility(View.GONE);
//            param.height = 0;
//            param.width = 0;
//        }
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
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                currenRating = String.valueOf(v);



//                ratingBar.setVisibility(View.GONE);
//                ratingbar1.setRating(Float.parseFloat(currenRating));
                Toast.makeText(context, "New default rating: " + v, Toast.LENGTH_SHORT).show();
            }
        });

//        GalleryIcon = (ImageView) bottomSheetView.findViewById(R.id.gallery_icon);
//        CameraIcon = (ImageView) bottomSheetView.findViewById(R.id.camera_image);
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> rating = new HashMap<>();
                rating.put("parkingSpaceRating", currenRating);
                Toast.makeText(context, "New default rating: " + currenRating, Toast.LENGTH_SHORT).show();
                db.collection("Bookings").document(docid).update(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Rating updated", Toast.LENGTH_SHORT).show();
//                        ratingBar.setVisibility(View.GONE);
//                  ratingbar1.setRating(Float.parseFloat(currenRating));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

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


        public void showDialog(int currentRating, View v){
        isPopUpShowing = true;
        AlertDialog.Builder popDialog = new AlertDialog.Builder(context);

        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        final RatingBar rating = new RatingBar(context);
        rating.setLayoutParams(lp);
        rating.setMax(5);
        rating.setNumStars(5);
        rating.setRating(currentRating);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(rating);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                currentRating[mPosition] = ( int ) v;
                System.out.println("Rated val:"+v);
            }
        });

        popDialog.setTitle("Rate the space");

        popDialog.setView(linearLayout);
        popDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int pos = getItemCount();
                if (pos != RecyclerView.NO_POSITION){

                }
                String stars = String.valueOf(rating.getRating());
                Toast.makeText(context, stars, Toast.LENGTH_SHORT).show();
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
//


    @Override
    public int getItemCount() {
        return bookingList.size();
    }


}
