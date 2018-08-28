package com.polsec.pyrky.activity.ViewImage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.polsec.pyrky.activity.arnavigation.ArNavActivity;
import com.polsec.pyrky.fragment.TrackGPS;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.pojo.Users;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ViewImageActivity extends AppCompatActivity {
    TextView BookBtn;
    ImageView CloseImg;
    double curLat,curLong,latitude,longitude;
    Context context = this;
    String parkingSpaceRating,documentID;
    Boolean protectCar,bookingStatus;
    String DestName,lat,longi,mUid,CameraId,cameraImageUrl,cameraid;
    Boolean isBookedAny = false;
    List<Users> bookinglist = new ArrayList<Users>();
     FirebaseFirestore db;
    List<String> booking_ID = new ArrayList<>();
    FirebaseAuth mAuth;
    Map<String, Object> bookingid = new HashMap<>();

    Map<String, Object> bookingid1=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mAuth = FirebaseAuth.getInstance();
        mUid = PreferencesHelper.getPreference(ViewImageActivity.this, PreferencesHelper.PREFERENCE_FIREBASE_UUID);

        ImageView cameraImage = findViewById(R.id.camera_image);
//        BackImg = (ImageView) findViewById(R.id.back_image);
//        BackImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });


        TrackGPS trackGps = new TrackGPS(context);

        if (trackGps.canGetLocation()) {
            curLat = trackGps.getLatitude();
            curLong = trackGps.getLongitude();
        }
        db = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
            DestName = extras.getString("place");
            CameraId = extras.getString("cameraid");
            cameraImageUrl = extras.getString("cameraImageUrl");
            lat = String.valueOf(latitude);
            longi = String.valueOf(longitude);
            cameraid = String.valueOf(CameraId);

            Log.e("lattitudeview", String.valueOf(latitude));
            Log.e("longitudeview", String.valueOf(longitude));
            Log.e("place", String.valueOf(DestName));

            //The key argument here must match that used in the other activity
        }

        makeAlreadyBookedAlert();

        Picasso.with(context).load(cameraImageUrl).into(cameraImage);
        BookBtn = (TextView) findViewById(R.id.book_btn);
        BookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    makeAlreadyBookedAlert();
                    showBottomSheet(latitude, longitude);
                    SaveData(lat, longi, DestName);
            }


//

        });
        CloseImg = (ImageView) findViewById(R.id.close_iconimg);
        CloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//
//        DocumentReference docRef = db.collection("users").document(mUid);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
////                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        bookingid = document.getData();
//                        String count = String.valueOf(bookingid.size());
//
//                        Log.e("uidvalue", mUid);
//
//                        for (Map.Entry<String, Object> entry : bookingid.entrySet()) {
//                            System.out.println(entry.getKey() + " = " + entry.getValue());
//
//                            Boolean val = (Boolean) entry.getValue();
//                            String values = String.valueOf(val);
////                            Toast.makeText(getActivity(), values, Toast.LENGTH_SHORT).show();
//                            if (val == true) {
//
//
////                                Toast.makeText(getActivity(), followcount, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//
//                    } else {
////                        Log.d(TAG, "No such document");
//
//                    }
//                } else {
////                    Log.d(TAG, "get failed with ", task.getException());
//
//                }
//            }
//        });
//    }


//    public  void CheckBookingId(){
//
//    }

    private void makeAlreadyBookedAlert(){
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
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        bookingid = document.getData();

                                        bookingid1= (Map<String, Object>) bookingid.get("Booking_ID");


//                                    String count = String.valueOf(bookingid1.size());
//                                    Log.e("count", count);


//                                    followingcount = 1;
                                        for (Map.Entry<String, Object> entry : bookingid1.entrySet()) {
                                            System.out.println(entry.getKey() + " = " + entry.getValue());

                                            Boolean val = (Boolean) entry.getValue();
                                            String values = String.valueOf(val);

                                            Log.e("values", values);
//
                                            if (val) {
                                                isBookedAny = true;
                                                Toast.makeText(ViewImageActivity.this, values, Toast.LENGTH_SHORT).show();
                                                String valuedoc=PreferencesHelper.getPreference(getApplicationContext(),PreferencesHelper.PREFERENCE_DOCUMENTID);

                                                popup(valuedoc,entry.getKey());
                                                break;

//                                Toast.makeText(getActivity(), followcount, Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(context, "False value", Toast.LENGTH_SHORT).show();
                                            }
                                        }



                                    } else {
//                        Log.d(TAG, "No such document");

                                    }
                                } else {
//                    Log.d(TAG, "get failed with ", task.getException());

                                }
                            }
                        });

