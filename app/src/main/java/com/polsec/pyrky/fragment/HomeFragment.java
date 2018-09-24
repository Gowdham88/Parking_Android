package com.polsec.pyrky.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
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
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QuerySnapshot;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.activity.NearestLocMapsActivity;
import com.polsec.pyrky.adapter.CarouselNearestAdapter;
import com.polsec.pyrky.adapter.PlaceArrayAdapter;
import com.polsec.pyrky.pojo.Camera;
import com.polsec.pyrky.pojo.NearestData;
import com.polsec.pyrky.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, CarouselLayoutManager.OnCenterItemSelectionListener {

    //Nearest Place recycler
    RecyclerView mNearestPlaceRecycler;
    CarouselNearestAdapter mNearestrecyclerAdapter;
    Boolean isCarouselSwiped = false;
    //Filter
    Boolean isExpandableListEnabled = false;
    Button mFilterButton;

    //Google Map
    GoogleMap mMap;
    SupportMapFragment mMapFragment;
    private TrackGPS mCurrentGpsLoc;
    Location mLocation;
    LatLng laln;
    String address1;
    double mCurLocLat, mCurLocLong;
    double latitu, longitu;
    double latt, longi;
    List<Camera> mNearestLocationList = new ArrayList<Camera>();


    TextView mSearchButton;
    RelativeLayout HomeRelativeLay;

    AutoCompleteTextView autoCompView;


    Location mCurrentLoc = new Location("");
    Location mNearestLocations = new Location("");

    String placeId, description;
    BitmapDrawable bitmapdraw;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    SharedPreferences sharedPreferences;
    private static final int MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_CALL_PHONE = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;

    private android.support.v7.app.AlertDialog dialog;
    HomeFragment mcontext=HomeFragment.this;

//            @Override
//            public void onAttach(Context context) {
//                ((MyApplication)context.getApplicationContext()).getNetComponent().inject(this);
//                super.onAttach(context);
//            }

    private static final String TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    double Latitude, Longitude;
    String StrLatitude, StrLongitude;
    ArrayList<Double> distances = new ArrayList<>();
    ArrayList<Double> distancesmtrs = new ArrayList<>();
    ArrayList<String> caldis = new ArrayList<>();
    ArrayList<String> nearlat = new ArrayList<>();
    ArrayList<String> nearlong = new ArrayList<>();


    ArrayList<String> distances1 = new ArrayList<>();
    ArrayList<String> distancesarray = new ArrayList<>();
    ArrayList<Double> mLocationDistances = new ArrayList<>();
    ArrayList<Double> caldis1 = new ArrayList<>();
    ArrayList<String> mCameraLat = new ArrayList<>();
    ArrayList<String> mCameraLong = new ArrayList<>();
    ArrayList<String> mCameraImageUrl = new ArrayList<>();
    ArrayList<Double> distancesmtrscurrent = new ArrayList<>();
    ArrayList<String> distancescurrentarr = new ArrayList<>();
    ArrayList<String> mCameraLocName = new ArrayList<>();
    ArrayList<String> mparkingtypeist = new ArrayList<>();
    ArrayList<ArrayList<Double>> mNeawdislist = new ArrayList<ArrayList<Double>>();
    ArrayList<HashMap<String, Object>> Ruleslist = new ArrayList<HashMap<String, Object>>();
    List<NearestData> mNearestDataList = new ArrayList<NearestData>();

    ArrayList<String> mCameraID = new ArrayList<>();
 HashMap<String, Object> popupruls = new HashMap<String, Object>();
    List<Address> mCurLocAddress = null;
    double distanceval;
    HashMap<String,Object> listofparkingRules=new HashMap<>();
    RelativeLayout HomeRelLayout, mParentLayout, HomeFragrellay;
    LinearLayout NearLinLay;
    Marker marker;
    MarkerOptions makeroptions;
    CarouselLayoutManager carouselLayoutManager;
    HomeActivity parent=(HomeActivity) getActivity();
//    public static final String TAG = "ImmersiveModeFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
//        mNearestrecyclerAdapter.notifyDataSetChanged();



        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), 0, this)
                .addConnectionCallbacks(this)
                .build();

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
//                getCurrentLocation();
//                loadCameraLocations();

            }
        }

        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @SuppressLint("CutPasteId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        Utils.hideKeyboard(getActivity());
        showProgressDialog();

        mSearchButton = view.findViewById(R.id.search_btn);
        mNearestPlaceRecycler = view.findViewById(R.id.nearest_places_recycler);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        HomeRelLayout = (RelativeLayout) view.findViewById(R.id.home_lay);
        mParentLayout = (RelativeLayout) view.findViewById(R.id.parent_layout);
        NearLinLay = (LinearLayout) view.findViewById(R.id.nearest_locations_layout);
        HomeFragrellay = (RelativeLayout) view.findViewById(R.id.parfrag_lay);
        autoCompView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);

        //Auto Complete textview
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(),R.layout.drop_downlay,
                BOUNDS_MOUNTAIN_VIEW, null);
