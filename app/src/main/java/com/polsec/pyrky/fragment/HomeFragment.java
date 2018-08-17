package com.polsec.pyrky.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
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
import android.support.v7.widget.SearchView;
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
import android.widget.ExpandableListView;
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

import com.google.android.gms.maps.CameraUpdate;
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
import com.polsec.pyrky.adapter.ExpandableListAdapter;
import com.polsec.pyrky.adapter.PlaceArrayAdapter;
import com.polsec.pyrky.map.PlacesPOJO;
import com.polsec.pyrky.pojo.UserLocationData;
import com.polsec.pyrky.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    //Search View
    SearchView mSearchView;
    //Nearest Place recycler
    RecyclerView mNearestPlaceRecycler;
    CarouselNearestAdapter mNearestrecyclerAdapter;

    //Expandable List View
    ExpandableListView mExpandableListView;
    ExpandableListAdapter mExpandableListAdapter;
    List<String> mExpandableListTitle;
    HashMap<String, List<String>> mExpandableListDetail;
    Boolean isExpandableListEnabled = false;


    GoogleMap Mmap;
    SupportMapFragment mapFrag;
    private TrackGPS gps;
    double Strlat, Strlong;
    LatLng laln;
    String address1;
    Location mLocation;
    double latitu, longitu;
    List<UserLocationData> datalist = new ArrayList<UserLocationData>();


    TextView mSearchButton;
    RelativeLayout HomeRelativeLay;

    private ArrayList<String> permissions = new ArrayList<>();

    Button filterButton;
    List<PlacesPOJO.CustomA> results;
    AutoCompleteTextView autoCompView;

    double latt, longi;
    Location loc1 = new Location("");
    Location loc2 = new Location("");

    String placeId, description;


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
    ArrayList<Double> distancesmtrs1 = new ArrayList<>();
    ArrayList<String> caldis1 = new ArrayList<>();
    ArrayList<String> nearlat1 = new ArrayList<>();
    ArrayList<String> nearlong1 = new ArrayList<>();
    ArrayList<String> nearimg = new ArrayList<>();
    ArrayList<Double> distancesmtrscurrent = new ArrayList<>();
    ArrayList<String> distancescurrentarr = new ArrayList<>();
    ArrayList<String> Placename = new ArrayList<>();
    List<Address> addresses = null;
    double distanceval;
    RelativeLayout HomeRelLayout, HomeRelLayout1, HomeFragrellay;
    LinearLayout NearLinLay;
    Marker marker;
    MarkerOptions makeroptions;


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
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        HomeRelLayout = (RelativeLayout) view.findViewById(R.id.home_lay);
        HomeRelLayout1 = (RelativeLayout) view.findViewById(R.id.home_lay1);
        NearLinLay = (LinearLayout) view.findViewById(R.id.current_location_layout);
        HomeFragrellay = (RelativeLayout) view.findViewById(R.id.parfrag_lay);
        autoCompView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        autoCompView.setAdapter(mPlaceArrayAdapter);
        autoCompView.setThreshold(1);




        HomeRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpandableListEnabled = false;
                mExpandableListView.setVisibility(View.GONE);
                Utils.hideKeyboard(getActivity());
            }
        });
        HomeRelLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpandableListEnabled = false;
                mExpandableListView.setVisibility(View.GONE);
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

        gps = new TrackGPS(getActivity());
        try {
            if (gps.canGetLocation()) {
                Double lat = gps.getLatitude();
                Double lng = gps.getLongitude();
                Strlat = laln.latitude;
                Strlong = laln.longitude;


                try {
                    Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                    addresses = geo.getFromLocation(lat, lng, 1);
                    if (addresses.isEmpty()) {
                    } else {
                        if (addresses.size() > 0) {
                            String address = addresses.get(0).getAddressLine(0);
                            latt = addresses.get(0).getLatitude();
                            longi = addresses.get(0).getLongitude();
                            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
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
                gps.showSettingsAlert();
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
                            datalist.add(comment);
                            Log.e("dbbd", String.valueOf(document.getData()));
//
                            distancesmtrscurrent.clear();
                            distancescurrentarr.clear();
                            caldis1.clear();
                            nearlat1.clear();
                            nearlong1.clear();
                            distancesmtrs1.clear();
                            distances1.clear();
                            nearimg.clear();
                            Placename.clear();
                            for (int i = 0; i < datalist.size(); i++) {
//

                                loc1.setLatitude(gps.getLatitude());
                                loc1.setLongitude(gps.getLongitude());
                                loc2.setLatitude(Double.parseDouble(datalist.get(i).getCameraLat()));
                                loc2.setLongitude(Double.parseDouble(datalist.get(i).getCameraLong()));

                                double distancemtrs1 = loc1.distanceTo(loc2);
                                distancesmtrs1.add(distancemtrs1);
                                Log.e("distancemtrs1", String.valueOf(distancesmtrs1));
//                        for(int j =0;j<distancesmtrs.size();j++){
//
                                distanceval = loc1.distanceTo(loc2) / 1000;
                                distances1.add(String.valueOf(distanceval));
                                Log.e("distance", String.valueOf(distances1));

                                if (distancemtrs1 < 1500) {
                                    caldis1.add(String.valueOf(distancesmtrs1));
                                    Log.e("caldis1", String.valueOf(caldis1));
                                    nearlat1.add(datalist.get(i).getCameraLat());
                                    nearlong1.add(datalist.get(i).getCameraLong());
                                    nearimg.add(datalist.get(i).getCameraImageUrl());
                                    Placename.add(datalist.get(i).getCameraLocationName());
                                    Log.e("nearlat1", String.valueOf(nearlat1));
                                    Log.e("nearlong1", String.valueOf(nearlong1));
                                    Log.e("nearimg", String.valueOf(nearimg));

                                    final CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
                                    carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                    carouselLayoutManager.setMaxVisibleItems(1);

                                    mNearestPlaceRecycler.setLayoutManager(carouselLayoutManager);
                                    mNearestPlaceRecycler.setHasFixedSize(true);
//        mNearestrecyclerAdapter = new NearestRecyclerAdapter(getActivity(),datalist,nearlat1,nearlong1,distances1);
                                    mNearestrecyclerAdapter = new CarouselNearestAdapter(getActivity(), nearimg, nearlat1, nearlong1, distances1, Placename);
                                    mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
                                    mNearestPlaceRecycler.addOnScrollListener(new CenterScrollListener());
                                    mNearestrecyclerAdapter.notifyDataSetChanged();

                                    LatLng sydney = new LatLng(Double.parseDouble(datalist.get(i).getCameraLat()), Double.parseDouble(datalist.get(i).getCameraLong()));
                                    Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                                    Mmap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                                }

//


                            }

                        }
                    }

                });


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (autoCompView.getText().toString().equals(null) || autoCompView.getText().toString().isEmpty() || description.equals(null)) {
                    Toast.makeText(getActivity(), "Please enter the search location", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), NearestLocMapsActivity.class);
                    intent.putExtra("placeid", placeId);
                    intent.putExtra("latitude", String.valueOf(Latitude).toString().trim());
                    intent.putExtra("longitude", String.valueOf(Longitude).toString().trim());
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

        filterButton = (Button) view.findViewById(R.id.filter_button);

        filterButton.setVisibility(View.VISIBLE);

        filterButton.setOnClickListener(new View.OnClickListener() {
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

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {

                if (groupPosition != previousGroup) {
                    mExpandableListView.collapseGroup(previousGroup);
                    previousGroup = groupPosition;
                }

                Toast.makeText(getActivity(),
                        mExpandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });
//
//                //Carousel

//                mSearchView = view.findViewById(R.id.home_search_view);

        //NearestPlace Recycler
        mNearestPlaceRecycler = view.findViewById(R.id.nearest_places_recycler);
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
        //Expandable List and Searchview
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need phone Permission");
                builder.setMessage("This app needs phone permission.");
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
            } else if (permissionStatus.getBoolean(Manifest.permission.CALL_PHONE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need phone Permission");
                builder.setMessage("This app needs phone permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getActivity(), "Go to Permissions to Grant phone", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }

            else {
                //just request the permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.CALL_PHONE, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }


        return view;
    }

    private void proceedAfterPermission() {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Mmap.clear();
        //    Mmap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_markf)));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln, 13.5f));
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(13.5f), 2000, null);
        Mmap.setMaxZoomPreference(13.5f);
        Mmap.setMinZoomPreference(6.5f);
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
        Mmap.setMyLocationEnabled(true);
        Mmap.getUiSettings().setMyLocationButtonEnabled(false);

//
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mmap=googleMap;
        Mmap.clear();
        Double lat = gps.getLatitude();
        Double lng = gps.getLongitude();
        LatLng locateme = new LatLng(lat, lng);
        Mmap.getUiSettings().isZoomControlsEnabled();

        Mmap.getUiSettings().setMyLocationButtonEnabled(false);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Mmap.setMyLocationEnabled(false);

//
           Mmap.addMarker(new MarkerOptions().position(locateme).icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocationicon)));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(locateme,13.5f));
        // map.animateCamera(CameraUpdateFactory.zoomIn());
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(13.5f), 2000, null);
        Mmap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                if(gps.canGetLocation()) {
                    Double lat = gps.getLatitude();
                    Double lng = gps.getLongitude();
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
        Mmap.clear();

        //  Mmap.addMarker(new MarkerOptions().position(laln).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,13.5f));
        // map.animateCamera(CameraUpdateFactory.zoomIn());
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(13.5f), 2000, null);
        latitu=laln.latitude;
        longitu=laln.longitude;



    }


            public AdapterView.OnItemClickListener mAutocompleteClickListener
                    = new AdapterView.OnItemClickListener() {
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
                                        for (int i = 0; i < datalist.size(); i++) {

                                            Log.e("Latitude", "" + datalist.get(i).getCameraLat());
                                            Log.e("Longitude", "" + datalist.get(i).getCameraLong());

                                            loc1.setLatitude(Latitude);
                                            loc1.setLongitude(Longitude);
                                            loc2.setLatitude(Double.parseDouble(datalist.get(i).getCameraLat()));
                                            loc2.setLongitude(Double.parseDouble(datalist.get(i).getCameraLong()));
                                            double distance = 0;
                                            double distancemtrs = loc1.distanceTo(loc2);
                                            distancesmtrs.add(distancemtrs);
                                            Log.e("distancemtrs", String.valueOf(distancesmtrs));
//                        for(int j =0;j<distancesmtrs.size();j++){

                                            if (distancemtrs < 1500) {
                                                caldis.add(String.valueOf(distancesmtrs));
                                                Log.e("caldis", String.valueOf(caldis));
                                                nearlat.add(datalist.get(i).getCameraLat());
                                                nearlong.add(datalist.get(i).getCameraLong());
                                                Log.e("nearlat", String.valueOf(nearlat));
                                                Log.e("nearlong", String.valueOf(nearlong));
//                            }
                                            }
                                            distance = loc1.distanceTo(loc2) / 1000;
                                            Log.e("distance", String.valueOf(distance));
                                            distances.add(distance);
//                                          distancedata();


//                                            caldis.add(distance);
//                                            Log.e("caldis", String.valueOf(caldis));
//                                            distance(Latitude,Longitude,Double.parseDouble(datalist.get(i).getCameraLat()),Double.parseDouble(datalist.get(i).getCameraLong()));


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
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

//                ActivityCompat.requestPermissions(getActivity(),
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                        MY_PERMISSIONS_REQUEST_LOCATION);

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
//                new AlertDialog.Builder(getActivity())
//                        .setTitle("Hi")
//                        .setMessage("Hello")
//                        .setPositiveButton("Ho",new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Prompt the user once explanation has been shown
//                                ActivityCompat.requestPermissions(getActivity(),
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                        MY_PERMISSIONS_REQUEST_LOCATION);
//                            }
//                        })
//                        .create()
//                        .show();

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);



            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
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


//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if (sentToSettings) {
//            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                //Got Permission
//                proceedAfterPermission();
//            }
//        }
//    }


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



}
