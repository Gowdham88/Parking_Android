package com.polsec.pyrky.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.ViewImage.ViewImageActivity;
import com.polsec.pyrky.adapter.CarouselDetailMapAdapter;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.pojo.Camera;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.Constants;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class NearestLocMapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,CarouselDetailMapAdapter.ListAdapterListener, DiscreteScrollView.OnItemChangedListener {

    Context context = this;

    CarouselDetailMapAdapter mNearestrecyclerAdapter;
//    RecyclerView mNearestPlaceRecycler;
    DiscreteScrollView mNearestPlaceRecycler;
    int mListPosition = 0;


    Location mCurrentLoc = new Location("");
    Location mNearestLocations = new Location("");

    ArrayList<String> mAccurateDistancesString = new ArrayList<>();
    ArrayList<Double> mLocationDistances = new ArrayList<>();
    ArrayList<String> mCalculateDistances = new ArrayList<>();
    ArrayList<String> mCameraLat = new ArrayList<>();
    ArrayList<String> mCameraLong = new ArrayList<>();
    ArrayList<String> mCameraImageUrl = new ArrayList<>();
    ArrayList<Double> distancesmtrscurrentmap = new ArrayList<>();
    ArrayList<String> distancescurrentarrmap = new ArrayList<>();
    ArrayList<String> mCameraLocName = new ArrayList<>();
    ArrayList<String> mCameraId = new ArrayList<>();
    private Boolean isPopUpShowing = false;
    double mAccurateDistance;
    GoogleMap Mmap;
    List<Address> yourAddresses;
    String yourplace,area;
    String documentID;
    double parkingSpaceRating;
    Boolean protectCar,bookingStatus;
    String lat,longi;

    LatLng laln;
    Location mLocation;
    List<Camera> datalist = new ArrayList<Camera>();
    ImageView mBackIcon;
    TextView TitlaTxt;
    String mLat,mLongi,PlaceName;
    String Nameval="home";
    String Nameval1="carousel",mapLat,mapLongi,cameraid;
    Camera camera;
    String parkytype,mUid,docid;
    private InfiniteScrollAdapter infiniteAdapter;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Map<String, Object> bookingid = new HashMap<>();

    Map<String, Object> bookingid1=new HashMap<>();
    Boolean val;
    RelativeLayout BackImgRelay;
    private GoogleApiClient mGoogleApiClient;
    private android.support.v7.app.AlertDialog dialog;
    Boolean isBookedAny = false;
    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage((FragmentActivity) context, 0, this)
                .addConnectionCallbacks(this)
                .build();
        mNearestPlaceRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_loc_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(NearestLocMapsActivity.this);
        showProgressDialog();
        mNearestPlaceRecycler = findViewById(R.id.nearest_places_recycler);
        mNearestPlaceRecycler.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUid = PreferencesHelper.getPreference(NearestLocMapsActivity.this, PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        docid=PreferencesHelper.getPreference(context, PreferencesHelper.PREFERENCE_DOCUMENTID);

        mBackIcon = (ImageView) findViewById(R.id.back_icon);
        TitlaTxt = (TextView) findViewById(R.id.extra_title);
        TitlaTxt.setText("Map");
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent chatIntent = getIntent();
        if(chatIntent!=null){
            String Value=chatIntent.getStringExtra("value");
            String Value1=chatIntent.getStringExtra("values");

            if(Nameval.equals(Value)){
                mLat = chatIntent.getStringExtra("latitude").trim();
                mLongi = chatIntent.getStringExtra("longitude").trim();
                PlaceName= chatIntent.getStringExtra("place").trim();
                Log.e("hlattitude", String.valueOf(mLat));
                Log.e("hlongitude", String.valueOf(mLongi));
                Log.e("hplace", String.valueOf(PlaceName));
            }
            else if(Nameval1.equals(Value1)){
                mLat = chatIntent.getStringExtra("lat").trim();
                mLongi = chatIntent.getStringExtra("lng").trim();
                mListPosition = chatIntent.getIntExtra("listposition",0);
                PlaceName= chatIntent.getStringExtra("placename").trim();

                Log.e("lattitude", String.valueOf(mLat));
                 Log.e("longitude", String.valueOf(mLongi));
                Log.e("plc", String.valueOf(PlaceName));
            }
            else{

            }


        }

        String[] field = {"SecurityRating","parkingTypes","carCategory"};
        String[] value = {"5 stars","Paid street parking","Small"};

        HashMap<String,ArrayList<String>> keyValue = new HashMap<>();
        ArrayList<String> fields = new ArrayList<>();
        fields.add("SecurityRating");
        fields.add("parkingTypes");
        fields.add("carCategory");

        ArrayList<String> parkingTypes = new ArrayList<>();
        if (Constants.PARKING_TYPES.size()>0){
            parkingTypes.addAll(Constants.PARKING_TYPES);
        }

        ArrayList<String> carCategory = new ArrayList<>();
        if (Constants.CAR_CATEGORY.size()>0){
            carCategory.addAll(Constants.CAR_CATEGORY);
        }

        ArrayList<String> sRatings = new ArrayList<>();
        if (Constants.SECURITY_RATINGS.size()>0){
            sRatings.addAll(Constants.SECURITY_RATINGS);
        }
            keyValue.put(field[0],sRatings);
            keyValue.put(field[1],parkingTypes);
            keyValue.put(field[2],carCategory);

        FirebaseFirestore db = FirebaseFirestore.getInstance();




//        abll
/*
        for (String key : field){

            for (String values : keyValue.get(key)){
                Query query = db.collection("camera").whereEqualTo(key,values);

                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Camera comment = document.toObject(Camera.class);
                            Log.d("Response", document.toString());
                        }

                    }
                });


            }

        }
*/
        if (parkingTypes.size() < 1 && carCategory.size() < 1 && sRatings.size() < 1){
            Query query = db.collection("camera");

            loadCameraLocation(query);
        }
        else{

            for (String key : field) {

            for (String values : keyValue.get(key)) {

                Query query = db.collection("camera").whereEqualTo(key, values);
                loadCameraLocation(query);

            }
            }
            }
        }


    private void loadCameraLocation(Query query){

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.getDocuments().size() < 1) {
                            return;
                        }

                        for (DocumentSnapshot document : documentSnapshots.getDocuments()) {

                            camera = document.toObject(Camera.class);
                            datalist.add(camera);

                            Log.e("dbbd", String.valueOf(document.getData()));
//

                            distancesmtrscurrentmap.clear();
                            distancescurrentarrmap.clear();

                            mCalculateDistances.clear();
                            mCameraLat.clear();
                            mCameraLong.clear();
                            mLocationDistances.clear();
                            mAccurateDistancesString.clear();
                            mCameraImageUrl.clear();
                            mCameraLocName.clear();
                            mCameraId.clear();
                            for (int i = 0; i < datalist.size(); i++) {
                                mCurrentLoc.setLatitude(Double.parseDouble(mLat));
                                mCurrentLoc.setLongitude(Double.parseDouble(mLongi));

                                mNearestLocations.setLatitude(Double.parseDouble(datalist.get(i).getCameraLat()));
                                mNearestLocations.setLongitude(Double.parseDouble(datalist.get(i).getCameraLong()));

                                double locationDistance = mCurrentLoc.distanceTo(mNearestLocations);
                                mLocationDistances.add(locationDistance);
                                Log.e("distancemtrsmap", String.valueOf(mLocationDistances));

                                //Calculate distances by 1000 to show to the users
                                mAccurateDistance = mCurrentLoc.distanceTo(mNearestLocations) / 1000;
                                mAccurateDistancesString.add(String.valueOf(mAccurateDistance));
                                Log.e("distancemap", String.valueOf(mAccurateDistancesString));

//                                if (locationDistance < 15000) {
                                    mCalculateDistances.add(String.valueOf(mLocationDistances));
                                    Log.e("mCalculateDistances", String.valueOf(mCalculateDistances));
                                    mCameraLat.add(datalist.get(i).getCameraLat());
                                    mCameraLong.add(datalist.get(i).getCameraLong());
                                    mCameraImageUrl.add(datalist.get(i).getCameraImageUrl());
                                    mCameraLocName.add(datalist.get(i).getCameraLocationName());
                                    mCameraId.add(datalist.get(i).getCameraID());

                                    Log.e("nearlatmap", String.valueOf(mCameraLat));
                                    Log.e("nearlongmap", String.valueOf(mCameraLong));
                                    Log.e("nearimgmap", String.valueOf(mCameraImageUrl));
                                    Log.e("rulsmap", String.valueOf(mCameraId));

////                            //Carousel View
//                                    final CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
//                                    carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
//                                    carouselLayoutManager.setMaxVisibleItems(3);
//
//
//                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//                                    mNearestPlaceRecycler.setLayoutManager(carouselLayoutManager);
//                                    mNearestPlaceRecycler.setHasFixedSize(true);
//
//                                    mNearestrecyclerAdapter = new CarouselDetailMapAdapter(context,mCameraImageUrl,mCameraLat,mCameraLong,mAccurateDistancesString,mCameraLocName,NearestLocMapsActivity.this);
//                                    mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
//                                    mNearestPlaceRecycler.addOnScrollListener(new CenterScrollListener());

                                    mNearestPlaceRecycler.setOrientation(DSVOrientation.HORIZONTAL);
                                    mNearestPlaceRecycler.addOnItemChangedListener(NearestLocMapsActivity.this);
                                    mNearestrecyclerAdapter = new CarouselDetailMapAdapter(context, mCameraImageUrl, mCameraLat, mCameraLong, mAccurateDistancesString, mCameraLocName, mCameraId, NearestLocMapsActivity.this);
                                    mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
                                    mNearestPlaceRecycler.scrollToPosition(mListPosition);
                                    mNearestPlaceRecycler.setItemTransformer(new ScaleTransformer.Builder()
                                            .setMinScale(0.8f)
                                            .build());
//                                            parkytype = datalist.get(i).getParkingType();



                                    onItemChanged(mCameraLat.get(0), mCameraLong.get(0), mCameraId.get(0),datalist.get(i).getParkingTypes());

                if((!datalist.equals(null))||(!datalist.isEmpty())){

                    if (datalist.get(i).getParkingTypes().equals("Free street parking")) {
                        LatLng sydney = new LatLng(Double.parseDouble(datalist.get(i).getCameraLat()), Double.parseDouble(datalist.get(i).getCameraLong()));
                        Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                        Mmap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        Mmap.animateCamera(CameraUpdateFactory.zoomTo(14),2000,null);
                    } else {
                        LatLng sydney = new LatLng(Double.parseDouble(datalist.get(i).getCameraLat()), Double.parseDouble(datalist.get(i).getCameraLong()));
                        Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.paid));
                        Mmap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        Mmap.animateCamera(CameraUpdateFactory.zoomTo(14),2000,null);
                    }

                }

hideProgressDialog();
//                                }
//

                            }
                        }

                    }
                });

    }

    @Override
    public void onClickimageButton(final int position, final String actionLikeButtonClicked, final String s, final String s1, String mapvalues, String s2, String s3) {


//        Toast.makeText(NearestLocMapsActivity.this, "click", Toast.LENGTH_SHORT).show();
//        mLat= String.valueOf(s);
//        mLongi= String.valueOf(s1);
//        valuestr=mapvalues;
//        PlaceNameval=s2;
//        Mmap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLongi))));
//        Log.e("maplattitude", String.valueOf(mLat));
//                Log.e("maplongitude", String.valueOf(mLongi));
//        Log.e("valuemap",valuelat+valuelongi+valuestr);

    }

    private void onItemChanged(String lat, String lng, String cameraId, String parkingType) {
        mapLat = lat.trim();
        mapLongi = lng.trim();
        cameraid = cameraId;
        Log.e("mapLongi",mapLat);
        Log.e("mapLat",mapLongi);
        Log.e("cameraid",cameraid);

        LatLng sydney = new LatLng(Double.parseDouble(mapLat), Double.parseDouble(mapLongi));

//            LatLng sydney = new LatLng(Double.parseDouble(mapLat), Double.parseDouble(mapLongi));
             if (parkingType.equals("Free street parking")){
              Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            }else{
            Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.paid));
              }
