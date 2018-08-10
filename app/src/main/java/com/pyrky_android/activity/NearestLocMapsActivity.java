package com.pyrky_android.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pyrky_android.R;
import com.pyrky_android.adapter.CarouselDetailMapAdapter;
import com.pyrky_android.adapter.CustomInfoWindowGoogleMap;
import com.pyrky_android.fragment.TrackGPS;
import com.pyrky_android.pojo.UserLocationData;

import java.util.ArrayList;
import java.util.List;


public class NearestLocMapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final float METERS_100 = 100;
    /* Latitude: 70°00′50″N   70.01383623
         Longitude: 24°13′10″W   -24.21957723
         Latitude: 56°30′12″N   56.50329796
         Longitude: 147°38′53″E   147.64797704
         Latitude: 1°14′15″N   1.23736985
         Longitude: 163°35′12″W   -163.58662616
         Latitude: 24°20′10″S   -24.33605988
         Longitude: 16°53′22″E   16.88948658
         Latitude: 11°23′01″N   11.38350584
         Longitude: 62°37′43″E   62.62863347
         Latitude: 58°41′02″S   -58.68375965
         Longitude: 43°28′09″W   -43.46925429
         Latitude: 44°52′23″N   44.87310434
         Longitude: 91°17′07″W   -91.28527609
         Latitude: 41°44′56″S   -41.74890304
         Longitude: 85°56′44″E   85.94545339
         Latitude: 3°01′27″S   -3.02408824
         Longitude: 82°29′25″W   -82.49033554
         Latitude: 21°20′04″S   -21.33447419
         Longitude: 175°31′50″W   -175.53067807*/
    Context context = this;
    double lat[] = {70.01383623, 56.50329796, 1.23736985, -24.33605988, 11.38350584, -58.68375965, 44.87310434, 147.64797704, -3.02408824, -21.33447419};
    double lng[] = {-24.21957723, 56.50329796, -163.58662616, 16.88948658, 62.62863347, -43.46925429, -91.28527609, 85.94545339, -82.49033554, -175.53067807};
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    double mLatitude;
    double mLongitude;
    private int PROXIMITY_RADIUS = 10000;
    //Carousel
    CarouselDetailMapAdapter mNearestrecyclerAdapter;
    RecyclerView mNearestPlaceRecycler;
    int mNearestPlacesImages[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    String[] mNearestPlacesAve = {"1st Avenue", "2nd Avenue", "3rd Avenue", "4th Avenue", "5th Avenue", "6th Avenue", "7th Avenue", "8th Avenue", "9th Avenue", "10th Avenue",};
    String[] mNearestPlacesCity = {"City 1", "City 2", "City 3", "City 4", "City 5", "City 6", "City 7", "City 8", "City 9", "City 10"};
    int mLocationImage[] = {R.drawable.loc0, R.drawable.loc1, R.drawable.loc2, R.drawable.loc3,
            R.drawable.loc4, R.drawable.loc5, R.drawable.loc6, R.drawable.loc7, R.drawable.loc8, R.drawable.loc9};


    private LatLng currLocation;

    private static final LatLng POINTA = new LatLng(32.820193, -117.232568);
    private static final LatLng POINTB = new LatLng(32.829129, -117.232204);
    private static final LatLng POINTC = new LatLng(32.821114, -117.231534);
    private static final LatLng POINTD = new LatLng(32.825157, -117.232003);

    // Array to store hotspot coordinates
    private ArrayList<LatLng> markerCoords = new ArrayList<LatLng>();
    private static final int POINTS = 4;

    private GoogleApiClient mGoogleApiClient;
    String placeId;
    double Latitude, Longitude;


    Location loc1 = new Location("");
    Location loc2 = new Location("");

    ArrayList<String> distances1 = new ArrayList<>();
    ArrayList<Double> distancesmtrsmap = new ArrayList<>();
    ArrayList<String> caldismap = new ArrayList<>();
    ArrayList<String> nearlat1 = new ArrayList<>();
    ArrayList<String> nearlong1 = new ArrayList<>();
    ArrayList<String> nearimg = new ArrayList<>();
    ArrayList<Double> distancesmtrscurrentmap = new ArrayList<>();
    ArrayList<String> distancescurrentarrmap = new ArrayList<>();
    ArrayList<String> Placename = new ArrayList<>();
    double distanceval;
    GoogleMap Mmap;


    private static final int GOOGLE_API_CLIENT_ID = 0;

    Location mLastLocation;
    Marker myLocatMarker;

    SupportMapFragment mapFrag;
    private TrackGPS gps;
    String Strlat, Strlong, latvalue;
    LatLng laln;
    String address1;
    Location mLocation;
    List<Address> addresses;
    String lattitude, longitude, address, city, state, country, postalCode, knownName;
    double latitud, longitud, latitu, longitu;
    List<UserLocationData> datalist = new ArrayList<UserLocationData>();
    Marker marker;
    String maplat, maplongitude;
    ImageView BackImg;
    TextView TitlaTxt;
    String mLat,mLongi;
    String Nameval="home";
    Double Mlat,Mlongi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_loc_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(NearestLocMapsActivity.this);

        BackImg = (ImageView) findViewById(R.id.back_image);
        TitlaTxt = (TextView) findViewById(R.id.extra_title);
        TitlaTxt.setText("Map");
        BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent chatIntent = getIntent();
        if(chatIntent!=null){
            String Value=chatIntent.getStringExtra("value");

            if(Nameval.equals(Value)){
                mLat = chatIntent.getStringExtra("latitude").toString().trim();
                mLongi = chatIntent.getStringExtra("longitude").toString().trim();
//            placeId = chatIntent.getStringExtra("placeid");
//            Mlat= Double.valueOf(mLat);
//            Mlongi= Double.valueOf(mLongi);
////        maplongitude = chatIntent.getStringExtra("longitude");
                Log.e("hlattitude", String.valueOf(mLat));
                Log.e("hlongitude", String.valueOf(mLongi));
            }
            else {
                mLat = chatIntent.getStringExtra("lat").toString().trim();
                mLongi = chatIntent.getStringExtra("lng").toString().trim();
//            placeId = chatIntent.getStringExtra("placeid");
//            Mlat= Double.valueOf(mLat);
//            Mlongi= Double.valueOf(mLongi);
////        maplongitude = chatIntent.getStringExtra("longitude");
                Log.e("lattitude", String.valueOf(mLat));
                Log.e("longitude", String.valueOf(mLongi));
            }
//            placeId = chatIntent.getStringExtra("placeid");

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
                            distancesmtrscurrentmap.clear();
                            distancescurrentarrmap.clear();
                            caldismap.clear();
                            nearlat1.clear();
                            nearlong1.clear();
                            distancesmtrsmap.clear();
                            distances1.clear();
                            nearimg.clear();
                            Placename.clear();
                            for (int i = 0; i < datalist.size(); i++) {
//

                                loc1.setLatitude(Double.parseDouble(mLat));
                                loc1.setLongitude(Double.parseDouble(mLongi));
                                loc2.setLatitude(Double.parseDouble(datalist.get(i).getCameraLat()));
                                loc2.setLongitude(Double.parseDouble(datalist.get(i).getCameraLong()));

                                double distancemtrs1 = loc1.distanceTo(loc2);
                                distancesmtrsmap.add(distancemtrs1);
                                Log.e("distancemtrsmap", String.valueOf(distancesmtrsmap));
//                        for(int j =0;j<distancesmtrs.size();j++){
//
                                distanceval = loc1.distanceTo(loc2) / 1000;
                                distances1.add(String.valueOf(distanceval));
                                Log.e("distancemap", String.valueOf(distances1));

                                if (distancemtrs1 < 1500) {
                                    caldismap.add(String.valueOf(distancesmtrsmap));
                                    Log.e("caldismap", String.valueOf(caldismap));
                                    nearlat1.add(datalist.get(i).getCameraLat());
                                    nearlong1.add(datalist.get(i).getCameraLong());
                                    nearimg.add(datalist.get(i).getCameraImageUrl());
                                    Placename.add(datalist.get(i).getCameraLocationName());
                                    Log.e("nearlatmap", String.valueOf(nearlat1));
                                    Log.e("nearlongmap", String.valueOf(nearlong1));
                                    Log.e("nearimgmap", String.valueOf(nearimg));

//                            //Carousel View
                                    final CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
                                    carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                    carouselLayoutManager.setMaxVisibleItems(3);

                                    mNearestPlaceRecycler = findViewById(R.id.nearest_places_recycler);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                    mNearestPlaceRecycler.setLayoutManager(carouselLayoutManager);
                                    mNearestPlaceRecycler.setHasFixedSize(true);

                                    mNearestrecyclerAdapter = new CarouselDetailMapAdapter(context,nearimg,nearlat1,nearlong1,distances1,Placename);
                                    mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
                                    mNearestPlaceRecycler.addOnScrollListener(new CenterScrollListener());


                                    LatLng sydney = new LatLng(Double.parseDouble(datalist.get(i).getCameraLat()), Double.parseDouble(datalist.get(i).getCameraLong()));
                                    Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                                    Mmap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                                }

//


                            }

                        }
                    }

                });


