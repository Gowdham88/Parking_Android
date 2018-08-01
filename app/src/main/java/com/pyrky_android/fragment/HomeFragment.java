package com.pyrky_android.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pyrky_android.ExpandableListData;
import com.pyrky_android.R;
import com.pyrky_android.activity.NearestLocMapsActivity;
import com.pyrky_android.adapter.ExpandableListAdapter;
import com.pyrky_android.adapter.NearestRecyclerAdapter;
import com.pyrky_android.map.ApiClient;
import com.pyrky_android.map.ApiInterface;
import com.pyrky_android.map.PlacesPOJO;
import com.pyrky_android.map.RecyclerViewAdapter;
import com.pyrky_android.map.ResultDistanceMatrix;
import com.pyrky_android.map.StoreModel;
import com.pyrky_android.pojo.TimeModelClass;
import com.pyrky_android.pojo.UserLocationData;
import com.pyrky_android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

import javax.inject.Inject;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener
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
            GoogleMap Mmap;
            SupportMapFragment mapFrag;
            private TrackGPS gps;
            String  Strlat,Strlong;
            LatLng laln;
            String address1;
            Location mLocation;
            double latitu,longitu;
            List<UserLocationData> datalist = new ArrayList<UserLocationData>();
            Marker marker;

    //Location
    double lat[] = {70.01383623,56.50329796,1.23736985,-24.33605988,11.38350584,-58.68375965,44.87310434,147.64797704,-3.02408824,-21.33447419};
    double lng[] = {-24.21957723,56.50329796,-163.58662616,16.88948658,62.62863347,-43.46925429,-91.28527609,85.94545339,-82.49033554,-175.53067807};
    int mLocationImage[] = {R.drawable.loc0,R.drawable.loc1,R.drawable.loc2,R.drawable.loc3,
            R.drawable.loc4,R.drawable.loc5,R.drawable.loc6,R.drawable.loc7,R.drawable.loc8,R.drawable.loc9};


    TextView mSearchButton;
    LinearLayout HomeRelativeLay;
    @Inject
    Retrofit retrofit;

    //Search
    private ArrayList<String> permissionsToRequest;
            private ArrayList<String> permissionsRejected = new ArrayList<>();
            private ArrayList<String> permissions = new ArrayList<>();
            private final static int ALL_PERMISSIONS_RESULT = 101;
            List<StoreModel> storeModels;
            ApiInterface apiService;

            String latLngString;
            LatLng latLng;

            RecyclerView recyclerView;
            EditText editText;
            Button button;
            List<PlacesPOJO.CustomA> results;


//            @Override
//            public void onAttach(Context context) {
//                ((MyApplication)context.getApplicationContext()).getNetComponent().inject(this);
//                super.onAttach(context);
//            }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("CutPasteId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
                Utils.hideKeyboard(getActivity());

                //AutoCompleteText
                PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                        getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        ImageView searchIcon = (ImageView)((LinearLayout)autocompleteFragment.getView()).getChildAt(0);

        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search1));

        // Set the desired behaviour on click
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "YOUR DESIRED BEHAVIOUR HERE", Toast.LENGTH_SHORT).show();
            }
        });
                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        Log.i(TAG, "Place: " + place.getName());
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i(TAG, "An error occurred: " + status);
                    }
                });

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            else {
                fetchLocation();
            }
        } else {
            fetchLocation();
        }


        apiService = ApiClient.getClient().create(ApiInterface.class);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        editText = (EditText) view.findViewById(R.id.editText);
        button = (Button) view.findViewById(R.id.filter_button);

        editText.setVisibility(View.GONE);
        button.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString().trim();
                String[] split = s.split("\\s+");

                if (split.length != 2) {
                    Toast.makeText(getActivity(), "Please enter text in the required format", Toast.LENGTH_SHORT).show();
                } else
                    fetchStores(split[0], split[1]);
            }
        });





        //Carousel
        final CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        carouselLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        carouselLayoutManager.setMaxVisibleItems(1);
                mSearchView = view.findViewById(R.id.home_search_view);

        //NearestPlace Recycler
        mNearestPlaceRecycler = view.findViewById(R.id.nearest_places_recycler);
                HomeRelativeLay = view.findViewById(R.id.home_lay);
                HomeRelativeLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.hideKeyboard(getActivity());
                    }
                });

        mNearestPlaceRecycler.setLayoutManager(carouselLayoutManager);
        mNearestPlaceRecycler.setHasFixedSize(true);
        mNearestrecyclerAdapter = new NearestRecyclerAdapter(getActivity(), mNearestPlacesImages, mNearestPlacesAve, mNearestPlacesCity, mLocationImage);
        mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
        mNearestPlaceRecycler.addOnScrollListener(new CenterScrollListener());