//            Mmap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
             Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,14 ));
//            Mmap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            Mmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker m) {
                    showDialog(m,cameraid,datalist.get(mNearestPlaceRecycler.getCurrentItem()).getParkingRules(),datalist.get(mNearestPlaceRecycler.getCurrentItem()).getCameraImageUrl());
                    mNearestPlaceRecycler.setVisibility(View.INVISIBLE);

                    return false;

                }

            });
            mNearestPlaceRecycler.setVisibility(View.VISIBLE);
        }

    private void pulseMarker(final Bitmap markerIcon, final Marker marker, final long onePulseDuration) {
        final Handler handler = new Handler();
        final long startTime = System.currentTimeMillis();

        final Interpolator interpolator = new CycleInterpolator(1f);
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / onePulseDuration);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(markerIcon));
                handler.postDelayed(this, 16);
            }
        });
    }

    public Bitmap scaleBitmap(Bitmap bitmap, float scaleFactor) {
        final int sizeX = Math.round(bitmap.getWidth() * scaleFactor);
        final int sizeY = Math.round(bitmap.getHeight() * scaleFactor);
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, sizeX, sizeY, false);
        return bitmapResized;
    }
    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = adapterPosition;
        onItemChanged(mCameraLat.get(positionInDataSet), mCameraLong.get(positionInDataSet), mCameraId.get(positionInDataSet),datalist.get(positionInDataSet).getParkingTypes());


    }



    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        mNearestPlaceRecycler.setVisibility(View.VISIBLE);
        LatLng latLng = new LatLng(Double.parseDouble(mapLat), Double.parseDouble(mLongi));
        Mmap.clear();

