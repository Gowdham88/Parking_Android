package com.pyrky_android.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pyrky_android.ExpandableListData;
import com.pyrky_android.R;
import com.pyrky_android.activity.HomeActivity;
import com.pyrky_android.activity.NearestLocMapsActivity;
import com.pyrky_android.adapter.ExpandableListAdapter;
import com.pyrky_android.adapter.NearestRecyclerAdapter;
import com.pyrky_android.map.ApiClient;
import com.pyrky_android.map.ApiInterface;
import com.pyrky_android.map.PlacesPOJO;
import com.pyrky_android.map.RecyclerViewAdapter;
import com.pyrky_android.map.ResultDistanceMatrix;
import com.pyrky_android.map.StoreModel;
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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener, AdapterView.OnItemClickListener
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener
        {
            private static List<String> placeidList;
            private static String placeidval;
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


            private static final String LOG_TAG = "ExampleApp";

            private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
            private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
            private static final String OUT_JSON = "/json";

            //------------ make your specific key ------------
            private static final String API_KEY = "AIzaSyAU9ShujnIg3IDQxtPr7Q1qOvFVdwNmWc4";

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
            AutoCompleteTextView autoCompView;
            String placeid;
            List<String> places= new ArrayList<String>();
            List<String> placesid = new ArrayList<String>();

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
//                PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                        getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

//        ImageView searchIcon = (ImageView)((LinearLayout)autocompleteFragment.getView()).getChildAt(0);
//
//        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search1));
        autoCompView= (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.auto_listview));
        autoCompView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
//        autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String str = (String) adapterView.getItemAtPosition(i);
//                autoCompView.setText(str);
//                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
//            }
//        });

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
//
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setHasFixedSize(true);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);

//        editText = (EditText) view.findViewById(R.id.editText);
        button = (Button) view.findViewById(R.id.filter_button);

//        editText.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);
                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
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
//                                    RecyclerViewAdapter adapterStores = new RecyclerViewAdapter(results, storeModels);
//                                    recyclerView.setAdapter(adapterStores);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String str = (String) adapterView.getItemAtPosition(position);

                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
//                String selection = (String) adapterView.getItemAtPosition(position);
//                int pos = -1;
//
//                for (int i = 0; i < placeidList.size(); i++) {
//                    if (placeidList.get(i).equals(selection)) {
//                        pos = i;
//                        placeid = placeidList.get(pos);
//                        Log.e("placeid",placeid);
//
//                        break;
//                    }
//                }



            }

            public ArrayList<String> autocomplete(String input) {
                ArrayList<String> resultList = null;
                ArrayList<String> resultListval = null;

                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                    StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                    sb.append("?key=" + API_KEY);
                    sb.append("&components=country:in");
                    sb.append("&input=" + URLEncoder.encode(input, "utf8"));

                    URL url = new URL(sb.toString());
                    Log.e("url", String.valueOf(url));


                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    // Load the results into a StringBuilder
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        jsonResults.append(buff, 0, read);
//
                    }
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "Error processing Places API URL", e);
//                    return resultList;
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error connecting to Places API", e);
//                    return resultList;
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

                try {
                    parseJSON(jsonResults);
                    // Create a JSON object hierarchy from the results
//                    JSONObject jsonObj = new JSONObject(jsonResults.toString());
//                    JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");


//                    GeoDataApi mGeoDataClient = null;
//                    mGeoDataClient.getPlaceById(placeId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
//                        @Override
//                        public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
//                            if (task.isSuccessful()) {
//                                PlaceBufferResponse places = task.getResult();
//                                Place myPlace = places.get(0);
////                                Log.i(TAG, "Place found: " + myPlace.getName());
//                                places.release();
//                            } else {
////                                Log.e(TAG, "Place not found.");
//                            }
//                        }
//                    });

                    // Extract the Place descriptions from the results
//                    resultList = new ArrayList<String>(predsJsonArray.length());
//                    resultListval=new ArrayList<String>(predsJsonArray.length());
//                    for (int i = 0; i < predsJsonArray.length(); i++) {
////                        System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
////                        System.out.println("============================================================");
//                        resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
//                        Log.e("resultList", String.valueOf(resultList));
////                        placeidval =predsJsonArray.getJSONObject(i).getString("place_id");
//                        resultListval.add(predsJsonArray.getJSONObject(i).getString("place_id"));
//                        Log.e("resultListval", String.valueOf(resultListval));
////
////                        placeidList.add(predsJsonArray.getJSONObject(i).getString("place_id"));
//
////
////                        String placeid = String.valueOf(predsJsonArray.getJSONObject(i).get("place_id"));
////                        Log.e("placeid", String.valueOf(placeid));
////                        resultListval.add(placeid);
//
//                    }


                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Cannot process JSON results", e);
                }

                return resultList;
            }


            class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
                private ArrayList<String> resultList;
                private ArrayList<String> resultListval;

                public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
                    super(context, textViewResourceId);
                }

                @Override
                public int getCount() {
                    return resultList.size();
                }

                @Override
                public String getItem(int index) {
                    return resultList.get(index);
                }

                @Override
                public Filter getFilter() {
                    Filter filter = new Filter() {
                        @Override
                        protected FilterResults performFiltering(CharSequence constraint) {
                            FilterResults filterResults = new FilterResults();
                            if (constraint != null) {
                                // Retrieve the autocomplete results.
//                                resultList = autocomplete(constraint.toString());
                                // Assign the data to the FilterResults
                                filterResults.values = resultList;
                                filterResults.count = resultList.size();
                            }
                            return filterResults;
                        }

                        @Override
                        protected void publishResults(CharSequence constraint, FilterResults results) {
                            if (results != null && results.count > 0) {
                                notifyDataSetChanged();
                            } else {
                                notifyDataSetInvalidated();
                            }
                        }
                    };
                    return filter;
                }
            }

            public void parseJSON(StringBuilder url)throws JSONException {

                JSONObject jsonObj = new JSONObject(url.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                for (int i = 0; i < predsJsonArray.length(); i++) {
//                        System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
//                        System.out.println("============================================================");
                    places.add(predsJsonArray.getJSONObject(i).getString("description"));
                    Log.e("places", String.valueOf(places));
                    places=autocomplete(predsJsonArray.getJSONObject(i).getString("description"));
//                        placeidval =predsJsonArray.getJSONObject(i).getString("place_id");
                    placesid.add(predsJsonArray.getJSONObject(i).getString("place_id"));
                    Log.e("placesid", String.valueOf(placesid));
//
//                        placeidList.add(predsJsonArray.getJSONObject(i).getString("place_id"));

//
//                        String placeid = String.valueOf(predsJsonArray.getJSONObject(i).get("place_id"));
//                        Log.e("placeid", String.valueOf(placeid));
//                        resultListval.add(placeid);

                }

            }

        }