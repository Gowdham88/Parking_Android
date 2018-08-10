package com.pyrky_android.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pyrky_android.ExpandableListData;
import com.pyrky_android.R;
import com.pyrky_android.activity.HomeActivity;
import com.pyrky_android.activity.NearestLocMapsActivity;
import com.pyrky_android.adapter.CarouselNearestAdapter;
import com.pyrky_android.adapter.ExpandableListAdapter;
import com.pyrky_android.adapter.NearestRecyclerAdapter;
import com.pyrky_android.adapter.PlaceArrayAdapter;
import com.pyrky_android.map.ApiClient;
import com.pyrky_android.map.ApiInterface;
import com.pyrky_android.map.PlacesPOJO;
import com.pyrky_android.map.RecyclerViewAdapter;
import com.pyrky_android.map.ResultDistanceMatrix;
import com.pyrky_android.map.StoreModel;
import com.pyrky_android.pojo.DistanceApi;
import com.pyrky_android.pojo.TimeModelClass;
import com.pyrky_android.pojo.UserLocationData;
import com.pyrky_android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import okhttp3.internal.Util;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener,
   GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    //Search View
    SearchView mSearchView;
    //Nearest Place recycler
    RecyclerView mNearestPlaceRecycler;
    int mNearestPlacesImages[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    String[] mNearestPlacesAve = {"1st Avenue", "2nd Avenue", "3rd Avenue", "4th Avenue", "5th Avenue", "6th Avenue", "7th Avenue", "8th Avenue", "9th Avenue", "10th Avenue",};
    String[] mNearestPlacesCity = {"City 1", "City 2", "City 3", "City 4", "City 5", "City 6", "City 7", "City 8", "City 9", "City 10"};
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

    int mLocationImage[] = {R.drawable.loc0, R.drawable.loc1, R.drawable.loc2, R.drawable.loc3,
            R.drawable.loc4, R.drawable.loc5, R.drawable.loc6, R.drawable.loc7, R.drawable.loc8, R.drawable.loc9};

    TextView mSearchButton;
    RelativeLayout HomeRelativeLay;

    private ArrayList<String> permissions = new ArrayList<>();

    Button filterButton;
    List<PlacesPOJO.CustomA> results;
    AutoCompleteTextView autoCompView;

    double latt, longi;
    Location loc1 = new Location("");
    Location loc2 = new Location("");

    String placeId;

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
    RelativeLayout HomeRelLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
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
        mExpandableListView=(ExpandableListView)view.findViewById(R.id.expandableListView);
        HomeRelLayout=(RelativeLayout)view.findViewById(R.id.home_lay);
        HomeRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpandableListEnabled = false;
              mExpandableListView.setVisibility(View.GONE);
                Utils.hideKeyboard(getActivity());
            }
        });

        autoCompView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        autoCompView.setAdapter(mPlaceArrayAdapter);
        autoCompView.setThreshold(1);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(this)
                .build();


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
//                            marker = Mmap.addMarker(new MarkerOptions().position(new LatLng(gps.getLatitude(), gps.getLatitude()))
//                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.smallcar_icon))
//                                    .flat(true));

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
                                        mNearestrecyclerAdapter = new CarouselNearestAdapter(getActivity(),nearimg,nearlat1,nearlong1,distances1,Placename);
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
                Intent intent = new Intent(getActivity(), NearestLocMapsActivity.class);
                intent.putExtra("placeid", placeId);
                intent.putExtra("latitude",String.valueOf(Latitude).toString().trim());
                intent.putExtra("longitude",String.valueOf(Longitude).toString().trim());
                intent.putExtra("value","home");
                Log.e("strLatitude", String.valueOf(Latitude));
                Log.e("strLongitude", String.valueOf(Longitude));
                intent.putStringArrayListExtra("placesarray", caldis);
                getActivity().startActivity(intent);
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

                if(groupPosition != previousGroup){
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


//
                //Expandable List and Searchview


            return  view;
            }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Mmap.clear();

        //    Mmap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_markf)));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,13.5f));
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(13.5f), 2000, null);
        Mmap.setMaxZoomPreference(13.5f);
        Mmap.setMinZoomPreference(6.5f);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mmap=googleMap;
        Mmap.clear();
        Double lat = gps.getLatitude();
        Double lng = gps.getLongitude();
        LatLng locateme = new LatLng(lat, lng);
        Mmap.getUiSettings().isZoomControlsEnabled();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Mmap.setMyLocationEnabled(true);
        //   Mmap.addMarker(new MarkerOptions().position(locateme).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(locateme,13.5f));
        // map.animateCamera(CameraUpdateFactory.zoomIn());
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(13.5f), 2000, null);
        //      Mmap.addMarker(new MarkerOptions().position(new LatLng(12.978424,80.219333)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
        //    Mmap.addMarker(new MarkerOptions().position(new LatLng(13.031522,80.201531)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
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

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mGoogleApiClient.stopAutoManage(getActivity());
//        mGoogleApiClient.disconnect();
//    }


}
