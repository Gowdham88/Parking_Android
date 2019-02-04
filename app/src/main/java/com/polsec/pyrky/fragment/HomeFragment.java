package com.polsec.pyrky.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.polsec.pyrky.BuildConfig;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.activity.NearestLocMapsActivity;
import com.polsec.pyrky.adapter.CarouselNearestAdapter;
import com.polsec.pyrky.adapter.PlaceArrayAdapter;
import com.polsec.pyrky.pojo.Camera;
import com.polsec.pyrky.pojo.NearestData;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.Constants;
import com.polsec.pyrky.utils.Utils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, CarouselLayoutManager.OnCenterItemSelectionListener {

    //Nearest Place recycler
    private RecyclerView mNearestPlaceRecycler;
    private Boolean isCarouselSwiped = false, isExpandableListEnabled = false;
    private GoogleMap mMap;
    private Location currentLocation;
    private List<Camera> mNearestLocationList = new ArrayList<Camera>();
    private AutoCompleteTextView autoCompView;
    private Location mCurrentLoc = new Location("");
    private Location mNearestLocations = new Location("");
    private String description;
    private AlertDialog dialog;
    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private double Latitude, Longitude;
    private String stringcartypeval;
    private List<DocumentSnapshot> documentSnapshotList = new ArrayList<>();
    private ArrayList<Double> distancesmtrs = new ArrayList<>();
    private ArrayList<String> caldis = new ArrayList<>();
    private List<NearestData> mNearestDataList = new ArrayList<>();

    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationRequest mLocationRequest;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private LocationCallback mLocationCallback = null;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .build();


        if (Constants.currentLocation == null || Constants.mNearestDataList.size() == 0) {

            if (checkPermissions()) {
                startLocationUpdates();
            } else if (!checkPermissions()) {
                requestPermissions();
            }
        } else {
            if (HomeActivity.IS_LOCATION_LOADING_REQUIRED) {
                showProgressDialog();
                HomeActivity.IS_LOCATION_LOADING_REQUIRED = false;
            }
            currentLocation = Constants.currentLocation;
            mNearestDataList = Constants.mNearestDataList;
            loadCameraLocations();
        }
    }


    @SuppressLint("CutPasteId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        Utils.hideKeyboard(getActivity());

        TextView mSearchButton = view.findViewById(R.id.search_btn);
        mNearestPlaceRecycler = view.findViewById(R.id.nearest_places_recycler);

        RelativeLayout homeRelLayout = view.findViewById(R.id.home_lay);
        RelativeLayout mParentLayout = view.findViewById(R.id.parent_layout);
        RelativeLayout nearLinLay = view.findViewById(R.id.nearest_locations_layout);
        RelativeLayout homeFragrellay = view.findViewById(R.id.parfrag_lay);
        autoCompView = view.findViewById(R.id.autoCompleteTextView);
        int cartype = Integer.parseInt(PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_PROFILE_CAR));

        Log.e("cartype", String.valueOf(cartype));
        if (cartype == 0) {
            stringcartypeval = "Compact";
        } else if (cartype == 1) {
            stringcartypeval = "Small";
        } else if (cartype == 2) {
            stringcartypeval = "Mid";
        } else if (cartype == 3) {
            stringcartypeval = "Full";
        } else {
            stringcartypeval = "Van";
        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        //Auto Complete textview
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), R.layout.drop_downlay,
                BOUNDS_MOUNTAIN_VIEW, null);
        autoCompView.setDropDownVerticalOffset(7);
        autoCompView.setAdapter(mPlaceArrayAdapter);
        autoCompView.setThreshold(1);
        homeRelLayout.setOnClickListener(view15 -> {
            isExpandableListEnabled = false;
            Utils.hideKeyboard(getActivity());
        });

        mParentLayout.setOnClickListener(view12 -> {
            isExpandableListEnabled = false;
            Utils.hideKeyboard(getActivity());
        });

        nearLinLay.setOnClickListener(view13 -> Utils.hideKeyboard(getActivity()));

        homeFragrellay.setOnClickListener(view14 -> Utils.hideKeyboard(getActivity()));