//        autoCompView.setDropDownWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        autoCompView.setDropDownWidth(850);

//        int offset = 64;
//
//        autoCompView.setDropDownHorizontalOffset(-1 * offset);
//        autoCompView.setDropDownWidth((int) (autoCompView.getWidth() + offset * 10.7));
//        autoCompView.setDropDownHorizontalOffset(10);
        autoCompView.setDropDownVerticalOffset(7);
        autoCompView.setAdapter(mPlaceArrayAdapter);
        autoCompView.setThreshold(1);
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


//        final View decorView = getActivity().getWindow().getDecorView();
//        decorView.setOnSystemUiVisibilityChangeListener(
//                new View.OnSystemUiVisibilityChangeListener() {
//                    @Override
//                    public void onSystemUiVisibilityChange(int i) {
//                        int height = decorView.getHeight();
//                        Log.i(TAG, "Current height: " + height);
//                    }
//                });
//
//        toggleHideyBar();
        HomeRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpandableListEnabled = false;
                Utils.hideKeyboard(getActivity());
            }
        });

        mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpandableListEnabled = false;
                Utils.hideKeyboard(getActivity());
            }
        });

        NearLinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());
            }
        });

        HomeFragrellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());
            }
        });

        getCurrentLocation();
        loadCameraLocations();
        mNearestLocationList.clear();
        mNearestDataList.clear();