//            Mmap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,15.5f));
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
        Mmap.setMaxZoomPreference(20.5f);
        Mmap.setMinZoomPreference(6.5f);
        Mmap.getUiSettings().setMyLocationButtonEnabled(false);

    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        Mmap = gMap;
        Mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mNearestPlaceRecycler.setVisibility(View.VISIBLE);
        // Load custom marker icon

        try {
            Mmap.setMyLocationEnabled(false);
        } catch (SecurityException se) {

        }

        //Edit the following as per you needs
        Mmap.getUiSettings().setZoomControlsEnabled(true);
        Mmap.getUiSettings().setRotateGesturesEnabled(false);
//        Mmap.getUiSettings().setMyLocationButtonEnabled(false);
        //

//        LatLng placeLocation = new LatLng(Double.parseDouble(mapLat.trim()), Double.parseDouble(mLongi.trim())); //Make them global
//        Marker placeMarker = Mmap.addMarker(new MarkerOptions().position(placeLocation));
//        Mmap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
//        Mmap.animateCamera(CameraUpdateFactory.zoomTo(15), 10, null);

        mNearestPlaceRecycler.setVisibility(View.VISIBLE);

    }

    public void showDialog(Marker m, String cameraid, HashMap<String,Object> listofparkingRules, String cameraImageUrl){
        LayoutInflater layoutInflater = LayoutInflater.from(NearestLocMapsActivity.this);
        View promptView = layoutInflater.inflate(R.layout.ruls_layout , null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();

            TextView ViewTxt,NavigateTxt,rule1,rule2,rule3,rule4;

        ViewTxt=promptView.findViewById(R.id.view_txt);
        NavigateTxt=promptView.findViewById(R.id.navi_txt);

        rule1 = promptView.findViewById(R.id.rule1_txt);
        rule2 = promptView.findViewById(R.id.rule2_txt);
        rule3 =promptView.findViewById(R.id.rule3_txt);
        rule4 =promptView.findViewById(R.id.rule4_txt);

        if((!listofparkingRules.equals(null)) || (!listofparkingRules.isEmpty())){
            rule1.setText((CharSequence) listofparkingRules.get("0"));
            rule2.setText((CharSequence) listofparkingRules.get("1"));
            rule3.setText((CharSequence) listofparkingRules.get("2"));
            rule4.setText((CharSequence) listofparkingRules.get("3"));
        }



        Geocoder geocoder;
////
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            yourAddresses= geocoder.getFromLocation(Double.parseDouble(String.valueOf(m.getPosition().latitude)),Double.parseDouble(String.valueOf(m.getPosition().longitude)) , 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (yourAddresses.size() > 0)
        {
            yourplace = yourAddresses.get(0).getAddressLine(0);
            String yourCity = yourAddresses.get(0).getAddressLine(1);
            String yourCountry = yourAddresses.get(0).getAddressLine(2);


        }

        ViewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(NearestLocMapsActivity.this, ViewImageActivity.class);
                intent.putExtra("latitude",m.getPosition().latitude);
                intent.putExtra("longitude",m.getPosition().longitude);
                intent.putExtra("place",yourplace);
                intent.putExtra("cameraid",cameraid);
                intent.putExtra("cameraImageUrl",cameraImageUrl);

                Log.e("lattitude", String.valueOf(m.getPosition().latitude));
                Log.e("longitude", String.valueOf(m.getPosition().longitude));
                Log.e("yourplace",yourplace);
                Log.e("cameraid",cameraid);
                startActivity(intent);
                alertD.cancel();
                mNearestPlaceRecycler.setVisibility(View.VISIBLE);
            }
        });
        NavigateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showBottomSheet(m.getPosition().latitude,m.getPosition().longitude,yourplace);
                lat= String.valueOf(m.getPosition().latitude);
                longi= String.valueOf(m.getPosition().longitude);

