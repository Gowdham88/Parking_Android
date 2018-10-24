package com.polsec.pyrky.activity.ViewImage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.polsec.pyrky.R;

import com.polsec.pyrky.activity.ar.ArNavActivity;
import com.polsec.pyrky.fragment.TrackGPS;
import com.polsec.pyrky.pojo.Booking;

import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.Constants;


import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class ViewImageActivity extends AppCompatActivity {
    TextView BookBtn;
    double curLat,curLong,latitude1,longitude1;
         String   latitude,longitude;
    Context context = this;
    String parkingSpaceRating,documentID;
    Boolean protectCar,bookingStatus;
    String DestName,lat,longi,mUid,CameraId,cameraImageUrl,cameraid,documentiDs,DocId;
    Boolean isBookedAny = false;
     FirebaseFirestore db;

    FirebaseAuth mAuth;
    Map<String, Object> bookingid = new HashMap<>();
    ImageView Close_Img;

    Map<String, Object> bookingid1=new HashMap<>();
    private int avatarSize;

    RelativeLayout relay;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mAuth = FirebaseAuth.getInstance();
        mUid = PreferencesHelper.getPreference(ViewImageActivity.this, PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        DocId=PreferencesHelper.getPreference(ViewImageActivity.this,PreferencesHelper.PREFERENCE_DOCMENTID);
        ImageView cameraImage = findViewById(R.id.camera_image);
        relay=findViewById(R.id.rel_parent);
        Close_Img=findViewById(R.id.close_iconimg);
        Close_Img.setVisibility(View.VISIBLE);
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size1);
        TrackGPS trackGps = new TrackGPS(context);

        if (trackGps.canGetLocation()) {
            curLat = trackGps.getLatitude();
            curLong = trackGps.getLongitude();

            Log.e("curLat", String.valueOf(curLat));
            Log.e("curLong =", String.valueOf(curLong));
        }
        db = FirebaseFirestore.getInstance();


        showProgressDialog();
        Intent bundle = ViewImageActivity.this.getIntent();
        if(bundle!=null){


                latitude =bundle.getStringExtra("latitude");
                longitude = bundle.getStringExtra("longitude");
            latitude1= Double.parseDouble(latitude);
            longitude1= Double.parseDouble(longitude);

                DestName = bundle.getStringExtra("place");
                CameraId = bundle.getStringExtra("cameraid");
                cameraImageUrl = bundle.getStringExtra("cameraImageUrl");
                lat = String.valueOf(latitude);
                longi = String.valueOf(longitude);
                cameraid = String.valueOf(CameraId);

                Log.e("lattitudeview", String.valueOf(latitude));
                Log.e("longitudeview", String.valueOf(longitude));
                Log.e("place", String.valueOf(cameraImageUrl));



        }




        if(!cameraImageUrl.equals(null)|| !cameraImageUrl.isEmpty()){

//
            Glide.with(ViewImageActivity.this).load(cameraImageUrl)
                   .into(cameraImage);

            hideProgressDialog();

        }else {
            hideProgressDialog();
        }


        BookBtn = findViewById(R.id.book_btn);
        BookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    showBottomSheet(Double.parseDouble(latitude), Double.parseDouble(longitude));
            }

        });
        Close_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showBottomSheet(double latitude, double longitude) {
        View bottomSheetView;
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ViewImageActivity.this);
        LayoutInflater factory = LayoutInflater.from(ViewImageActivity.this);
        bottomSheetView = factory.inflate(R.layout.ar_pyrky_bottomsheet, null);
        TextView map = bottomSheetView.findViewById(R.id.maps_title);
        TextView pyrky = bottomSheetView.findViewById(R.id.pyrky_title);
        if (Constants.IS_AR_ENABLED){

        }else {
            pyrky.setVisibility(View.GONE);
        }

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheetView.getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) bottomSheetView.getParent()).setBackgroundColor(Color.TRANSPARENT);

        TextView cancel = bottomSheetView.findViewById(R.id.cancel_txt);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value="map";
                Boolean bookingRequest=true;
//
//                if (bookingRequest){
                    makeAlreadyBookedAlert(true, value);
//                }else{
//                    SaveData(lat, longi, DestName);
//                }
//


                bottomSheetDialog.dismiss();

            }
        });

        pyrky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value="pyrky";

                Boolean bookingRequest=true;

                if (bookingRequest){
                    makeAlreadyBookedAlert(true, value);
                }else{
                    SaveData(lat, longi, DestName);
                }