//
//        mGoogleApiClient = new GoogleApiClient.Builder(NearestLocMapsActivity.this)
//                .addApi(Places.GEO_DATA_API)
//                .enableAutoManage(NearestLocMapsActivity.this, GOOGLE_API_CLIENT_ID, NearestLocMapsActivity.this)
//                .addConnectionCallbacks(NearestLocMapsActivity.this)
//                .build();
//

    }







    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        LatLng latLng = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLongi));
        Mmap.clear();

        //    Mmap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_markf)));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,10.5f));
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(25.5f), 2000, null);
        Mmap.setMaxZoomPreference(24.5f);
        Mmap.setMinZoomPreference(6.5f);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        Mmap = gMap;
        Mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            Mmap.setMyLocationEnabled(false);
        } catch (SecurityException se) {

        }

        //Edit the following as per you needs
//        Mmap.setTrafficEnabled(true);
//        Mmap.setIndoorEnabled(true);
//        Mmap.setBuildingsEnabled(true);
        Mmap.getUiSettings().setZoomControlsEnabled(true);
        Mmap.getUiSettings().setRotateGesturesEnabled(false);
        //

        LatLng placeLocation = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLongi)); //Make them global
        Marker placeMarker = Mmap.addMarker(new MarkerOptions().position(placeLocation));
        Mmap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(25), 1000, null);



        Mmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
//                ShowRulesAlert();


                CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(NearestLocMapsActivity.this);
                Mmap.setInfoWindowAdapter(customInfoWindow);
                return false;
            }
    });
    }

    private void ShowRulesAlert() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NearestLocMapsActivity.this);
        LayoutInflater factory = LayoutInflater.from(NearestLocMapsActivity.this);
        View bottomSheetView = factory.inflate(R.layout.rules_layout, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        RelativeLayout ViewLay=(RelativeLayout)bottomSheetDialog.findViewById(R.id.view_lay);
        RelativeLayout NaviLay=(RelativeLayout)bottomSheetDialog.findViewById(R.id.navi_lay);

        TextView ViewLTxt=(TextView)bottomSheetDialog.findViewById(R.id.view_txt);
        TextView NaviTxt=(TextView)bottomSheetDialog.findViewById(R.id.navi_txt);


        ViewLTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();

            }
        });

        NaviTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();
            }
        });
        ViewLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        NaviLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

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

//
    }