//                SaveData(lat,longi,yourplace);
                alertD.cancel();
                mNearestPlaceRecycler.setVisibility(View.VISIBLE);
            }
        });
        alertD.setView(promptView);
        alertD.getWindow().setDimAmount(0.0f);
        alertD.getWindow().setGravity(Gravity.NO_GRAVITY);

        alertD.show();

        alertD.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mNearestPlaceRecycler.setVisibility(View.VISIBLE);
            }
        });

    }

    private void SaveData(String latitude, String longitude, String yourplace) {


        final String uid = PreferencesHelper.getPreference(NearestLocMapsActivity.this, PreferencesHelper.PREFERENCE_FIREBASE_UUID);


        parkingSpaceRating=0;
        protectCar=false;
        bookingStatus=true;
//          locationTxt=Location_Txt.getText().toString();
//        String photoURL = PreferencesHelper.getPreference(this, PreferencesHelper.PREFERENCE_PHOTOURL);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Map<String, Boolean> likeData = new HashMap<>();
        likeData.put(uid, false);
        documentID="";

        Booking bookingdata = new Booking(uid,latitude,longitude,yourplace,getPostTime(),bookingStatus,cameraid,documentID,parkingSpaceRating,protectCar);


        db.collection("Bookings").add(bookingdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                documentID = documentReference.getId();
//                PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_DOCMENTID, documentID);

                Booking bookingdata = new Booking(uid,latitude,longitude,yourplace,getPostTime(),bookingStatus,cameraid,documentID,parkingSpaceRating,protectCar);
                Map<String, Object> docID = new HashMap<>();
                docID.put("documentID", documentID);


                db.collection("Bookings").document(documentID).update(docID).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_DOCUMENTID,documentID);

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
//

    private void showBottomSheet(double latitude, double longitude, String yourPlace) {


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NearestLocMapsActivity.this);
        LayoutInflater factory = LayoutInflater.from(NearestLocMapsActivity.this);
        View bottomSheetView = factory.inflate(R.layout.ar_pyrky_bottomsheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        TextView map = bottomSheetView.findViewById(R.id.maps_title);
        TextView pyrky = bottomSheetView.findViewById(R.id.pyrky_title);
        TextView cancel = bottomSheetView.findViewById(R.id.cancel_txt);


        map.setOnClickListener(view -> {

            makeAlreadyBookedAlert(true,latitude,longitude,yourPlace);

            bottomSheetDialog.dismiss();

        });

        pyrky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    private void popup(String valuedoc,String key,Boolean bookingRequest,double latitude, double longitude, String yourPlace) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.status_alert_lay, null);
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        Button ok = deleteDialogView.findViewById(R.id.ok_button);
        Button cancel = deleteDialogView.findViewById(R.id.cancel_button);

        final android.support.v7.app.AlertDialog alertDialog1 = alertDialog.create();
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
                                isBookedAny = false;
                                if (bookingRequest){
                                    makeAlreadyBookedAlert(true,latitude,longitude,yourPlace);
                                }else{
                                    makeAlreadyBookedAlert(false,latitude,longitude,yourPlace);
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });


                Map<String, Object> likeupdate = new HashMap<>();
                likeupdate.put( "bookingStatus", false);

                db.collection("Bookings").document(docid)
                        .update(likeupdate)
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

