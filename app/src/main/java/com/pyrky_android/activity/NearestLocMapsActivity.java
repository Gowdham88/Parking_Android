package com.pyrky_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pyrky_android.R;
import com.pyrky_android.adapter.NearestRecyclerAdapter;
import com.pyrky_android.fragment.TrackGPS;
import com.pyrky_android.location.GetNearbyPlacesData;
import com.pyrky_android.pojo.UserLocationData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class NearestLocMapsActivity extends FragmentActivity implements OnMapReadyCallback {
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
    NearestRecyclerAdapter mNearestrecyclerAdapter;
    RecyclerView mNearestPlaceRecycler;
    int mNearestPlacesImages[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    String[] mNearestPlacesAve = {"1st Avenue", "2nd Avenue", "3rd Avenue", "4th Avenue", "5th Avenue", "6th Avenue", "7th Avenue", "8th Avenue", "9th Avenue", "10th Avenue",};
    String[] mNearestPlacesCity = {"City 1", "City 2", "City 3", "City 4", "City 5", "City 6", "City 7", "City 8", "City 9", "City 10"};
    int mLocationImage[] = {R.drawable.loc0, R.drawable.loc1, R.drawable.loc2, R.drawable.loc3,
            R.drawable.loc4, R.drawable.loc5, R.drawable.loc6, R.drawable.loc7, R.drawable.loc8, R.drawable.loc9};


    private GoogleMap mMap;

    private LatLng currLocation;

    private static final LatLng POINTA = new LatLng(32.820193, -117.232568);
    private static final LatLng POINTB = new LatLng(32.829129, -117.232204);
    private static final LatLng POINTC = new LatLng(32.821114, -117.231534);
    private static final LatLng POINTD = new LatLng(32.825157, -117.232003);

    // Array to store hotspot coordinates
    private ArrayList<LatLng> markerCoords = new ArrayList<LatLng>();
    private static final int POINTS = 4;


    GoogleMap Mmap;
    SupportMapFragment mapFrag;
    private TrackGPS gps;
    String  Strlat,Strlong,latvalue;
    LatLng laln;
    String address1;
    Location mLocation;
    List<Address> addresses;
    String lattitude,longitude,address,city,state,country,postalCode,knownName;
    double latitud, longitud,latitu,longitu;
    List<UserLocationData> datalist = new ArrayList<UserLocationData>();
    Marker marker;
    double maplat,maplongitude;
ImageView BackImg;
TextView  TitlaTxt;
    GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_loc_maps);
        BackImg=(ImageView) findViewById(R.id.back_image);
        TitlaTxt=(TextView)findViewById(R.id.extra_title);
        TitlaTxt.setText("Map");
        BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        setUpMapIfNeeded();
//       Intent in=getIntent();
////        if (in != null) {
//
//            maplat = Double.parseDouble(getIntent().getExtras().getString("lattitude"));
//            maplongitude =Double.parseDouble(getIntent().getExtras().getString("longitude"));
//            Log.e("lattitude", String.valueOf(maplat));
//        Log.e("longitude", String.valueOf(maplongitude));
//        }

        //Looping for multiple lat/lng input
//        for (int i= 0; i<lat.length;i++){
//            latlngs.add(new LatLng(lat[i],lng[i]));
//        }
//
//
//        if (getIntent()!=null){
//            mLatitude = getIntent().getDoubleExtra("lat",1.0);
//            mLongitude = getIntent().getDoubleExtra("lng",1.0);
//        }

        //Carousel View
        final CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        carouselLayoutManager.setMaxVisibleItems(3);

        mNearestPlaceRecycler = findViewById(R.id.nearest_places_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mNearestPlaceRecycler.setLayoutManager(carouselLayoutManager);
        mNearestPlaceRecycler.setHasFixedSize(true);

        mNearestrecyclerAdapter = new NearestRecyclerAdapter(context, mNearestPlacesImages, mNearestPlacesAve, mNearestPlacesCity, mLocationImage);
        mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
        mNearestPlaceRecycler.addOnScrollListener(new CenterScrollListener());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//
//        LatLng placeLocation = new LatLng(maplat,maplongitude); //Make them global
//        Marker placeMarker = googleMap.addMarker(new MarkerOptions().position(placeLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallcar_icon)));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);

//                mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//                mapFrag.getMapAsync(getActivity());
//                mapFrag.getMapAsync(getActivity());

       /* autocompleteFragment = getSupportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
            }

            @Override
            public void onError(Status status) {

            }
        });*/

        //Looping for multiple lat/lng input
//        for (int i= 0; i<lat.length;i++){
//            latlngs.add(new LatLng(lat[i],lng[i]));
//        }



//        mLatitude = 13.0002586;
//        mLongitude = 80.2046057;
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//
//
//        Button search = findViewById(R.id.search_button);
//        final Object dataTransfer[] = new Object[2];
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mMap.clear();
//                String hospital = "hospital";
//                String url = getUrl(mLatitude,mLongitude,hospital);
//
//                dataTransfer[0] = mMap;
//                dataTransfer[1] = url;
//
//                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
//                getNearbyPlacesData.execute(dataTransfer);
//
//                Toast.makeText(NearestLocMapsActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(12.8448, 80.2255);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        mLocation = location;
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        Mmap.clear();
//
//        //    Mmap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_markf)));
//        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,6.5f));
//        Mmap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
//        Mmap.setMaxZoomPreference(14.5f);
//        Mmap.setMinZoomPreference(6.5f);
//    }

//    @Override
//    public void onMapReady(GoogleMap gMap) {
//        googleMap = gMap;
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        try {
//            googleMap.setMyLocationEnabled(true);
//        } catch (SecurityException se) {
//
//        }
//
//        //Edit the following as per you needs
//        googleMap.setTrafficEnabled(true);
//        googleMap.setIndoorEnabled(true);
//        googleMap.setBuildingsEnabled(true);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        //
//
//        LatLng placeLocation = new LatLng(maplat,maplongitude); //Make them global
//        Marker placeMarker = googleMap.addMarker(new MarkerOptions().position(placeLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.smallcar_icon)));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
//    }

//    public void handlenewlocation(final LatLng laln)
//    {
//        Mmap.clear();
//
//        //  Mmap.addMarker(new MarkerOptions().position(laln).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
//        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,6.5f));
//        // map.animateCamera(CameraUpdateFactory.zoomIn());
//        Mmap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
//        latitu=laln.latitude;
//        longitu=laln.longitude;
//
//
//
//    }


//    private void nearestLocationAdapter() {
//        final CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
//        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
//        carouselLayoutManager.setMaxVisibleItems(3);
//
//        mNearestPlaceRecycler = findViewById(R.id.nearest_places_recycler);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//        mNearestPlaceRecycler.setLayoutManager(carouselLayoutManager);
//        mNearestPlaceRecycler.setHasFixedSize(true);
//
//        mNearestrecyclerAdapter = new NearestRecyclerAdapter(context, mNearestPlacesImages, mNearestPlacesAve, mNearestPlacesCity, mLocationImage);
//        mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
//        mNearestPlaceRecycler.addOnScrollListener(new CenterScrollListener());
//
//
//    }


    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                markerCoords.add(POINTA);
                markerCoords.add(POINTB);
                markerCoords.add(POINTC);
                markerCoords.add(POINTD);

//                mMap.setMyLocationEnabled(true);
//                mMap.setOnMyLocationChangeListener((GoogleMap.OnMyLocationChangeListener) this);

                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // Set My Location blue dot
        if (ActivityCompat.checkSelfPermission(NearestLocMapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearestLocMapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(NearestLocMapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearestLocMapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(provider);

        if (myLocation != null) {
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            currLocation = new LatLng(latitude, longitude);
        }

        for (int i = 0; i < POINTS; i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(markerCoords.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_compact)));
        }
    }

//    @Override
//    public void onLocationChanged(Location location) {
//
//        Location target = new Location("target");
//        for (LatLng point : new LatLng[]{POINTA, POINTB, POINTC, POINTD}) {
//            target.setLatitude(point.latitude);
//            target.setLongitude(point.longitude);
//            if (location.distanceTo(target) < METERS_100) {
//                // bingo!
//            }
//        }
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        if (ActivityCompat.checkSelfPermission(NearestLocMapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearestLocMapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//        mMap.setOnMyLocationChangeListener(NearestLocMapsActivity.this);

//    }
//    private String getUrl(double latitude,double longitude,String nearbyPlace){
//
//        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//        googlePlacesUrl.append("location"+latitude+","+longitude);
//        googlePlacesUrl.append("&radius="+PROXIMITY_RADIUS);
//        googlePlacesUrl.append("&type="+nearbyPlace);
//        googlePlacesUrl.append("&sensor=true");
////        googlePlacesUrl.append("&key="+"AIzaSyC9DZmQ7_6n9TdF8is_fGA-U2ir1DiGLKg");
////        Api id which is in Manifest
//        googlePlacesUrl.append("&key="+"AIzaSyDytCl7ZsDxnOfoEvB1TUjKkZagCiRzaT8");
//
//        return googlePlacesUrl.toString();
//
//    }
//
//
///**
// * Manipulates the map once available.
// * This callback is triggered when the map is ready to be used.
// * This is where we can add markers or lines, add listeners or move the camera. In this case,
// * we just add a marker near Sydney, Australia.
// * If Google Play services is not installed on the device, the user will be prompted to install
// * it inside the SupportMapFragment. This method will only be triggered once the user has
// * installed Google Play services and returned to the app.
// */
//public void onMapSearch(View view) {
//    EditText locationSearch = (EditText ) findViewById(R.id.editText);
//    String location = locationSearch.getText().toString();
//    List<Address> addressList = null;
//
//    if (location != null || !location.equals("")) {
//        Geocoder geocoder = new Geocoder(this);
//        try {
//            addressList = geocoder.getFromLocationName(location, 1);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Address address = addressList.get(0);
//        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//    }
//}
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(mLatitude, mLongitude);
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(sydney);
//        markerOptions.title("Marker in Sydney");
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        for (LatLng point : latlngs) {
//            markerOptions.position(point);
//            markerOptions.title("someTitle");
//            markerOptions.snippet("someDesc");
//            mMap.addMarker(markerOptions);
//        }
//        nearestLocationAdapter();
//
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//
//
//    }
    }