//
        //Expandable List and Searchview
        mExpandableListView = view.findViewById(R.id.expandableListView);

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
                mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                mapFrag.getMapAsync(this);
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

                gps = new TrackGPS(getActivity());
                try {
                    if(gps.canGetLocation()){
                        Double lat =gps .getLatitude();
                        Double lng =  gps.getLongitude();
                        Strlat= String.valueOf(laln.latitude);
                        Strlong= String.valueOf(laln.longitude);
                        List<Address> addresses = null;

                        try {
                            Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                            addresses = geo.getFromLocation(lat, lng, 1);
                            if (addresses.isEmpty()) {
                            }
                            else {
                                if (addresses.size() > 0) {
                                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();
                                    address1=(address + "," + city + "," + state + "," + country + "," + postalCode);
                                    Toast.makeText(getActivity(), address1, Toast.LENGTH_SHORT).show();
                                    Log.e("address1",address1);
                                marker = Mmap.addMarker(new MarkerOptions().position(new LatLng(gps .getLatitude(), gps .getLatitude()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.smallcar_icon))
                                        .flat(true));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        gps.showSettingsAlert();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),NearestLocMapsActivity.class);
                        intent.putExtra("lattitude",gps .getLatitude());
                        intent.putExtra("longitude",gps.getLongitude());
                        getActivity().startActivity(intent);
                    }
                });

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Query first = db.collection("camera");
                first.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                if (documentSnapshots.getDocuments().size() < 1) {
                                    return;
                                }

                                for(DocumentSnapshot document : documentSnapshots.getDocuments()) {

                                    UserLocationData comment = document.toObject(UserLocationData.class);
                                    datalist.add(comment);
                                    Log.e("dbbd", String.valueOf(document.getData()));
                                }
                            }

                        });
                return view;
    }

            @Override
            public void onResume() {
                super.onResume();
//                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            }
            private void fetchStores(String placeType, String businessName) {

                /**
                 * For Locations In India McDonalds stores aren't returned accurately
                 */

                //Call<PlacesPOJO.Root> call = apiService.doPlaces(placeType, latLngString,"\""+ businessName +"\"", true, "distance", APIClient.GOOGLE_PLACE_API_KEY);

                Call<PlacesPOJO.Root> call = apiService.doPlaces(placeType, latLngString, businessName, true, "distance", ApiClient.GOOGLE_PLACE_API_KEY);
                call.enqueue(new Callback<PlacesPOJO.Root>() {
                    @Override
                    public void onResponse(Call<PlacesPOJO.Root> call, Response<PlacesPOJO.Root> response) {
                        PlacesPOJO.Root root = response.body();


                        if (response.isSuccessful()) {

                            if (root.status.equals("OK")) {

                                results = root.customA;
                                storeModels = new ArrayList<>();
                                for (int i = 0; i < results.size(); i++) {

                                    if (i == 10)
                                        break;
                                    PlacesPOJO.CustomA info = results.get(i);


                                    fetchDistance(info);

                                }

                            } else {
                                Toast.makeText(getActivity(), "No matches found near you", Toast.LENGTH_SHORT).show();
                            }

                        } else if (response.code() != 200) {
                            Toast.makeText(getActivity(), "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<PlacesPOJO.Root> call, Throwable t) {
                        // Log error here since request failed
                        call.cancel();
                    }
                });


            }

            private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
                ArrayList<String> result = new ArrayList<>();

                for (String perm : wanted) {
                    if (!hasPermission(perm)) {
                        result.add(perm);
                    }
                }

                return result;
            }

            private boolean hasPermission(String permission) {
                if (canMakeSmores()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
                    }
                }
                return true;
            }

            private boolean canMakeSmores() {
                return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
            }


            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

                switch (requestCode) {

                    case ALL_PERMISSIONS_RESULT:
                        for (String perms : permissionsToRequest) {
                            if (!hasPermission(perms)) {
                                permissionsRejected.add(perms);
                            }
                        }

                        if (permissionsRejected.size() > 0) {


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                                    showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                                    }
                                                }
                                            });
                                    return;
                                }
                            }

                        } else {
                            fetchLocation();
                        }

                        break;
                }

            }

            private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(message)
                        .setPositiveButton("OK", okListener)
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }

            private void fetchLocation() {

                SmartLocation.with(getActivity()).location()
                        .oneFix()
                        .start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                latLngString = location.getLatitude() + "," + location.getLongitude();
                                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                        });
            }

            private void fetchDistance(final PlacesPOJO.CustomA info) {

                Call<ResultDistanceMatrix> call = apiService.getDistance(ApiClient.GOOGLE_PLACE_API_KEY, latLngString, info.geometry.locationA.lat + "," + info.geometry.locationA.lng);
                call.enqueue(new Callback<ResultDistanceMatrix>() {
                    @Override
                    public void onResponse(Call<ResultDistanceMatrix> call, Response<ResultDistanceMatrix> response) {

                        ResultDistanceMatrix resultDistance = response.body();
                        if ("OK".equalsIgnoreCase(resultDistance.status)) {

                            ResultDistanceMatrix.InfoDistanceMatrix infoDistanceMatrix = ( ResultDistanceMatrix.InfoDistanceMatrix ) resultDistance.rows.get(0);
                            ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement distanceElement = ( ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement ) infoDistanceMatrix.elements.get(0);
                            if ("OK".equalsIgnoreCase(distanceElement.status)) {
                                ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDuration = distanceElement.duration;
                                ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDistance = distanceElement.distance;
                                String totalDistance = String.valueOf(itemDistance.text);
                                String totalDuration = String.valueOf(itemDuration.text);

                                storeModels.add(new StoreModel(info.name, info.vicinity, totalDistance, totalDuration));


                                if (storeModels.size() == 10 || storeModels.size() == results.size()) {
                                    RecyclerViewAdapter adapterStores = new RecyclerViewAdapter(results, storeModels);
                                    recyclerView.setAdapter(adapterStores);
                                }

                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<ResultDistanceMatrix> call, Throwable t) {
                        call.cancel();
                    }
                });

            }

            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Mmap.clear();

                //    Mmap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_markf)));
                Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,6.5f));
                Mmap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
                Mmap.setMaxZoomPreference(14.5f);
              
            }

            @Override
            public void onMapReady(GoogleMap googleMap) {
                Mmap=googleMap;
                Mmap.clear();
                Double lat = gps.getLatitude();
                Double lng = gps.getLongitude();
                LatLng locateme = new LatLng(lat, lng);
                Mmap.getUiSettings().isZoomControlsEnabled();

                if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Mmap.setMyLocationEnabled(true);
                Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(locateme,6.5f));
                Mmap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
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
//
            }

            public void handlenewlocation(final LatLng laln)
            {
                Mmap.clear();

                //  Mmap.addMarker(new MarkerOptions().position(laln).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin2)));
//                marker
                Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(laln,6.5f));
                // map.animateCamera(CameraUpdateFactory.zoomIn());
                Mmap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 1500, null);
                latitu=laln.latitude;
                longitu=laln.longitude;

            }

            public void hideKeyboard(View view){
                InputMethodManager inputMethodManager =(InputMethodManager )getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }

        }