package com.polsec.pyrky.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.ViewImage.ViewImageActivity;
import com.polsec.pyrky.activity.ar.ArNavActivity;
import com.polsec.pyrky.adapter.CarouselDetailMapAdapter;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.pojo.Camera;
import com.polsec.pyrky.pojo.NearestDestnetionData;
import com.polsec.pyrky.pojo.SlotsData;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.Constants;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class NearestLocMapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, DiscreteScrollView.OnItemChangedListener {

    private static final long DEFAULT_MARKER_LENGTH = 2000;
    private CarouselDetailMapAdapter mNearestrecyclerAdapter;
    private DiscreteScrollView mNearestPlaceRecycler;
    private int mListPosition = 0;
    private Location mCurrentLoc = new Location("");
    private Location mNearestLocations = new Location("");
    private ArrayList<String> mAccurateDistancesString = new ArrayList<>();
    private ArrayList<String> mCameraId = new ArrayList<>();
    private double mAccurateDistance;
    private GoogleMap Mmap;
    private List<Address> yourAddresses;
    private String yourplace;
    private String documentID;
    private List<Camera> datalist = new ArrayList<Camera>();
    private String mLat, mLongi, PlaceName, Imageurl;
    private int distance;
    private String cameraid;
    private String ParkingType;
    private String CarType;
    private Camera camera;
    private String mUid, CameraId;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Map<String, Object> bookingid = new HashMap<>();
    private Map<String, Object> slotsid = new HashMap<>();
    private Map<String, Object> slotsid1 = new HashMap<>();

    private Map<String, Object> bookingid1 = new HashMap<>();
    private android.support.v7.app.AlertDialog dialog;
    private Boolean isBookedAny = false;
    private String documentIDs;
    private double curLat, curLong;
    private HashMap<String, Object> mrlslist = new HashMap<>();
    private List<NearestDestnetionData> mNearestDataList = new ArrayList<>();
    private List<SlotsData> mNearestDataList1 = new ArrayList<SlotsData>();
    private List<SlotsData> mNearestDataAdapterList = new ArrayList<SlotsData>();
    private SparseIntArray mDeniedKeys;
    private HashMap<String, Object> compactArray = new HashMap<String, Object>();
    double latitudes, longitudesvall;
    private JSONObject itemobj;
    private JSONArray jsarray;
    private String contactsitem;
    private String locname, cameraid1, prkingtype, imgurl;
    private ArrayList<String> Slotlat1 = new ArrayList<>();
    private ArrayList<String> Slotlong = new ArrayList<>();
    private int listCurrentPosition = 5, listPrevPosition = 0;
    // Group permission request code
    private static final int REQUEST_GROUP_PERMISSIONS = 104;

    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_loc_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDeniedKeys = new SparseIntArray();
        mNearestPlaceRecycler = findViewById(R.id.nearest_places_recycler);
        mNearestPlaceRecycler.setOrientation(DSVOrientation.HORIZONTAL);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUid = PreferencesHelper.getPreference(this, PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        RelativeLayout mBackIcon = findViewById(R.id.back_icon);
        TextView titlaTxt = findViewById(R.id.extra_title);
        titlaTxt.setText("Map");
        mBackIcon.setOnClickListener(view1 -> {
            this.onBackPressed();
        });

        String[] field = {"SecurityRating", "parkingTypes", "carCategory"};

        HashMap<String, ArrayList<String>> keyValue = new HashMap<>();
        ArrayList<String> fields = new ArrayList<>();
        fields.add("SecurityRating");
        fields.add("parkingTypes");
        fields.add("carCategory");

        ArrayList<String> parkingTypes = new ArrayList<>();
        if (Constants.PARKING_TYPES.size() > 0) {
            parkingTypes.addAll(Constants.PARKING_TYPES);
        }

        ArrayList<String> carCategory = new ArrayList<>();
        if (Constants.CAR_CATEGORY.size() > 0) {
            carCategory.addAll(Constants.CAR_CATEGORY);
        }

        ArrayList<String> sRatings = new ArrayList<>();
        if (Constants.SECURITY_RATINGS.size() > 0) {
            sRatings.addAll(Constants.SECURITY_RATINGS);
        }
        keyValue.put(field[0], sRatings);
        keyValue.put(field[1], parkingTypes);
        keyValue.put(field[2], carCategory);

        recyclerViewScrollListener();
        Bundle bundle = getIntent().getBundleExtra("mapBundle");
        if (bundle != null) {

            String nameval = "home";
            String nameval1 = "carousel";
            if (nameval.equals(bundle.getString("value"))) {

                showProgressDialog();
                mLat = Objects.requireNonNull(bundle.getString("latitude")).trim();
                mLongi = Objects.requireNonNull(bundle.getString("longitude")).trim();
                PlaceName = Objects.requireNonNull(bundle.getString("place")).trim();
                ParkingType = Objects.requireNonNull(bundle.getString("parkingtype")).trim();
                CarType = bundle.getString("cartypes");
                mNearestPlaceRecycler.setVisibility(View.VISIBLE);

                getCurrentLocation(mLat, mLongi);

                if (parkingTypes.size() < 1 && carCategory.size() < 1 && sRatings.size() < 1) {
                    datalist.clear();
                    mNearestDataList.clear();
                    loadCameraLocation();
                } else {

                    for (String key : field) {

                        for (String values : keyValue.get(key)) {
                            datalist.clear();
                            mNearestDataList.clear();
                            loadCameraLocation();

                        }
                    }
                }
            } else if (nameval1.equals(bundle.getString("carousel"))) {


                mLat = Objects.requireNonNull(bundle.getString("latt")).trim();
                mLongi = Objects.requireNonNull(bundle.getString("longg")).trim();
                mListPosition = bundle.getInt("adapterPosition");
                PlaceName = Objects.requireNonNull(bundle.getString("place")).trim();
                distance = bundle.getInt("distance");
                Imageurl = bundle.getString("imgurl");
                CameraId = bundle.getString("cameraid");
                mrlslist = (HashMap<String, Object>) bundle.getSerializable("rulslist");
                ParkingType = bundle.getString("parkingtype");
                mNearestPlaceRecycler.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(() -> showDialog1(mrlslist, CameraId, mLat, mLongi, Imageurl, PlaceName), DEFAULT_MARKER_LENGTH);


                getCurrentLocation(mLat, mLongi);
            }

        }

    }


    private void loadCameraLocation() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query docRef = db.collection("camera").orderBy("cameraLat", Query.Direction.ASCENDING);
        docRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.getDocuments().size() < 1) {
                return;
            }

            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                camera = document.toObject(Camera.class);
                datalist.add(camera);
                mNearestDataList.clear();
                mAccurateDistancesString.clear();
                mCameraId.clear();
                mNearestDataList1.clear();
                mNearestDataList.clear();
                if (datalist != null || !datalist.isEmpty()) {
                    for (int i = 0; i < datalist.size(); i++) {
//
                        mCurrentLoc.setLatitude(Double.parseDouble(mLat));
                        mCurrentLoc.setLongitude(Double.parseDouble(mLongi));
                        mNearestLocations.setLatitude(Double.parseDouble(datalist.get(i).getCameraLat()));
                        mNearestLocations.setLongitude(Double.parseDouble(datalist.get(i).getCameraLong()));
                        double locationDistance = mCurrentLoc.distanceTo(mNearestLocations);
                        int str = (int) locationDistance;
                        mAccurateDistance = mCurrentLoc.distanceTo(mNearestLocations) / 1000;
                        mAccurateDistancesString.add(String.valueOf(mAccurateDistance));
                        if (str < 1500) {
                            NearestDestnetionData nearestdata = new NearestDestnetionData();
                            nearestdata.setLocationDistance(locationDistance);
                            nearestdata.setCameraLat(datalist.get(i).getCameraLat());
                            nearestdata.setCameraLong(datalist.get(i).getCameraLong());
                            nearestdata.setParkingTypes(datalist.get(i).getParkingTypes());
                            nearestdata.setCameraID(datalist.get(i).getCameraID());
                            nearestdata.setParkingRules(datalist.get(i).getParkingRules());
                            nearestdata.setCameraLocationName(datalist.get(i).getCameraLocationName());
                            nearestdata.setCameraImageUrl(datalist.get(i).getCameraImageUrl());
                            nearestdata.setParkingSlots(datalist.get(i).getParkingSlots());
                            mNearestDataList.add(nearestdata);

                            mCameraId.add(datalist.get(i).getCameraID());

                            for (int j = 0; j < mCameraId.size(); j++) {


                                db.collection("camera")
                                        .whereEqualTo("cameraID", mCameraId.get(j))
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document1 : task.getResult()) {

                                                    if (document1.exists()) {
                                                        if (document1.contains("parkingSlots")) {
                                                            Log.e("data", String.valueOf(document1.getData().get("parkingSlots")));

                                                            locname = String.valueOf(document1.getData().get("cameraLocationName"));
                                                            cameraid1 = String.valueOf(document1.getData().get("cameraID"));
                                                            prkingtype = String.valueOf(document1.getData().get("parkingTypes"));
                                                            imgurl = String.valueOf(document1.getData().get("cameraImageUrl"));

                                                            compactArray = (HashMap<String, Object>) document1.getData().get("parkingRules");

                                                            Log.e("locname", String.valueOf(locname));
                                                            slotsid = document1.getData();
                                                            Log.e("slotsid", String.valueOf(slotsid));

                                                            slotsid1 = (Map<String, Object>) slotsid.get("parkingSlots");
                                                            Log.e("slotsid1", String.valueOf(slotsid1));


                                                            itemobj = new JSONObject(slotsid1);


                                                            try {
                                                                contactsitem = itemobj.getString(CarType.toLowerCase());

                                                                Log.e("contactsitem", String.valueOf(contactsitem));

                                                                if (!contactsitem.equals(null) || !contactsitem.isEmpty()) {

                                                                    jsarray = new JSONArray(contactsitem);
                                                                    Log.e("jsarray", String.valueOf(jsarray));
                                                                    Slotlat1.clear();
                                                                    Slotlong.clear();


                                                                    for (int m = 0; m < jsarray.length(); m++) {
                                                                        JSONObject objrct = jsarray.getJSONObject(m);
                                                                        latitudes = Double.parseDouble(objrct.getString("latitude"));
                                                                        longitudesvall = Double.parseDouble(objrct.getString("longitude"));
                                                                        Log.e("latitudes", String.valueOf(latitudes));

                                                                        Log.e("longitudesvall", String.valueOf(longitudesvall));


                                                                        mNearestLocations.setLatitude(Double.parseDouble(String.valueOf(latitudes)));
                                                                        mNearestLocations.setLongitude(Double.parseDouble(String.valueOf(longitudesvall)));
                                                                        double locationDistance1 = mCurrentLoc.distanceTo(mNearestLocations);

                                                                        Slotlat1.add(String.valueOf(latitudes));
                                                                        Slotlong.add(String.valueOf(longitudesvall));

                                                                        mNearestDataList1.clear();


                                                                        for (int s = 0; s < Slotlat1.size(); s++) {

                                                                            SlotsData slotsData = new SlotsData();
                                                                            slotsData.setLocationDistance(locationDistance1);
                                                                            slotsData.setCameraLat(String.valueOf(latitudes));
                                                                            slotsData.setCameraLong(String.valueOf(longitudesvall));
                                                                            slotsData.setCameraID(cameraid1);
                                                                            slotsData.setCameraLocationName(locname);
                                                                            slotsData.setCameraImageUrl(imgurl);
                                                                            slotsData.setParkingTypes(prkingtype);
                                                                            slotsData.setParkingRules(compactArray);
                                                                            mNearestDataList1.add(slotsData);
                                                                        }
////
                                                                    }


                                                                }


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                            loadAdapter();

                                                        }

                                                    }

                                                }

                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        });
                            }


                        } else if (str >= 1500 && str < 3000) {

                            mCameraId.add(datalist.get(i).getCameraID());
                            Log.e("mCameraId", String.valueOf(mCameraId));

                            NearestDestnetionData nearestdata = new NearestDestnetionData();
                            nearestdata.setLocationDistance(locationDistance);
                            nearestdata.setCameraLat(datalist.get(i).getCameraLat());
                            nearestdata.setCameraLong(datalist.get(i).getCameraLong());
                            nearestdata.setParkingTypes(datalist.get(i).getParkingTypes());
                            nearestdata.setCameraID(datalist.get(i).getCameraID());
                            nearestdata.setParkingRules(datalist.get(i).getParkingRules());
                            nearestdata.setCameraLocationName(datalist.get(i).getCameraLocationName());
                            nearestdata.setCameraImageUrl(datalist.get(i).getCameraImageUrl());
                            nearestdata.setParkingSlots(datalist.get(i).getParkingSlots());
                            mNearestDataList.add(nearestdata);

                            mCameraId.add(datalist.get(i).getCameraID());

                            for (int j = 0; j < mCameraId.size(); j++) {
                                db.collection("camera")
                                        .whereEqualTo("cameraID", mCameraId.get(j))
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document12 : task.getResult()) {
                                                    if (document12.exists()) {
                                                        if (document12.contains("parkingSlots")) {
                                                            Log.e("data", String.valueOf(document12.getData().get("parkingSlots")));
                                                            slotsid = document12.getData();
                                                            Log.e("slotsid", String.valueOf(slotsid));

                                                            slotsid1 = (Map<String, Object>) slotsid.get("parkingSlots");
                                                            Log.e("slotsid1", String.valueOf(slotsid1));

                                                            locname = String.valueOf(document12.getData().get("cameraLocationName"));
                                                            cameraid1 = String.valueOf(document12.getData().get("cameraID"));
                                                            prkingtype = String.valueOf(document12.getData().get("parkingTypes"));
                                                            imgurl = String.valueOf(document12.getData().get("cameraImageUrl"));
                                                            compactArray = (HashMap<String, Object>) document12.getData().get("parkingRules");

                                                            itemobj = new JSONObject(slotsid1);


                                                            try {
                                                                contactsitem = itemobj.getString(CarType.toLowerCase());
                                                                Log.e("contactsitem", String.valueOf(contactsitem));

                                                                if (!contactsitem.equals(null) || !contactsitem.isEmpty()) {


                                                                    jsarray = new JSONArray(contactsitem);
                                                                    Log.e("jsarray", String.valueOf(jsarray));
                                                                    Slotlat1.clear();
                                                                    Slotlong.clear();

                                                                    for (int m = 0; m < jsarray.length(); m++) {
                                                                        JSONObject objrct = jsarray.getJSONObject(m);
                                                                        latitudes = Double.parseDouble(objrct.getString("latitude"));
                                                                        longitudesvall = Double.parseDouble(objrct.getString("longitude"));
                                                                        Log.e("latitudes", String.valueOf(latitudes));

                                                                        Log.e("longitudesvall", String.valueOf(longitudesvall));


                                                                        mNearestLocations.setLatitude(Double.parseDouble(String.valueOf(latitudes)));
                                                                        mNearestLocations.setLongitude(Double.parseDouble(String.valueOf(longitudesvall)));
                                                                        double locationDistance1 = mCurrentLoc.distanceTo(mNearestLocations);

                                                                        Slotlat1.add(String.valueOf(latitudes));
                                                                        Slotlong.add(String.valueOf(longitudesvall));

                                                                        mNearestDataList1.clear();
                                                                        for (int s = 0; s < Slotlat1.size(); s++) {

                                                                            SlotsData slotsData = new SlotsData();
                                                                            slotsData.setLocationDistance(locationDistance1);
                                                                            slotsData.setCameraLat(String.valueOf(latitudes));
                                                                            slotsData.setCameraLong(String.valueOf(longitudesvall));
                                                                            slotsData.setCameraID(cameraid1);
                                                                            slotsData.setCameraLocationName(locname);
                                                                            slotsData.setCameraImageUrl(imgurl);
                                                                            slotsData.setParkingTypes(prkingtype);
                                                                            slotsData.setParkingRules(compactArray);
                                                                            mNearestDataList1.add(slotsData);

                                                                            Log.e("mNearestDataListlat", String.valueOf(mNearestDataList1.get(s).getCameraID()));

                                                                        }
                                                                    }

                                                                }


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }


                                                            loadAdapter();

                                                        }


                                                    }
                                                }

                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        });
                            }

                        }
