package com.pyrky_android.activity;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pyrky_android.Manifest;
import com.pyrky_android.R;
import com.pyrky_android.location.GetNearbyPlacesData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NearestLocMapsActivity extends FragmentActivity implements OnMapReadyCallback
    {
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
       double lat[] = {70.01383623,56.50329796,1.23736985,-24.33605988,11.38350584,-58.68375965,44.87310434,147.64797704,-3.02408824,-21.33447419};
        double lng[] = {-24.21957723,56.50329796,-163.58662616,16.88948658,62.62863347,-43.46925429,-91.28527609,85.94545339,-82.49033554,-175.53067807};
        private ArrayList<LatLng> latlngs = new ArrayList<>();
        private GoogleMap mMap;
        double mLatitude;
        double mLongitude;
        private int PROXIMITY_RADIUS = 10000;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_loc_maps);

        //Looping for multiple lat/lng input
        for (int i= 0; i<lat.length;i++){
            latlngs.add(new LatLng(lat[i],lng[i]));
        }


        if (getIntent()!=null){
            mLatitude = getIntent().getDoubleExtra("lat",1.0);
            mLongitude = getIntent().getDoubleExtra("lng",1.0);
        }

        mLatitude = 13.0002586;
        mLongitude = 80.2046057;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ( SupportMapFragment ) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button search = findViewById(R.id.search_button);
        final Object dataTransfer[] = new Object[2];
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.clear();
                String hospital = "hospital";
                String url = getUrl(mLatitude,mLongitude,hospital);

                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(dataTransfer);

                Toast.makeText(NearestLocMapsActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getUrl(double latitude,double longitude,String nearbyPlace){

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location"+latitude+","+longitude);
        googlePlacesUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type="+nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
//        googlePlacesUrl.append("&key="+"AIzaSyC9DZmQ7_6n9TdF8is_fGA-U2ir1DiGLKg");
//        Api id which is in Manifest
        googlePlacesUrl.append("&key="+"AIzaSyDytCl7ZsDxnOfoEvB1TUjKkZagCiRzaT8");

        return googlePlacesUrl.toString();

    }


/**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */
public void onMapSearch(View view) {
    EditText locationSearch = (EditText ) findViewById(R.id.editText);
    String location = locationSearch.getText().toString();
    List<Address> addressList = null;

    if (location != null || !location.equals("")) {
        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(mLatitude, mLongitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(sydney);
        markerOptions.title("Marker in Sydney");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        for (LatLng point : latlngs) {
            markerOptions.position(point);
            markerOptions.title("someTitle");
            markerOptions.snippet("someDesc");
            mMap.addMarker(markerOptions);
        }



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

    }
    }