//

      /*  if (mNearestDataList.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mNearestPlaceRecycler.setOnScrollChangeListener((view1, i, i1, i2, i3) -> {

                    int scrollPosition = carouselLayoutManager.getCenterItemPosition();
                    if (scrollPosition == 0) {
//
                        double lat = Double.parseDouble(mNearestDataList.get(scrollPosition).getCameraLat());
                        double lng = Double.parseDouble(mNearestDataList.get(scrollPosition).getCameraLong());
                        LatLng latLng = new LatLng(lat, lng);
                        if (isCarouselSwiped) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                        } else {
                            isCarouselSwiped = true;
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                        }
//
                    }
//


                });
            }
        }*/


        mSearchButton.setOnClickListener(v -> {

            if (autoCompView.getText().toString().isEmpty() || description == null) {

                String str = "Please enter the search location";
                athenticaationpopup(str);
            } else {

                Bundle args = new Bundle();
                args.putString("placeid", "");
                args.putString("latitude", String.valueOf(Latitude).trim());
                args.putString("longitude", String.valueOf(Longitude).trim());
                args.putString("value", "home");
                args.putString("parkingtype", "Free street");
                args.putString("place", description);
                args.putString("cartypes", stringcartypeval);
                args.putStringArrayList("placesarray", caldis);
                startActivity(new Intent(getActivity(), NearestLocMapsActivity.class).putExtra("mapBundle", args));

                autoCompView.setText("");
            }

        });


        Button mFilterButton = view.findViewById(R.id.filter_button);
        RelativeLayout mFilterRelativeLayout = view.findViewById(R.id.filter_lay);

        mFilterButton.setVisibility(View.VISIBLE);
        mFilterRelativeLayout.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction;

            @Override
            public void onClick(View view) {
                if (!isExpandableListEnabled) {
                    isExpandableListEnabled = true;
                    Fragment filterFragment = new FiltersFragment();
                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.add(R.id.frame_layout, filterFragment).addToBackStack(null).commit();
                    Utils.hideKeyboard(getActivity());
                } else {
                    isExpandableListEnabled = false;
                    getChildFragmentManager().popBackStack();
                }
            }
        });

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction;

            @Override
            public void onClick(View v) {
                if (!isExpandableListEnabled) {
                    isExpandableListEnabled = true;
                    Fragment filterFragment = new FiltersFragment();
                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.add(R.id.frame_layout, filterFragment).addToBackStack(null).commit();
                    Utils.hideKeyboard(getActivity());
                } else {
                    isExpandableListEnabled = false;
                    getChildFragmentManager().popBackStack();
                }


            }
        });

        //NearestPlace Recycler

        RelativeLayout homeRelativeLay = view.findViewById(R.id.home_lay);
        homeRelativeLay.setOnClickListener(view16 -> Utils.hideKeyboard(getActivity()));
        mNearestPlaceRecycler.setOnClickListener(view17 -> Utils.hideKeyboard(getActivity()));
        return view;
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.d("Loc---", "Location callback");
                currentLocation = locationResult.getLastLocation();
                if (currentLocation != null) {
                    mNearestDataList.clear();
                    loadCameraLocations();
                } else {
                    startUpdatesButtonHandler();
                }
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startUpdatesButtonHandler();
            }
        }, 2000);


    }

    public void startUpdatesButtonHandler() {

        startLocationUpdates();
    }

    public void stopUpdatesButtonHandler() {
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {

        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {


        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");

                    if (checkPermissions() && HomeActivity.IS_LOCATION_LOADING_REQUIRED) {
                        showProgressDialog();
                        HomeActivity.IS_LOCATION_LOADING_REQUIRED = false;
                    }
                    //noinspection MissingPermission

                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());


                })
                .addOnFailureListener(getActivity(), e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);
                            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    }


                });
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        if (currentLocation != null) {
            LatLng locatedMe = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(locatedMe)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car));
            float zoomLevel = 12;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locatedMe, zoomLevel));
        }
    }

    private void loadCameraLocations() {

        SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query docRef = db.collection("camera");
        docRef.get().addOnSuccessListener(queryDocumentSnapshots -> {

            documentSnapshotList.addAll(queryDocumentSnapshots.getDocuments());
//
            if (queryDocumentSnapshots.getDocuments().size() < 1) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_SHORT).show();
            } else {
                loadAdapter();
            }

        });


    }


    private void loadAdapter() {
        try {


            if (Constants.currentLocation == null || Constants.mNearestDataList.size() == 0) {

                mNearestLocationList.clear();

                for (DocumentSnapshot document : documentSnapshotList) {

                    Camera comment = document.toObject(Camera.class);
                    mNearestLocationList.add(comment);
                    Log.e("dbbd", String.valueOf(document.getData()));


                    for (int i = 0; i < mNearestLocationList.size(); i++) {

//
                        mCurrentLoc.setLatitude(currentLocation.getLatitude());
                        mCurrentLoc.setLongitude(currentLocation.getLongitude());

                        mNearestLocations.setLatitude(Double.parseDouble(mNearestLocationList.get(i).getCameraLat()));
                        mNearestLocations.setLongitude(Double.parseDouble(mNearestLocationList.get(i).getCameraLong()));

                        double locationDistance = mCurrentLoc.distanceTo(mNearestLocations);

                        if (locationDistance < 2500) {

                            NearestData nearestdata = new NearestData();
                            nearestdata.setLocationDistance(locationDistance);
                            nearestdata.setCameraLat(mNearestLocationList.get(i).getCameraLat());
                            nearestdata.setCameraLong(mNearestLocationList.get(i).getCameraLong());
                            nearestdata.setParkingTypes(mNearestLocationList.get(i).getParkingTypes());
                            nearestdata.setCameraID(mNearestLocationList.get(i).getCameraID());
                            nearestdata.setParkingRules(mNearestLocationList.get(i).getParkingRules());
                            nearestdata.setCameraLocationName(mNearestLocationList.get(i).getCameraLocationName());
                            nearestdata.setCameraImageUrl(mNearestLocationList.get(i).getCameraImageUrl());

                            mNearestDataList.add(nearestdata);
                            Collections.sort(mNearestDataList, (lhs, rhs) -> lhs.getLocationDistance().compareTo(rhs.getLocationDistance()));

                        }
                    }
                }

                Constants.currentLocation = currentLocation;
                Constants.mNearestDataList = mNearestDataList;
            } else {
                mNearestDataList = Constants.mNearestDataList;
                if (checkPermissions()) {
                    startLocationUpdates();
                } else if (!checkPermissions()) {
                    requestPermissions();
                }
            }

            CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
            carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
            carouselLayoutManager.setMaxVisibleItems(1);

            mNearestPlaceRecycler.setLayoutManager(carouselLayoutManager);
            mNearestPlaceRecycler.setHasFixedSize(true);
            CarouselNearestAdapter mNearestrecyclerAdapter = new CarouselNearestAdapter(getActivity(), mNearestDataList);
            mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
            mNearestrecyclerAdapter.notifyDataSetChanged();
            mNearestPlaceRecycler.addOnScrollListener(new CenterScrollListener());

            for (int i = 0; i < mNearestDataList.size(); i++) {

                if ((!mNearestDataList.get(i).getParkingTypes().equals(null)) || (!mNearestDataList.get(i).getParkingTypes().isEmpty())) {

                    if (mNearestDataList.get(i).getParkingTypes().equals("Free street parking")) {
                        LatLng sydney1 = new LatLng(Double.parseDouble(mNearestDataList.get(i).getCameraLat()), Double.parseDouble(mNearestDataList.get(i).getCameraLong()));
//
                        mMap.addMarker(new MarkerOptions().position(sydney1)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_free_marker));

                    } else {
                        LatLng sydney2 = new LatLng(Double.parseDouble(mNearestDataList.get(i).getCameraLat()), Double.parseDouble(mNearestDataList.get(i).getCameraLong()));
//
                        mMap.addMarker(new MarkerOptions().position(sydney2)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_paid_marker));
                    }

                }


            }
            hideProgressDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            Utils.hideKeyboard(getActivity());
            String placeId = String.valueOf(item.placeId);
            description = java.lang.String.valueOf(item.description);

            Log.e(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.e(TAG, "Fetching details for ID: " + item.placeId);

            Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess()) {
                                final Place myPlace = places.get(0);
                                LatLng queriedLocation = myPlace.getLatLng();
                                Latitude = queriedLocation.latitude;
                                Longitude = queriedLocation.longitude;
                                Log.e("Latitude is", "" + queriedLocation.latitude);
                                Log.e("Longitude is", "" + queriedLocation.longitude);


                                distancesmtrs.clear();
                                for (int i = 0; i < mNearestLocationList.size(); i++) {

                                    Log.e("Latitude", "" + mNearestLocationList.get(i).getCameraLat());
                                    Log.e("Longitude", "" + mNearestLocationList.get(i).getCameraLong());

                                    mCurrentLoc.setLatitude(Latitude);
                                    mCurrentLoc.setLongitude(Longitude);
                                    mNearestLocations.setLatitude(Double.parseDouble(mNearestLocationList.get(i).getCameraLat()));
                                    mNearestLocations.setLongitude(Double.parseDouble(mNearestLocationList.get(i).getCameraLong()));
                                    double distancemtrs = mCurrentLoc.distanceTo(mNearestLocations);
                                    distancesmtrs.add(distancemtrs);
                                    Log.e("distancemtrs", String.valueOf(distancesmtrs));
                                    if (distancemtrs < 1500) {
                                        caldis.add(String.valueOf(distancesmtrs));
                                        Log.e("caldis", String.valueOf(caldis));
                                    }
                                }

                            }
                            places.release();
                        }
                    });
        }
    };


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }

        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    public void showProgressDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        //View view = getLayoutInflater().inflate(R.layout.progress);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!dialog.isShowing())
            dialog.show();


    }

    public void hideProgressDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        stopUpdatesButtonHandler();
        super.onDestroy();
    }


    private void athenticaationpopup(String message) {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.authentication_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(deleteDialogView);
        TextView AthuntTxt = (TextView) deleteDialogView.findViewById(R.id.txt_authent);
        TextView ok = (TextView) deleteDialogView.findViewById(R.id.ok_txt);
        AthuntTxt.setText(message);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
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
        if (!alertDialog1.isShowing())
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


    @Override
    public void onCenterItemChanged(int adapterPosition) {
        // Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            permissionDialog(1);

        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                permissionDialog(2);
            }
        }
    }

    private void permissionDialog(int showType) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialog.setTitle("Request Location Permission");
        alertDialog.setMessage("Need Permission for location updates.");
        alertDialog.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
            if (showType == 1) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);
            } else {
                Intent intent = new Intent();
                intent.setAction(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",
                        BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        alertDialog.show();
    }

}