//

        if (mNearestDataList!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mNearestPlaceRecycler.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//                        mNearestrecyclerAdapter.notifyDataSetChanged();

                        int scrollPosition = carouselLayoutManager.getCenterItemPosition();
                        double lat = Double.parseDouble(mNearestDataList.get(scrollPosition).getCameraLat());
                        double lng = Double.parseDouble(mNearestDataList.get(scrollPosition).getCameraLong());
                        LatLng latLng = new LatLng(lat,lng);
                        //                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        if (scrollPosition == 0){

                            if (isCarouselSwiped){
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                            }else{

                            }

//                            Toast.makeText(getActivity(), "Same count", Toast.LENGTH_SHORT).show();
                        }else{
                            isCarouselSwiped = true;
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                        }


                    }
                });
            }
        }



        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (autoCompView.getText().toString().isEmpty() || description == null) {

                    String str="Please enter the search location";
                    athenticaationpopup(str);



                }
                 else {
//                    Toast.makeText(getActivity(), getFirstWord(description), Toast.LENGTH_SHORT).show();

                    NearestLocMapsActivity newFragment = new NearestLocMapsActivity();
                    Bundle args = new Bundle();
                    args.putString("placeid", placeId);
                    args.putString("latitude", String.valueOf(Latitude).trim());
                    args.putString("longitude", String.valueOf(Longitude).trim());
                    args.putString("value", "home");
                    args.putString("parkingtype", "Free street");
                    args.putString("place", description);
//                    Log.e("strLatitude", String.valueOf(Latitude));
//                    Log.e("strLongitude", String.valueOf(Longitude));
                    args.putStringArrayList("placesarray", caldis);

                    newFragment.setArguments(args);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack so the user can navigate back
                    transaction.replace(R.id.main_frame_layout, newFragment);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();
//                    Intent intent = new Intent(getActivity(), NearestLocMapsActivity.class);
//
//                    getActivity().startActivity(intent);
                    autoCompView.setText("");
                }

            }
        });


        mFilterButton = (Button) view.findViewById(R.id.filter_button);

        mFilterButton.setVisibility(View.VISIBLE);

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction;

            @Override
            public void onClick(View v) {
                if (!isExpandableListEnabled) {
                    isExpandableListEnabled = true;




                    // clear FLAG_TRANSLUCENT_STATUS flag:
//                    getActivity().setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    Fragment filterFragment = new FiltersFragment();
                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.add(R.id.frame_layout, filterFragment).addToBackStack(null).commit();
                    Utils.hideKeyboard(getActivity());
                } else {
                    isExpandableListEnabled = false;
//                    Toast.makeText(getActivity(), "Filter Disabled", Toast.LENGTH_SHORT).show();
                    getChildFragmentManager().popBackStack();
                }


            }
        });

        //NearestPlace Recycler

        HomeRelativeLay = view.findViewById(R.id.home_lay);
        HomeRelativeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());
            }
        });
        mNearestPlaceRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());
            }
        });