//

                    }
                }


            }

        });

    }

    private void getCurrentLocation(String mLat, String mLongi) {

        Double lat = Double.valueOf(mLat);
        Double lng = Double.valueOf(mLongi);

        try {
            Geocoder geo = new Geocoder(this, Locale.getDefault());
            List<Address> mCurLocAddress = geo.getFromLocation(lat, lng, 1);
            if (mCurLocAddress.size() > 0) {
                curLat = mCurLocAddress.get(0).getLatitude();
                curLong = mCurLocAddress.get(0).getLongitude();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void onItemChanged(String lat, String lng, String cameraId, HashMap<String, Object> rules) {
        String mapLat = lat.trim();
        String mapLongi = lng.trim();
        cameraid = cameraId;
        Log.e("mapLongi", mapLat);
        Log.e("mapLat", mapLongi);
        Log.e("cameraid", String.valueOf(rules));
        Log.e("Cartype", String.valueOf(CarType));

        LatLng sydney = new LatLng(Double.parseDouble(mapLat), Double.parseDouble(mapLongi));

        Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));


        Mmap.setOnMarkerClickListener(marker -> {

            Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, -2));
            showDialog(marker, cameraid, rules, mNearestDataList.get(mNearestPlaceRecycler.getCurrentItem()).getCameraImageUrl());
            mNearestPlaceRecycler.setVisibility(View.INVISIBLE);
            return false;

        });

        mNearestPlaceRecycler.setVisibility(View.VISIBLE);


    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        onItemChanged(mNearestDataList1.get(adapterPosition).getCameraLat(), mNearestDataList1.get(adapterPosition).getCameraLong(),
                mNearestDataList1.get(adapterPosition).getCameraID(),
                mNearestDataList1.get(adapterPosition).getParkingRules());


    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLongi));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.5f));
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        Mmap = gMap;
        LatLng sydney = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLongi));

        switch (ParkingType) {
            case "Free street parking":
                Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_free_marker));
                Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
                break;
            case "Paid street parking":
                Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_paid_marker));
                Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
                break;
            case "Paid parking":
                Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_paid_marker));
                Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
                break;
        }

        Mmap.setOnMarkerClickListener(marker -> {
            if (mrlslist.size() == 0)
                mrlslist = (HashMap<String, Object>) marker.getTag();

            showDialog1(mrlslist, CameraId, mLat, mLongi, Imageurl, PlaceName);
            return false;

        });

        try {
            Mmap.setMyLocationEnabled(false);
        } catch (SecurityException se) {

        }

    }

    public void showDialog(Marker m, String cameraid, HashMap<String, Object> listofparkingRules, String cameraImageUrl) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.ruls_layout, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        TextView ViewTxt, NavigateTxt, rule1, rule2, rule3, rule4;

        ViewTxt = promptView.findViewById(R.id.view_txt);
        NavigateTxt = promptView.findViewById(R.id.navi_txt);

        rule1 = promptView.findViewById(R.id.rule1_txt);
        rule2 = promptView.findViewById(R.id.rule2_txt);
        rule3 = promptView.findViewById(R.id.rule3_txt);
        rule4 = promptView.findViewById(R.id.rule4_txt);

        if (listofparkingRules != null && listofparkingRules.size() > 0) {
            rule1.setText((CharSequence) listofparkingRules.get("0"));
            rule2.setText((CharSequence) listofparkingRules.get("1"));
            rule3.setText((CharSequence) listofparkingRules.get("2"));
            rule4.setText((CharSequence) listofparkingRules.get("3"));
        }


        Geocoder geocoder;