//                PopUpprotectcar(documentID);

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

    private void PopUpprotectcar(String documentID) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.protetcar_alert, null);
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        Button ok = deleteDialogView.findViewById(R.id.ok_button);
        Button cancel = deleteDialogView.findViewById(R.id.cancel_button);
//        final MediaPlayer mp = MediaPlayer.create(this, R.raw.soho);

        final android.support.v7.app.AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//                mp.start();
                final Map<String, Object> protectdata = new HashMap<>();
                protectdata.put("protectCar", true);



                FirebaseFirestore db = FirebaseFirestore.getInstance();


                db.collection("Bookings").document(documentID)
                        .update(protectdata)
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

    private void makeAlreadyBookedAlert(Boolean bookingRequest,double latitude, double longitude, String yourPlace){
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
//
                                            if (val) {

                                                Toast.makeText(NearestLocMapsActivity.this, values, Toast.LENGTH_SHORT).show();
                                                String valuedoc=PreferencesHelper.getPreference(getApplicationContext(),PreferencesHelper.PREFERENCE_DOCUMENTID);

                                                popup(valuedoc,entry.getKey(),bookingRequest,latitude,longitude,yourPlace);
                                                break;

//                                Toast.makeText(getActivity(), followcount, Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(context, "False value", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }else{
                                        if (bookingRequest){
                                            bookAndNavigate(latitude,longitude,yourPlace);
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

    private void bookAndNavigate(double latitude, double longitude, String yourPlace){
//        showBottomSheet(latitude, longitude,yourPlace);
        SaveData(lat, longi, yourPlace);
        PackageManager pm = NearestLocMapsActivity.this.getPackageManager();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public boolean onMarkerClick(Marker marker) {


        return false;
    }

    public double getPostTime() {

        Date currentDate = new Date();
        long unixTime = currentDate.getTime() / 1000;
        return unixTime;


    }


    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage((FragmentActivity) context);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage((FragmentActivity) context);
        mGoogleApiClient.disconnect();
    }
    public void showProgressDialog() {


        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(NearestLocMapsActivity.this);
        //View view = getLayoutInflater().inflate(R.layout.progress);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void hideProgressDialog(){
        if(dialog!=null)
            dialog.dismiss();
    }


}