//
                bottomSheetDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });


    }


    private void bookAndNavigate(String latitude, String longitude){

        PackageManager pm =ViewImageActivity.this.getPackageManager();
        if(isPackageInstalled()){
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+"&daddr="+latitude+","+longitude));
            startActivity(intent);

        }else{
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.co.in/maps?saddr="+"&daddr="+latitude+","+longitude));
            startActivity(intent);
        }

    }
    private void SaveData(String latitude, String longitude, String destName) {



        final String uid = PreferencesHelper.getPreference(ViewImageActivity.this, PreferencesHelper.PREFERENCE_FIREBASE_UUID);


        parkingSpaceRating= String.valueOf(0.0);
        protectCar=false;
        bookingStatus=true;



        final Map<String, Boolean> likeData = new HashMap<>();
        likeData.put(uid, false);
        documentID="";

        Booking bookingdata = new Booking(uid,latitude,longitude,destName,getPostTime(),bookingStatus,cameraid,documentID,Double.parseDouble(parkingSpaceRating),protectCar);


        db.collection("Bookings").add(bookingdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                documentID = documentReference.getId();
                PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_DOCUMENTIDNEW,documentID);
                PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_DOCMENTID, documentID);



                Booking bookingdata = new Booking(uid,latitude,longitude,destName,getPostTime(),bookingStatus,cameraid,documentID,Double.parseDouble(parkingSpaceRating),protectCar);
                Map<String, Object> docID = new HashMap<>();
                docID.put("documentID", documentID);


                db.collection("Bookings").document(documentID).update(docID).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {




//                        try {

                         Map<String, Boolean> likeData1 = new HashMap<>();
                        likeData1.put( documentID, true);

                        Map<String, Map<String, Boolean>> likeData2 = new HashMap<>();
                        likeData2.put( "Booking_ID", likeData1);



                        FirebaseFirestore db = FirebaseFirestore.getInstance();


                        db.collection("users").document(uid)
                                .set(likeData2, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
    }


    private void popup(String valuedoc, String key, Boolean bookingRequest, String value) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.status_alert_lay, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = deleteDialogView.findViewById(R.id.ok_button);
        TextView cancel = deleteDialogView.findViewById(R.id.cancel_button);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Boolean> likeData1 = new HashMap<>();
                likeData1.put( key, false);

                Map<String, Map<String, Boolean>> likeData2 = new HashMap<>();
                likeData2.put( "Booking_ID", likeData1);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("users").document(mUid)
                        .set(likeData2, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");

                                PopUpprotectcar(bookingRequest,value);
                                isBookedAny = false;
                                if (bookingRequest){
                                    makeAlreadyBookedAlert(true, value);
                                }else{
                                    makeAlreadyBookedAlert(false, value);
                                }


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

                final Map<String, Object> bookingstatusdata = new HashMap<>();
                bookingstatusdata.put("bookingStatus", false);

                db.collection("Bookings").document(valuedoc)
                        .update(bookingstatusdata)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
//
                alertDialog1.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog1.dismiss();
            }
        });



        alertDialog1.setCanceledOnTouchOutside(false);
        try {
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());

        lp.gravity = Gravity.CENTER;
        alertDialog1.getWindow().setAttributes(lp);
        alertDialog1.show();
    }

    private void PopUpprotectcar(Boolean bookingRequest, String value) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.protetcar_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = deleteDialogView.findViewById(R.id.ok_button);
        TextView cancel = deleteDialogView.findViewById(R.id.cancel_button);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.parking_alert );



        final android.support.v7.app.AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(value=="map")
                {

                    bookAndNavigate(latitude, longitude);
                }
                else {

                    Intent intent = new Intent(ViewImageActivity.this, ArNavActivity.class);

                        intent.putExtra("SRC", "Current Location");
                        intent.putExtra("DEST", "Some Destination");
                        intent.putExtra("SRCLATLNG", curLat + "," + curLong);
                        intent.putExtra("DESTLATLNG", latitude + "," + longitude);
                        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
                        startActivity(intent);


                }


                mp.start();
                documentiDs =PreferencesHelper.getPreference(ViewImageActivity.this,PreferencesHelper.PREFERENCE_DOCUMENTIDNEW);
                Log.e("doc",documentiDs);
                protectCar(true,true,documentiDs);
                alertDialog1.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookAndNavigate(latitude, longitude);

                alertDialog1.dismiss();
            }
        });


        alertDialog1.setCanceledOnTouchOutside(false);
        try {
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());

        lp.gravity = Gravity.CENTER;

        alertDialog1.getWindow().setAttributes(lp);


            alertDialog1.show();


    }

    private void protectCar(Boolean protectCar, Boolean bookingStatus, String documentiDs){
        final Map<String, Object> protectdata = new HashMap<>();
        protectdata.put("protectCar", protectCar);
        protectdata.put("bookingStatus", bookingStatus);



        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("Bookings").document(documentiDs)
                .update(protectdata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    private boolean isPackageInstalled() {
        try
        {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }


    private void makeAlreadyBookedAlert(Boolean bookingRequest, String value){
        final FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    DocumentReference docRef = db.collection("users").document(mUid);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    if(document.contains("Booking_ID")){
                                        bookingid = document.getData();

                                        bookingid1= (Map<String, Object>) bookingid.get("Booking_ID");

                                        for (Map.Entry<String, Object> bookingEntry : bookingid1.entrySet()){
                                            Boolean value = (Boolean) bookingEntry.getValue();
                                            if (value){
                                                isBookedAny = true;
                                                break;
                                            }
                                        }

                                        if (isBookedAny){
                                            for (Map.Entry<String, Object> entry : bookingid1.entrySet()) {
                                                System.out.println(entry.getKey() + " = " + entry.getValue());

                                                Boolean val = (Boolean) entry.getValue();
                                                String values = String.valueOf(val);

                                                Log.e("values", values);

                                                if (val) {

                                                    String valuedoc=entry.getKey();

                                                    popup(valuedoc,entry.getKey(),bookingRequest,value);
                                                    break;

                                                }else{
                                                }
                                            }
                                        }else{

                                            if (bookingRequest){

                                                SaveData(lat, longi, DestName);
                                            }


                                        }
                                    }
                                    else {


                                        SaveData(lat, longi, DestName);

                                    }


                                } else {


                                }
                            } else {


                            }
                        }
                    });

                } else {




                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.w("Error", "Error adding document", e);
                Toast.makeText(getApplicationContext(),"Login failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public long getPostTime() {

        Date currentDate = new Date();
        long unixTime = currentDate.getTime() / 1000;
        return unixTime;


    }

    public void showProgressDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewImageActivity.this);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void hideProgressDialog(){
        if(dialog!=null)
            dialog.dismiss();
    }

}