//
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", MODE_PRIVATE);

        return view;
    }

    private void getCurrentLocation(){
        //Getting Current Location from independent class
        mCurrentGpsLoc = new TrackGPS(getActivity());
        try {
            if (mCurrentGpsLoc.canGetLocation()) {
                Double lat = mCurrentGpsLoc.getLatitude();
                Double lng = mCurrentGpsLoc.getLongitude();
                mCurLocLat = laln.latitude;
                mCurLocLong = laln.longitude;


                try {
                    Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                    mCurLocAddress = geo.getFromLocation(lat, lng, 1);
                    if (mCurLocAddress.isEmpty()) {
                    } else {
                        if (mCurLocAddress.size() > 0) {
                            String address = mCurLocAddress.get(0).getAddressLine(0);
                            latt = mCurLocAddress.get(0).getLatitude();
                            longi = mCurLocAddress.get(0).getLongitude();
                            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = mCurLocAddress.get(0).getLocality();
                            String state = mCurLocAddress.get(0).getAdminArea();
                            String country = mCurLocAddress.get(0).getCountryName();
                            String postalCode = mCurLocAddress.get(0).getPostalCode();
                            String knownName = mCurLocAddress.get(0).getFeatureName();
                            address1 = (address + "," + city + "," + state + "," + country + "," + postalCode);
//                            Toast.makeText(getActivity(), address1, Toast.LENGTH_SHORT).show();
                            Log.e("address1", address1);
                            LatLng sydney = new LatLng(latt, longi);
                            mMap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_free_marker));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,8));

                            Log.e("lattd", String.valueOf(latt));
                            Log.e("latgd", String.valueOf(longi));



                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                mCurrentGpsLoc.showSettingsAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadCameraLocations(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query docRef = db.collection("camera");
        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//
//
//
//
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Query first = db.collection("camera");
//        first.get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() < 1) {
                    return;
                }

                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                    Camera comment = document.toObject(Camera.class);
                    mNearestLocationList.add(comment);
                    Log.e("dbbd", String.valueOf(document.getData()));
//
//                            distancesmtrscurrent.clear();
//                            distancescurrentarr.clear();
                    caldis1.clear();
                    mCameraLat.clear();
                    mCameraLong.clear();
                    mLocationDistances.clear();
                    distances1.clear();
                    mCameraImageUrl.clear();
                    mCameraLocName.clear();
                    popupruls.clear();
                    mCameraID.clear();
                    listofparkingRules.clear();
                    Ruleslist.clear();
                    mparkingtypeist.clear();

                    distancesarray.clear();
                    mNearestDataList.clear();


                    for (int i = 0; i < mNearestLocationList.size(); i++) {

//
                        mCurrentLoc.setLatitude(mCurrentGpsLoc.getLatitude());
                        mCurrentLoc.setLongitude(mCurrentGpsLoc.getLongitude());

                        mNearestLocations.setLatitude(Double.parseDouble(mNearestLocationList.get(i).getCameraLat()));
                        mNearestLocations.setLongitude(Double.parseDouble(mNearestLocationList.get(i).getCameraLong()));

                        double locationDistance = mCurrentLoc.distanceTo(mNearestLocations);
                        mLocationDistances.add(locationDistance);
                        Collections.sort(mLocationDistances);

                        Log.e("distancemtrs1", String.valueOf(mLocationDistances));

                        distanceval = mCurrentLoc.distanceTo(mNearestLocations) / 1000;
                        distances1.add(String.valueOf(distanceval));
                        Log.e("distance", String.valueOf(distances1));


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

                            for (int j = 0; j < mNearestDataList.size(); j++) {

                                Collections.sort(mNearestDataList, new Comparator<NearestData>() {
                                    @Override
                                    public int compare(NearestData lhs, NearestData rhs) {
                                        return lhs.getLocationDistance().compareTo(rhs.getLocationDistance());
                                    }
                                });


                                Log.e("sortlist", String.valueOf(mNearestDataList.get(j).getLocationDistance()));

//


                                carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
                                carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                carouselLayoutManager.setMaxVisibleItems(1);

                                mNearestPlaceRecycler.setLayoutManager(carouselLayoutManager);
                                mNearestPlaceRecycler.setHasFixedSize(true);
//                                        mNearestrecyclerAdapter = new CarouselNearestAdapter(getActivity(), mCameraImageUrl, mCameraLat, mCameraLong, distances1, mCameraLocName, caldis1, mCameraID, Ruleslist, mparkingtypeist);
                                mNearestrecyclerAdapter = new CarouselNearestAdapter(getActivity(), mNearestDataList);
                                mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
                                mNearestrecyclerAdapter.notifyDataSetChanged();
                                mNearestPlaceRecycler.addOnScrollListener(new CenterScrollListener());
                            }


//


                                LatLng sydney = new LatLng(Double.parseDouble(mNearestLocationList.get(i).getCameraLat()), Double.parseDouble(mNearestLocationList.get(i).getCameraLong()));


                                if ((!mNearestLocationList.get(i).getParkingTypes().equals(null)) || (!mNearestLocationList.get(i).getParkingTypes().isEmpty())) {

                                    if (mNearestLocationList.get(i).getParkingTypes().equals("Free street parking")) {
                                        LatLng sydney1 = new LatLng(Double.parseDouble(mNearestLocationList.get(i).getCameraLat()), Double.parseDouble(mNearestLocationList.get(i).getCameraLong()));
//
                                        mMap.addMarker(new MarkerOptions().position(sydney1)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_free_marker));

                                    } else {
                                        LatLng sydney2 = new LatLng(Double.parseDouble(mNearestLocationList.get(i).getCameraLat()), Double.parseDouble(mNearestLocationList.get(i).getCameraLong()));
//
                                        mMap.addMarker(new MarkerOptions().position(sydney2)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_paid_marker));
                                    }

                                }


                            }
                        }
                    }
