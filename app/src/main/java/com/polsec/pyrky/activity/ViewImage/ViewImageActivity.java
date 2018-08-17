package com.polsec.pyrky.activity.ViewImage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.activity.NearestLocMapsActivity;
import com.polsec.pyrky.activity.forgotpassword.ForgotpasswordActivity;
import com.polsec.pyrky.activity.signin.SignInActivity;
import com.polsec.pyrky.activity.signup.SignupScreenActivity;
import com.polsec.pyrky.adapter.CarouselDetailMapAdapter;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.pojo.Camera;
import com.polsec.pyrky.pojo.Users;
import com.polsec.pyrky.preferences.PreferencesHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static android.content.ContentValues.TAG;
import static com.google.ar.core.ArCoreApk.InstallStatus.INSTALLED;
import static com.google.ar.core.ArCoreApk.InstallStatus.INSTALL_REQUESTED;

public class ViewImageActivity extends AppCompatActivity {
    TextView BookBtn;
    ImageView CloseImg;
    double latitude,longitude;
    Context context;
    String documentID;
    double parkingSpaceRating;
    Boolean protectCar,bookingStatus;
    String DestName,lat,longi,mUid;
    List<Users> bookinglist = new ArrayList<Users>();
     FirebaseFirestore db;
    List<String> booking_ID = new ArrayList<>();
    FirebaseAuth mAuth;
    Map<String, Object> bookingid = new HashMap<>();
    int currentapiVersion;
    private boolean mUserRequestedInstall = true;
        Session mSession;
    @Override
    protected void onResume() {
        super.onResume();

        // Check camera permission.


        // Make sure ARCore is installed and up to date.


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mAuth = FirebaseAuth.getInstance();
        mUid= PreferencesHelper.getPreference(ViewImageActivity.this, PreferencesHelper.PREFERENCE_FIREBASE_UUID);
//        BackImg = (ImageView) findViewById(R.id.back_image);
//        BackImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        currentapiVersion= android.os.Build.VERSION.SDK_INT;
        db = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitude = extras.getDouble("latitude");
            longitude= extras.getDouble("longitude");
            DestName= extras.getString("place");
            lat= String.valueOf(latitude);
            longi= String.valueOf(longitude);

            Log.e("lattitudeview", String.valueOf(latitude));
                Log.e("longitudeview", String.valueOf(longitude));
            Log.e("place", String.valueOf(DestName));


//            Toast.makeText(ViewImageActivity.this, latitude, Toast.LENGTH_SHORT).show();

            //The key argument here must match that used in the other activity
        }


        BookBtn=(TextView)findViewById(R.id.book_btn);
        BookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if (mSession == null) {
                        switch (ArCoreApk.getInstance().requestInstall(ViewImageActivity.this, mUserRequestedInstall)) {
                            case INSTALLED:
                                // Success, create the AR session.
                                mSession = new Session(ViewImageActivity.this);

                                showBottomSheet(latitude,longitude);
                                break;
                            case INSTALL_REQUESTED:

                                showBottomSheet1(latitude,longitude);
                                // Ensures next invocation of requestInstall() will either return
                                // INSTALLED or throw an exception.
                                mUserRequestedInstall = false;
                                return;
                        }
                    }
                } catch (UnavailableUserDeclinedInstallationException e) {
                    // Display an appropriate message to the user and return gracefully.
                    Toast.makeText(ViewImageActivity.this, "TODO: handle exception " + e, Toast.LENGTH_LONG)
                            .show();
                    return;
                } catch (Exception e) {  // Current catch statements.

                    return;  // mSession is still null.
                }

                if (currentapiVersion > 14) {
                    Toast.makeText(ViewImageActivity.this, "above", Toast.LENGTH_SHORT).show();
                    // Do something for 14 and above versions


                } else {

                    Toast.makeText(ViewImageActivity.this, "below", Toast.LENGTH_SHORT).show();

                }


                SaveData(lat,longi,DestName);
            }
        });
        CloseImg=(ImageView)findViewById(R.id.close_iconimg);
        CloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                                    Log.e("bookingid", String.valueOf(bookingid));






                                } else {
//                        Log.d(TAG, "No such document");

                                }
                            } else {
//                    Log.d(TAG, "get failed with ", task.getException());

                            }
                        }
                    });

                    Toast.makeText(ViewImageActivity.this, "ok", Toast.LENGTH_SHORT).show();

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


        parkingSpaceRating=0;
        protectCar=false;
        bookingStatus=true;
//          locationTxt=Location_Txt.getText().toString();
//        String photoURL = PreferencesHelper.getPreference(this, PreferencesHelper.PREFERENCE_PHOTOURL);



        final Map<String, Boolean> likeData = new HashMap<>();
        likeData.put(uid, false);
        documentID="";

        Booking bookingdata = new Booking(uid,latitude,longitude,destName,getPostTime(),bookingStatus,documentID,parkingSpaceRating,protectCar);


        db.collection("Bookings").add(bookingdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                documentID = documentReference.getId();
//                PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_DOCMENTID, documentID);

                Booking bookingdata = new Booking(uid,latitude,longitude,destName,getPostTime(),bookingStatus,documentID,parkingSpaceRating,protectCar);
                Map<String, Object> docID = new HashMap<>();
                docID.put("documentID", documentID);


                db.collection("Bookings").document(documentID).update(docID).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_DOCUMENTID,documentID);

//                        try {

                            final Map<String, Boolean> likeData1 = new HashMap<>();
                            likeData1.put(documentID, true);



                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            DocumentReference washingtonRef = db.collection("users").document(uid);

                            washingtonRef
                                    .update("Booking_ID",likeData1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });



//                        }
//
//                        catch (NullPointerException e) {
//
//                            e.printStackTrace();
//                        }

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

    private void popup() {
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

                Map<String, Object> docstatus = new HashMap<>();
                docstatus.put("bookingStatus", false);

                db.collection("Bookings").document(documentID).update(docstatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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
        alertDialog1.show();
//        alertDialog1.getWindow().setLayout((int) Utils.convertDpToPixel(228,getActivity()),(int)Utils.convertDpToPixel(220,getActivity()));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());
//        lp.height=200dp;
//        lp.width=228;
        lp.gravity = Gravity.CENTER;
//        lp.windowAnimations = R.style.DialogAnimation;
        alertDialog1.getWindow().setAttributes(lp);
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



    private void showBottomSheet1(double latitude, double longitude) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ViewImageActivity.this);
        LayoutInflater factory = LayoutInflater.from(ViewImageActivity.this);
        View bottomSheetView = factory.inflate(R.layout.mapspyrky_bottomsheet1, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();



        // do something for phones running an SDK before 14


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
    private void showBottomSheet(double latitude, double longitude) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ViewImageActivity.this);
        LayoutInflater factory = LayoutInflater.from(ViewImageActivity.this);
        View bottomSheetView = factory.inflate(R.layout.mapspyrky_bottomsheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();



            // do something for phones running an SDK before 14


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
    public double getPostTime() {

        Date currentDate = new Date();
        long unixTime = currentDate.getTime() / 1000;
        return unixTime;


    }

}
