package com.polsec.pyrky.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.activity.NearestLocMapsActivity;
import com.polsec.pyrky.adapter.CarouselNearestAdapter;
import com.polsec.pyrky.adapter.PlaceArrayAdapter;
import com.polsec.pyrky.pojo.UserLocationData;
import com.polsec.pyrky.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    //Nearest Place recycler
    RecyclerView mNearestPlaceRecycler;
    CarouselNearestAdapter mNearestrecyclerAdapter;

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
    List<UserLocationData> mNearestLocationList = new ArrayList<UserLocationData>();

    TextView mSearchButton;
    RelativeLayout HomeRelativeLay;

    AutoCompleteTextView autoCompView;


    Location mCurrentLoc = new Location("");
    Location mNearestLocations = new Location("");

    String placeId, description;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    SharedPreferences sharedPreferences;
    private static final int MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_CALL_PHONE = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;

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
    ArrayList<Double> mLocationDistances = new ArrayList<>();
    ArrayList<String> caldis1 = new ArrayList<>();
    ArrayList<String> mCameraLat = new ArrayList<>();
    ArrayList<String> mCameraLong = new ArrayList<>();
    ArrayList<String> mCameraImageUrl = new ArrayList<>();
    ArrayList<Double> distancesmtrscurrent = new ArrayList<>();
    ArrayList<String> distancescurrentarr = new ArrayList<>();
    ArrayList<String> mCameraLocName = new ArrayList<>();
    List<Address> mCurLocAddress = null;
    double distanceval;
    RelativeLayout HomeRelLayout, mParentLayout, HomeFragrellay;
    LinearLayout NearLinLay;
    Marker marker;
    MarkerOptions makeroptions;
    CarouselLayoutManager carouselLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:

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
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        autoCompView.setAdapter(mPlaceArrayAdapter);
        autoCompView.setThreshold(1);


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
                            Toast.makeText(getActivity(), address1, Toast.LENGTH_SHORT).show();
                            Log.e("address1", address1);


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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query first = db.collection("camera");
        first.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.getDocuments().size() < 1) {
                            return;
                        }

                        for (DocumentSnapshot document : documentSnapshots.getDocuments()) {

                            UserLocationData comment = document.toObject(UserLocationData.class);
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

                            for (int i = 0; i < mNearestLocationList.size(); i++) {
//
                                mCurrentLoc.setLatitude(mCurrentGpsLoc.getLatitude());
                                mCurrentLoc.setLongitude(mCurrentGpsLoc.getLongitude());

                                mNearestLocations.setLatitude(Double.parseDouble(mNearestLocationList.get(i).getCameraLat()));
                                mNearestLocations.setLongitude(Double.parseDouble(mNearestLocationList.get(i).getCameraLong()));

                                double locationDistance = mCurrentLoc.distanceTo(mNearestLocations);
                                mLocationDistances.add(locationDistance);

                                Log.e("distancemtrs1", String.valueOf(mLocationDistances));

                                distanceval = mCurrentLoc.distanceTo(mNearestLocations) / 1000;
                                distances1.add(String.valueOf(distanceval));
                                Log.e("distance", String.valueOf(distances1));

                                if (locationDistance < 15000) {
                                    caldis1.add(String.valueOf(mLocationDistances));
                                    Log.e("caldis1", String.valueOf(caldis1));
                                    mCameraLat.add(mNearestLocationList.get(i).getCameraLat());
                                    mCameraLong.add(mNearestLocationList.get(i).getCameraLong());
                                    mCameraImageUrl.add(mNearestLocationList.get(i).getCameraImageUrl());
                                    mCameraLocName.add(mNearestLocationList.get(i).getCameraLocationName());
                                    Log.e("mCameraLat", String.valueOf(mCameraLat));
                                    Log.e("mCameraLong", String.valueOf(mCameraLong));
                                    Log.e("mCameraImageUrl", String.valueOf(mCameraImageUrl));

                                    carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
                                    carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                    carouselLayoutManager.setMaxVisibleItems(1);

                                    mNearestPlaceRecycler.setLayoutManager(carouselLayoutManager);
                                    mNearestPlaceRecycler.setHasFixedSize(true);
                                    mNearestrecyclerAdapter = new CarouselNearestAdapter(getActivity(), mCameraImageUrl, mCameraLat, mCameraLong, distances1, mCameraLocName);
                                    mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
                                    mNearestPlaceRecycler.addOnScrollListener(new CenterScrollListener());
                                    mNearestrecyclerAdapter.notifyDataSetChanged();

                                    LatLng sydney = new LatLng(Double.parseDouble(mNearestLocationList.get(i).getCameraLat()), Double.parseDouble(mNearestLocationList.get(i).getCameraLong()));
                                    mMap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                                }

                            }
                        }
                        Double lat = mCurrentGpsLoc.getLatitude();
                        Double lng = mCurrentGpsLoc.getLongitude();
                        LatLng locateMe = new LatLng(lat, lng);


                        float zoomLevel = 14.0f;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locateMe,zoomLevel));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f), 2000, null);

                    }

                });
        if (mCameraLat!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mNearestPlaceRecycler.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                        int scrollPosition = carouselLayoutManager.getCenterItemPosition();
                        double lat = Double.parseDouble(mCameraLat.get(scrollPosition));
                        double lng = Double.parseDouble(mCameraLong.get(scrollPosition));
                        LatLng latLng = new LatLng(lat,lng);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                });
            }
        }


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (autoCompView.getText().toString().isEmpty() || description == null) {
                    Toast.makeText(getActivity(), "Please enter the search location", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getFirstWord(description), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), NearestLocMapsActivity.class);
                    intent.putExtra("placeid", placeId);
                    intent.putExtra("latitude", String.valueOf(Latitude).trim());
                    intent.putExtra("longitude", String.valueOf(Longitude).trim());
                    intent.putExtra("value", "home");
                    intent.putExtra("place", description);
                    Log.e("strLatitude", String.valueOf(Latitude));
                    Log.e("strLongitude", String.valueOf(Longitude));
                    intent.putStringArrayListExtra("placesarray", caldis);
                    getActivity().startActivity(intent);
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
                    Toast.makeText(getActivity(), "Filter Enabled", Toast.LENGTH_SHORT).show();
                    Fragment filterFragment = new FiltersFragment();
                    transaction = getChildFragmentManager().beginTransaction();
                    transaction.add(R.id.frame_layout, filterFragment).addToBackStack(null).commit();
                    Utils.hideKeyboard(getActivity());
                } else {
                    isExpandableListEnabled = false;
                    Toast.makeText(getActivity(), "Filter Disabled", Toast.LENGTH_SHORT).show();
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

    private void proceedAfterPermission() {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        //    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_markf)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln, 12.0f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f), 2000, null);
        mMap.setMaxZoomPreference(13.5f);
        mMap.setMinZoomPreference(6.5f);
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

//        mMap.getUiSettings().isZoomControlsEnabled();
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setMyLocationEnabled(false);
//
        mMap.addMarker(new MarkerOptions().position(locateme).icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocationicon)));
        float zoomLevel = 12.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locateme,zoomLevel));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f), 2000, null);
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

        //  mMap.addMarker(new MarkerOptions().position(laln).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,13.5f));

        // map.animateCamera(CameraUpdateFactory.zoomIn());
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(13.5f), 2000, null);
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
                                    }
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


    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
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

    public void animateMarker(final Marker marker, final Location location) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = marker.getRotation();
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * location.getBearing() + (1 - t)
                        * startRotation);

                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(rotation);

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private String getFirstWord(String text) {
        int index = text.indexOf(' ');
        if (index > -1) { // Check if there is more than one word.
            return text.substring(0, index); // Extract first word.
        } else {
            return text; // Text is the first word itself.
        }
    }

}