//
                    hideProgressDialog();

                }


                });


    }


    private void proceedAfterPermission() {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();

        // Helper method for smooth
        // animation
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

//
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap =googleMap;
        mMap.clear();
        Double lat = mCurrentGpsLoc.getLatitude();
        Double lng = mCurrentGpsLoc.getLongitude();
        LatLng locateme = new LatLng(lat, lng);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setMyLocationEnabled(false);

//        int height = 95;
//        int width = 95;
//
//        bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.currentlocationicon);
//
//        Bitmap b=bitmapdraw.getBitmap();
//        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//        mMap.addMarker(new MarkerOptions().position(locateme).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

        mMap.addMarker(new MarkerOptions().position(locateme)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car));
        float zoomLevel = 12;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locateme,zoomLevel));
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                if(mCurrentGpsLoc.canGetLocation()) {
                    Double lat = mCurrentGpsLoc.getLatitude();
                    Double lng = mCurrentGpsLoc.getLongitude();
                    LatLng locateme = new LatLng(lat, lng);
                    handlenewlocation(locateme);

                }
                else
                {
                    Toast.makeText(getActivity(),"SORRY WE COULDN`T TRACK YOUR LOCATION",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }

    public void handlenewlocation(final LatLng laln)
    {
        mMap.clear();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,13.5f));

        latitu=laln.latitude;
        longitu=laln.longitude;



    }

    public void markCurrentLocation(){


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
                                    double distance = 0;
                                    double distancemtrs = mCurrentLoc.distanceTo(mNearestLocations);
                                    distancesmtrs.add(distancemtrs);
                                    Log.e("distancemtrs", String.valueOf(distancesmtrs));
//                        for(int j =0;j<distancesmtrs.size();j++){

                                    if (distancemtrs < 1500) {
                                        caldis.add(String.valueOf(distancesmtrs));
                                        Log.e("caldis", String.valueOf(caldis));
                                        nearlat.add(mNearestLocationList.get(i).getCameraLat());
                                        nearlong.add(mNearestLocationList.get(i).getCameraLong());
                                        Log.e("nearlat", String.valueOf(nearlat));
                                        Log.e("nearlong", String.valueOf(nearlong));
//                            }
//                                    }
                                        distance = mCurrentLoc.distanceTo(mNearestLocations) / 1000;
                                        Log.e("distance", String.valueOf(distance));
                                        distances.add(distance);

//                                          distancedata();


//                                            caldis.add(distance);
//                                            Log.e("caldis", String.valueOf(caldis));
//                                            distance(Latitude,Longitude,Double.parseDouble(mNearestLocationList.get(i).getCameraLat()),Double.parseDouble(mNearestLocationList.get(i).getCameraLong()));


//
                                    }
                                }

                            }
                            places.release();
                        }
                    });
        }
    };



    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

//                    mNameView.setText(Html.fromHtml(place.getAddress() + ""));


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



    public boolean checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        getCurrentLocation();
                        loadCameraLocations();

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Need phone Permission");
                    builder.setMessage("This app needs phone permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getActivity(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private String getFirstWord(String text) {
        int index = text.indexOf(' ');
        if (index > -1) { // Check if there is more than one word.
            return text.substring(0, index); // Extract first word.
        } else {
            return text; // Text is the first word itself.
        }
    }

    public void showProgressDialog() {


        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
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

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mGoogleApiClient.stopAutoManage(getActivity());
//        mGoogleApiClient.disconnect();
//    }
    private void athenticaationpopup(String message) {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.authentication_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(deleteDialogView);
        TextView AthuntTxt=(TextView)deleteDialogView.findViewById(R.id.txt_authent);
        TextView ok = (TextView)deleteDialogView.findViewById(R.id.ok_txt);
        AthuntTxt.setText(message);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
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

    public void toggleHideyBar() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i(TAG, "Turning immersive mode mode off. ");
        } else {
            Log.i(TAG, "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getActivity().getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }

    /**
     * Listener that will be called on every change of center item.
     * This listener will be triggered on <b>every</b> layout operation if item was changed.
     * Do not do any expensive operations in this method since this will effect scroll experience.
     *
     * @param adapterPosition current layout center item
     */
    @Override
    public void onCenterItemChanged(int adapterPosition) {
        Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
    }
}