////
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            yourAddresses = geocoder.getFromLocation(Double.parseDouble(String.valueOf(m.getPosition().latitude)), Double.parseDouble(String.valueOf(m.getPosition().longitude)), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (yourAddresses.size() > 0) {
            yourplace = yourAddresses.get(0).getAddressLine(0);
        }

        ViewTxt.setOnClickListener(view -> {

            String maltval = String.valueOf(m.getPosition().latitude);
            String mlongival = String.valueOf(m.getPosition().longitude);

            Intent intent = new Intent(this, ViewImageActivity.class);
            intent.putExtra("latitude", maltval.trim());
            intent.putExtra("longitude", mlongival.trim());
            intent.putExtra("place", yourplace);
            intent.putExtra("cameraid", cameraid);
            intent.putExtra("cameraImageUrl", cameraImageUrl);
            intent.putExtra("recycler", "recyclervalue");

            alertD.dismiss();
            startActivity(intent);
            mNearestPlaceRecycler.setVisibility(View.VISIBLE);
        });
        NavigateTxt.setOnClickListener(view -> {
            alertD.dismiss();
            showBottomSheet(m.getPosition().latitude, m.getPosition().longitude, yourplace, cameraid);
            mNearestPlaceRecycler.setVisibility(View.VISIBLE);
        });
        alertD.setView(promptView);

        WindowManager.LayoutParams params = alertD.getWindow().getAttributes();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            params.y = (int) this.getResources().getDimension(R.dimen.size55);
        } else {
            params.y = (int) this.getResources().getDimension(R.dimen.size92);
        }
        alertD.getWindow().setAttributes(params);
        alertD.getWindow().setDimAmount(0.0f);
        alertD.getWindow().setGravity(Gravity.TOP);
        alertD.show();