//                    Toast.makeText(ViewImageActivity.this, "ok", Toast.LENGTH_SHORT).show();


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

    private void SaveData(String latitude, String longitude, String destName) {



        final String uid = PreferencesHelper.getPreference(ViewImageActivity.this, PreferencesHelper.PREFERENCE_FIREBASE_UUID);


        parkingSpaceRating= String.valueOf(0.0);
        protectCar=false;
        bookingStatus=true;
//          locationTxt=Location_Txt.getText().toString();
//        String photoURL = PreferencesHelper.getPreference(this, PreferencesHelper.PREFERENCE_PHOTOURL);



        final Map<String, Boolean> likeData = new HashMap<>();
        likeData.put(uid, false);
        documentID="";

        Booking bookingdata = new Booking(uid,latitude,longitude,destName,getPostTime(),bookingStatus,cameraid,documentID,Double.parseDouble(parkingSpaceRating),protectCar);


        db.collection("Bookings").add(bookingdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                documentID = documentReference.getId();
//                PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_DOCMENTID, documentID);

                Booking bookingdata = new Booking(uid,latitude,longitude,destName,getPostTime(),bookingStatus,cameraid,documentID,Double.parseDouble(parkingSpaceRating),protectCar);
                Map<String, Object> docID = new HashMap<>();
                docID.put("documentID", documentID);


                db.collection("Bookings").document(documentID).update(docID).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_DOCUMENTID,documentID);

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


//                            DocumentReference washingtonRef = db.collection("users").document(uid);
//
//                            washingtonRef
//                                    .update("Booking_ID",likeData1)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Log.w(TAG, "Error updating document", e);
//                                        }
//                                    });



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
//


            }
        });
    }

    private void popup(String valuedoc,String key) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.status_alert_lay, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        Button ok = (Button)deleteDialogView.findViewById(R.id.ok_button);
        Button cancel = (Button)deleteDialogView.findViewById(R.id.cancel_button);

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
                                makeAlreadyBookedAlert();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

                alertDialog1.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            bookingid = document.getData();


                                            bookingid1= (Map<String, Object>) bookingid.get("Booking_ID");


                                            String count = String.valueOf(bookingid1.size());
                                            Log.e("count", count);


//                                    followingcount = 1;
                                            for (Map.Entry<String, Object> entry : bookingid1.entrySet()) {
                                                System.out.println(entry.getKey() + " = " + entry.getValue());

                                                Boolean val = (Boolean) entry.getValue();
                                                String values = String.valueOf(val);

                                                Log.e("values", values);
//
                                                if (val) {


                                                    popup(valuedoc,entry.getKey());
//                                Toast.makeText(getActivity(), followcount, Toast.LENGTH_SHORT).show();
                                                }
//                                                else{
//
//                                                }
                                            }



                                        } else {
//                        Log.d(TAG, "No such document");

                                        }
                                    } else {
//                    Log.d(TAG, "get failed with ", task.getException());

                                    }
                                }
                            });

//                            Toast.makeText(ViewImageActivity.this, "ok", Toast.LENGTH_SHORT).show();

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
//

//                BookBtn.setVisibility(View.GONE);
                alertDialog1.dismiss();
            }
        });



        alertDialog1.setCanceledOnTouchOutside(false);
        try {
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        alertDialog1.getWindow().setLayout((int) Utils.convertDpToPixel(228,getActivity()),(int)Utils.convertDpToPixel(220,getActivity()));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());
//        lp.height=200dp;
//        lp.width=228;
        lp.gravity = Gravity.CENTER;
//        lp.windowAnimations = R.style.DialogAnimation;
        alertDialog1.getWindow().setAttributes(lp);
        alertDialog1.show();
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

    private void showBottomSheet(double latitude, double longitude) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ViewImageActivity.this);
        LayoutInflater factory = LayoutInflater.from(ViewImageActivity.this);
        View bottomSheetView = factory.inflate(R.layout.mapspyrky_bottomsheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        TextView map = (TextView) bottomSheetView.findViewById(R.id.maps_title);
        TextView pyrky = (TextView) bottomSheetView.findViewById(R.id.pyrky_title);
//        GalleryIcon = (ImageView) bottomSheetView.findViewById(R.id.gallery_icon);
//        CameraIcon = (ImageView) bottomSheetView.findViewById(R.id.camera_image);
        TextView cancel = (TextView) bottomSheetView.findViewById(R.id.cancel_txt);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PackageManager pm = ViewImageActivity.this.getPackageManager();
                if(isPackageInstalled()){
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr="+"&daddr="+latitude+","+longitude));
                    startActivity(intent);
//                    Toast.makeText(ViewImageActivity.this, "true", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.co.in/maps?saddr="+"&daddr="+latitude+","+longitude));
                    startActivity(intent);
//                    Toast.makeText(ViewImageActivity.this, "false", Toast.LENGTH_SHORT).show();


                }
//

//                Intent postintent = new Intent(getActivity(), PostActivity.class);
//                startActivity(postintent);
                bottomSheetDialog.dismiss();
//
//                if (hasPermissions()) {
//                    captureImage();
//                } else {
//                    EasyPermissions.requestPermissions(getActivity(), "Permissions required", PERMISSIONS_REQUEST_CAMERA, CAMERA);
//                }
            }
        });

        pyrky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (hasPermissions()) {
//                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, RC_PICK_IMAGE);
//                } else {
//                    EasyPermissions.requestPermissions(getActivity(), "Permissions required", PERMISSIONS_REQUEST_GALLERY, CAMERA);
//                }
//                Intent checkinintent = new Intent(getActivity(), CheckScreenActivity.class);
//                startActivity(checkinintent);
                Intent intent = new Intent(ViewImageActivity.this, ArNavActivity.class);
                try {
                    intent.putExtra("SRC", "Current Location");
                    intent.putExtra("DEST", "Some Destination");
                    intent.putExtra("SRCLATLNG", curLat + "," + curLong);
                    intent.putExtra("DESTLATLNG", latitude + "," + longitude);
                    startActivity(intent);
                }catch (NullPointerException npe){

                    Log.d(TAG, "onClick: The IntentExtras are Empty");
                }
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
    public long getPostTime() {

        Date currentDate = new Date();
        long unixTime = currentDate.getTime() / 1000;
        return unixTime;


    }

}
