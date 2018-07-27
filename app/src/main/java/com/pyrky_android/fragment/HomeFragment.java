package com.pyrky_android.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pyrky_android.ExpandableListData;
import com.pyrky_android.MyApplication;
import com.pyrky_android.R;
import com.pyrky_android.activity.HomeActivity;
import com.pyrky_android.activity.NearestLocMapsActivity;
import com.pyrky_android.activity.SignInActivity;
import com.pyrky_android.adapter.ExpandableListAdapter;
import com.pyrky_android.adapter.NearestRecyclerAdapter;
import com.pyrky_android.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
        {
    //Search View
    SearchView mSearchView;
    //Nearest Place recycler
    RecyclerView mNearestPlaceRecycler;
    int mNearestPlacesImages[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    String[] mNearestPlacesAve = {"1st Avenue", "2nd Avenue", "3rd Avenue", "4th Avenue", "5th Avenue", "6th Avenue", "7th Avenue", "8th Avenue", "9th Avenue", "10th Avenue",};
    String[] mNearestPlacesCity = {"City 1", "City 2", "City 3", "City 4", "City 5", "City 6", "City 7", "City 8", "City 9", "City 10"};
    NearestRecyclerAdapter mNearestrecyclerAdapter;

    //Expandable List View
    ExpandableListView mExpandableListView;
    ExpandableListAdapter mExpandableListAdapter;
    List<String> mExpandableListTitle;
    HashMap<String, List<String>> mExpandableListDetail;
    Boolean isExpandableListEnabled = false;

    //Location
    double lat[] = {70.01383623,56.50329796,1.23736985,-24.33605988,11.38350584,-58.68375965,44.87310434,147.64797704,-3.02408824,-21.33447419};
    double lng[] = {-24.21957723,56.50329796,-163.58662616,16.88948658,62.62863347,-43.46925429,-91.28527609,85.94545339,-82.49033554,-175.53067807};
    int mLocationImage[] = {R.drawable.loc0,R.drawable.loc1,R.drawable.loc2,R.drawable.loc3,
            R.drawable.loc4,R.drawable.loc5,R.drawable.loc6,R.drawable.loc7,R.drawable.loc8,R.drawable.loc9};
    private ArrayList<LatLng> latlngs = new ArrayList<>();

    private static final int LOCATION_REQUEST_CODE = 101;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    String mProvider;
    LatLng mLatLng;
    SupportMapFragment mMapView;
    GoogleMap mGoogleMap;
    TextView mSearchButton;
    LinearLayout HomeRelativeLay;
    PlaceAutocompleteFragment autocompleteFragment;
    @Inject
    Retrofit retrofit;

            @Override
            public void onAttach(Context context) {
                ((MyApplication)context.getApplicationContext()).getNetComponent().inject(this);
                super.onAttach(context);
            }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Instantiating the GoogleApiClient
        buildGoogleApiClient();
    }
            @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        //Carousel
        final CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        carouselLayoutManager.setMaxVisibleItems(1);
        //NearestPlace Recycler
        mNearestPlaceRecycler = view.findViewById(R.id.nearest_places_recycler);
                HomeRelativeLay = view.findViewById(R.id.home_lay);
                HomeRelativeLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.hideKeyboard(getActivity());
                    }
                });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mNearestPlaceRecycler.setLayoutManager(carouselLayoutManager);
        mNearestPlaceRecycler.setHasFixedSize(true);

        mNearestrecyclerAdapter = new NearestRecyclerAdapter(getActivity(), mNearestPlacesImages, mNearestPlacesAve, mNearestPlacesCity, mLocationImage);
        mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
        mNearestPlaceRecycler.addOnScrollListener(new CenterScrollListener());

        //Expandable List and Searchview
        mExpandableListView = view.findViewById(R.id.expandableListView);
        mSearchView = view.findViewById(R.id.home_search_view);
                Button filterButton = view.findViewById(R.id.filter_button);
        mSearchButton = view.findViewById(R.id.search_btn);
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setIconified(false);
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideKeyboard(mSearchView);
                mExpandableListView.setVisibility(View.GONE);
                isExpandableListEnabled = false;
                return false;
            }
        });
        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(v);
                }
                mExpandableListView.setVisibility(View.GONE);
                isExpandableListEnabled = false;
            }
        });

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandableListView.setVisibility(View.GONE);
                isExpandableListEnabled = false;
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NearestLocMapsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpandableListEnabled) {
                    isExpandableListEnabled = true;
                    mExpandableListView.setVisibility(View.VISIBLE);
                    mExpandableListDetail = ExpandableListData.getData();
                    mExpandableListTitle = new ArrayList<String>(mExpandableListDetail.keySet());
                    Collections.reverse(mExpandableListTitle);
                    mExpandableListAdapter = new ExpandableListAdapter(getActivity(), mExpandableListTitle, mExpandableListDetail);

                    mExpandableListView.setAdapter(mExpandableListAdapter);
                } else {
                    mExpandableListView.setVisibility(View.GONE);
                    isExpandableListEnabled = false;
                }
             /*   FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.filter_fragment,FiltersFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();*/
            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),
                        mExpandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        mExpandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity(),
                        mExpandableListTitle.get(groupPosition)
                                + " -> "
                                + mExpandableListDetail.get(
                                mExpandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });



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
        for (int i= 0; i<lat.length;i++){
            latlngs.add(new LatLng(lat[i],lng[i]));
        }

        return view;
    }

    public void onStart() {
        super.onStart();
        // Initiating the connection
        mGoogleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        // Disconnecting the connection
        mGoogleApiClient.disconnect();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        checkLocationandAddToMap();
    }
    //Callback invoked once the GoogleApiClient is connected successfully
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mMapView = ( SupportMapFragment ) getChildFragmentManager().findFragmentById(R.id.current_location_view);
        mMapView.getMapAsync(this);
        }
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), String.valueOf(i), Toast.LENGTH_SHORT).show();
    }
    //Callback invoked if the GoogleApiClient connection fails
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), String.valueOf(connectionResult.getErrorMessage()), Toast.LENGTH_SHORT).show();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    checkLocationandAddToMap();
                } else
                    Toast.makeText(getActivity(), "Location Permission Denied", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void checkLocationandAddToMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

        } else {
        //Fetching the last known location using the Fus
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are Here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        Toast.makeText(getActivity(), "Your Location is at "+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_LONG).show();
        //Adding the created the marker on the map
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
        mGoogleMap.setMyLocationEnabled(true);
        mMapView.setUserVisibleHint(false);

            for (LatLng point : latlngs) {
                markerOptions.position(point);
                markerOptions.title("someTitle");
                markerOptions.snippet("someDesc");
                mGoogleMap.addMarker(markerOptions);
            }
    }

    }
        protected synchronized void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

            public void hideKeyboard(View view){
                InputMethodManager inputMethodManager =(InputMethodManager )getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }
//Current Location = 13.0002586,80.2046057