//
        alertD.setOnCancelListener(dialogInterface -> mNearestPlaceRecycler.setVisibility(View.VISIBLE));

    }

    public void showDialog1(HashMap<String, Object> mrlslist, String cameraId, String mLat, String mLongi, String Imageurl1, String place) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.ruls_layout, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        TextView ViewTxt, NavigateTxt, rule1, rule2, rule3, rule4;

        ViewTxt = promptView.findViewById(R.id.view_txt);
        NavigateTxt = promptView.findViewById(R.id.navi_txt);

        rule1 = promptView.findViewById(R.id.rule1_txt);
        rule2 = promptView.findViewById(R.id.rule2_txt);
        rule3 = promptView.findViewById(R.id.rule3_txt);
        rule4 = promptView.findViewById(R.id.rule4_txt);

        if (mrlslist.size() > 0) {
            rule1.setText((CharSequence) mrlslist.get("0"));
            rule2.setText((CharSequence) mrlslist.get("1"));
            rule3.setText((CharSequence) mrlslist.get("2"));
            rule4.setText((CharSequence) mrlslist.get("3"));
        }


        ViewTxt.setOnClickListener(view -> {

            if (!Imageurl1.isEmpty()) {
                Intent intent = new Intent(this, ViewImageActivity.class);
                intent.putExtra("latitude", mLat);
                intent.putExtra("longitude", mLongi);
                intent.putExtra("cameraid", cameraId);
                intent.putExtra("cameraImageUrl", Imageurl1);
                intent.putExtra("place", place);
                startActivity(intent);
                alertD.dismiss();
            } else {
                Toast.makeText(this, "No Image Found", Toast.LENGTH_LONG).show();
            }
        });
        NavigateTxt.setOnClickListener(view -> {

            showBottomSheet(Double.parseDouble(mLat), Double.parseDouble(mLongi), place, cameraId);

            Log.e("latval", String.valueOf(Double.parseDouble(mLat)));
            Log.e("longival", String.valueOf(Double.parseDouble(mLongi)));
            alertD.dismiss();

        });
        alertD.setView(promptView);
        WindowManager.LayoutParams params = alertD.getWindow().getAttributes();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            params.y = (int) this.getResources().getDimension(R.dimen.size55);
        } else {
            params.y = (int) this.getResources().getDimension(R.dimen.size92);
        }

        alertD.getWindow().setAttributes(params);
        alertD.getWindow().setDimAmount(0.0f);
        alertD.getWindow().setGravity(Gravity.TOP);
        alertD.show();


    }

    private void showBottomSheet(double latitude, double longitude, String cameraid, String yourPlace) {


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater factory = LayoutInflater.from(this);
        View bottomSheetView = factory.inflate(R.layout.ar_pyrky_bottomsheet, null);
        TextView map = bottomSheetView.findViewById(R.id.maps_title);
        TextView pyrky = bottomSheetView.findViewById(R.id.pyrky_title);
        TextView cancel = bottomSheetView.findViewById(R.id.cancel_txt);

        if (Constants.IS_AR_ENABLED) {

        } else {
            pyrky.setVisibility(View.GONE);
        }
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheetView.getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) bottomSheetView.getParent()).setBackgroundColor(Color.TRANSPARENT);


        map.setOnClickListener(view -> {

            String value = "map";

            Boolean bookingRequest = true;

            if (bookingRequest) {
                makeAlreadyBookedAlert(true, latitude, longitude, yourPlace, cameraid, value);
            } else {

                SaveData(latitude, longitude, yourPlace, cameraid);
            }

            bottomSheetDialog.dismiss();

        });

        pyrky.setOnClickListener(view -> {

            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                    || (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    || (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                askRequestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        R.string.enable_permission, REQUEST_GROUP_PERMISSIONS);


                String value = "pyrky";

                Boolean bookingRequest = true;

                if (bookingRequest) {
                    makeAlreadyBookedAlert(true, latitude, longitude, yourPlace, cameraid, value);
                } else {

                    SaveData(latitude, longitude, yourPlace, cameraid);
                }
//


            } else {
                groupPermissionEnable();
            }


            bottomSheetDialog.dismiss();
        });
        cancel.setOnClickListener(view -> bottomSheetDialog.dismiss());
    }

    private void groupPermissionEnable() {
        //TODO Add your code
        Toast.makeText(this, "Group runtime permission enable successfully...", Toast.LENGTH_SHORT).show();


    }

    private void SaveData(double latitude, double longitude, String yourplace, String cameraid) {


        final String uid = PreferencesHelper.getPreference(this, PreferencesHelper.PREFERENCE_FIREBASE_UUID);


        double parkingSpaceRating = 0;
        Boolean protectCar = false;
        Boolean bookingStatus = true;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Map<String, Boolean> likeData = new HashMap<>();
        likeData.put(uid, false);
        documentID = "";

        Booking bookingdata = new Booking(uid, latitude, longitude, yourplace, getPostTime(), bookingStatus, cameraid, documentID, parkingSpaceRating, protectCar);


        db.collection("Bookings").add(bookingdata).addOnSuccessListener(documentReference -> {

            documentID = documentReference.getId();
            PreferencesHelper.setPreference(this, PreferencesHelper.PREFERENCE_DOCUMENTIDNEW, documentID);
            PreferencesHelper.setPreference(this, PreferencesHelper.PREFERENCE_DOCMENTID, documentID);
            Map<String, Object> docID = new HashMap<>();
            docID.put("documentID", documentID);
            db.collection("Bookings").document(documentID).update(docID).addOnSuccessListener(aVoid -> {

                Map<String, Boolean> likeData1 = new HashMap<>();
                likeData1.put(documentID, true);

                Map<String, Map<String, Boolean>> likeData2 = new HashMap<>();
                likeData2.put("Booking_ID", likeData1);

                FirebaseFirestore db1 = FirebaseFirestore.getInstance();


                db1.collection("users").document(uid)
                        .set(likeData2, SetOptions.merge())
                        .addOnSuccessListener(aVoid1 -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

            }).addOnFailureListener(e -> {

            });
//


        });
    }
//


    private void popup(String valuedoc, String key, Boolean bookingRequest, double latitude, double longitude, String cameraid, String yourPlace, String value) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.status_alert_lay, null);
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = deleteDialogView.findViewById(R.id.ok_button);
        TextView cancel = deleteDialogView.findViewById(R.id.cancel_button);

        final android.support.v7.app.AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(view -> {

            Map<String, Boolean> likeData1 = new HashMap<>();
            likeData1.put(key, false);

            Map<String, Map<String, Boolean>> likeData2 = new HashMap<>();
            likeData2.put("Booking_ID", likeData1);


            FirebaseFirestore db = FirebaseFirestore.getInstance();


            db.collection("users").document(mUid)
                    .set(likeData2, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        popUpProtectCar(latitude, longitude, value);
                        isBookedAny = false;
                        if (bookingRequest) {
                            makeAlreadyBookedAlert(true, latitude, longitude, yourPlace, cameraid, value);
                        } else {
                            makeAlreadyBookedAlert(false, latitude, longitude, yourPlace, cameraid, value);
                        }

                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

            final Map<String, Object> bookingstatusdata = new HashMap<>();
            bookingstatusdata.put("bookingStatus", false);

            db.collection("Bookings").document(valuedoc)
                    .update(bookingstatusdata)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

//
            alertDialog1.dismiss();
        });

        cancel.setOnClickListener(view -> alertDialog1.dismiss());


        alertDialog1.setCanceledOnTouchOutside(false);
        try {
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        alertDialog1.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());

        lp.gravity = Gravity.CENTER;

        alertDialog1.getWindow().setAttributes(lp);
    }

    private void popUpProtectCar(double latitude, double longitude, String value) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.protetcar_alert, null);
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = deleteDialogView.findViewById(R.id.ok_button);
        TextView cancel = deleteDialogView.findViewById(R.id.cancel_button);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.parking_alert);

        final android.support.v7.app.AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(view -> {


            if (value == "map") {

                bookAndNavigate(latitude, longitude);
            } else {

                Intent intent = new Intent(this, ArNavActivity.class);

                intent.putExtra("SRC", "Current Location");
                intent.putExtra("DEST", "Some Destination");
                intent.putExtra("SRCLATLNG", curLat + "," + curLong);
                intent.putExtra("DESTLATLNG", latitude + "," + longitude);
                this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                startActivity(intent);
            }

            mp.start();

            documentIDs = PreferencesHelper.getPreference(this, PreferencesHelper.PREFERENCE_DOCUMENTIDNEW);
            Log.e("doc", documentIDs);

            protectCar(true, true, documentIDs);
            alertDialog1.dismiss();
        });

        cancel.setOnClickListener(view -> {


            if (value == "map") {

                Toast.makeText(this, "map", Toast.LENGTH_SHORT).show();
                bookAndNavigate(latitude, longitude);
            } else {

                Toast.makeText(this, "pyrky", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ArNavActivity.class);
//                    try {
                intent.putExtra("SRC", "Current Location");
                intent.putExtra("DEST", "Some Destination");
                intent.putExtra("SRCLATLNG", curLat + "," + curLong);
                intent.putExtra("DESTLATLNG", latitude + "," + longitude);
                this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                startActivity(intent);
            }

            alertDialog1.dismiss();
        });


        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        alertDialog1.getWindow().setAttributes(lp);
        alertDialog1.show();
    }


    private void protectCar(Boolean protectCar, Boolean bookingStatus, String documentIDs) {
        try {

            Map<String, Object> protectdata = new HashMap<>();
            protectdata.put("protectCar", protectCar);
            protectdata.put("bookingStatus", bookingStatus);


            FirebaseFirestore db = FirebaseFirestore.getInstance();


            db.collection("Bookings").document(documentIDs)
                    .update(protectdata)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void makeAlreadyBookedAlert(Boolean bookingRequest, double latitude, double longitude, String cameraid, String yourPlace, String value) {
        final FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference docRef1 = db.collection("users").document(mUid);
                docRef1.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (document.contains("Booking_ID")) {
                                bookingid = document.getData();

                                bookingid1 = (Map<String, Object>) bookingid.get("Booking_ID");

                                for (Map.Entry<String, Object> bookingEntry : bookingid1.entrySet()) {
                                    Boolean value1 = (Boolean) bookingEntry.getValue();
                                    if (value1) {
                                        isBookedAny = true;
                                        break;
                                    }
                                }

                                if (isBookedAny) {
                                    for (Map.Entry<String, Object> entry : bookingid1.entrySet()) {
                                        System.out.println(entry.getKey() + " = " + entry.getValue());

                                        Boolean val = (Boolean) entry.getValue();
                                        if (val) {
                                            popup(entry.getKey(), entry.getKey(), bookingRequest, latitude, longitude, yourPlace, cameraid, value);
                                            break;
                                        }
                                    }
                                } else {

                                    if (bookingRequest) {
                                        SaveData(latitude, longitude, yourPlace, cameraid);
                                    }

                                }

                            } else {
                                SaveData(latitude, longitude, yourPlace, cameraid);

                            }

                        }
                    }
                });


            }

        }).addOnFailureListener(e -> {

            Log.w("Error", "Error adding document", e);
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        });

    }

    private void bookAndNavigate(double latitude, double longitude) {

        if (isPackageInstalled()) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + "&daddr=" + latitude + "," + longitude));
            startActivity(intent);
        } else {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.co.in/maps?saddr=" + "&daddr=" + latitude + "," + longitude));
            startActivity(intent);
        }

    }


    private boolean isPackageInstalled() {
        try {
            return this.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
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


    public void showProgressDialog() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void hideProgressDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public void askRequestPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mDeniedKeys.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            //TODO Check
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {

        } else {
            openAppSetting(requestCode);
        }
    }

    private void openAppSetting(int requestCode) {
        Snackbar.make(findViewById(android.R.id.content), mDeniedKeys.get(requestCode), Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).setAction("ENABLE", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        }).show();
    }

    void loadAdapter() {
        try {

            hideProgressDialog();

            if (listPrevPosition < 0)
                return;

            if (listCurrentPosition > mNearestDataList1.size()) {
                listCurrentPosition = mNearestDataList1.size();
            }

            List<SlotsData> tempMapList = mNearestDataList1.subList(listPrevPosition, listCurrentPosition);
            mNearestDataAdapterList.addAll(mNearestDataList1.subList(listPrevPosition, listCurrentPosition));
            if (mNearestrecyclerAdapter == null) {
                mNearestrecyclerAdapter = new CarouselDetailMapAdapter(this, mNearestDataAdapterList, distance);
                mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
                mNearestPlaceRecycler.setItemTransformer(new ScaleTransformer.Builder()
                        .setMinScale(0.8f)
                        .build());
            } else
                mNearestrecyclerAdapter.updateDate(mNearestDataAdapterList);

            mNearestrecyclerAdapter.notifyDataSetChanged();

            for (int s = 0; s < tempMapList.size(); s++) {


                if ((!tempMapList.get(s).getParkingTypes().isEmpty())) {

                    if (mNearestDataList1.get(s).getParkingTypes().equals("Free street parking")) {
                        LatLng sydney = new LatLng(Double.parseDouble(tempMapList.get(s).getCameraLat()), Double.parseDouble(tempMapList.get(s).getCameraLong()));
                        Marker marker = Mmap.addMarker(new MarkerOptions().position(sydney));
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_free_marker));
                        marker.setTag(tempMapList.get(s).getParkingRules());
                        Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));


                    } else {
                        LatLng sydney = new LatLng(Double.parseDouble(tempMapList.get(s).getCameraLat()), Double.parseDouble(tempMapList.get(s).getCameraLong()));
                        Marker marker = Mmap.addMarker(new MarkerOptions().position(sydney));
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_free_marker));
                        marker.setTag(tempMapList.get(s).getParkingRules());
                        Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void recyclerViewScrollListener() {


        try {

            mNearestPlaceRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = mNearestPlaceRecycler.getLayoutManager().getChildCount();
                        totalItemCount = mNearestPlaceRecycler.getLayoutManager().getItemCount();
                        pastVisiblesItems = ((LinearLayoutManager) mNearestPlaceRecycler.getLayoutManager()).findFirstVisibleItemPosition();

                        if (loading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                loading = false;
                                Log.v("...", "Last Item Wow !");
                                listPrevPosition = listCurrentPosition;
                                listCurrentPosition += 5;
                                loadAdapter();
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
