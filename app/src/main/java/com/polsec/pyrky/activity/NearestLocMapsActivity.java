package com.polsec.pyrky.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.ViewImage.ViewImageActivity;
import com.polsec.pyrky.activity.ar.ArNavActivity;
import com.polsec.pyrky.activity.ar.RuntimePermissionActivity;
import com.polsec.pyrky.adapter.CarouselDetailMapAdapter;
import com.polsec.pyrky.adapter.Carouselfirebaseadapter;
import com.polsec.pyrky.fragment.HomeFragment;
import com.polsec.pyrky.fragment.TrackGPS;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.pojo.Camera;
import com.polsec.pyrky.pojo.NearestDestnetionData;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.Constants;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class NearestLocMapsActivity extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, DiscreteScrollView.OnItemChangedListener {

    private static final long DEFAULT_MARKER_LENGTH = 2000;
    Context context = getActivity();

    CarouselDetailMapAdapter mNearestrecyclerAdapter;
    Carouselfirebaseadapter mNearestrecyclerAdapter1;
//    RecyclerView mNearestPlaceRecycler;
    DiscreteScrollView mNearestPlaceRecycler;
    int mListPosition = 0;


    Location mCurrentLoc = new Location("");
    Location mNearestLocations = new Location("");

    ArrayList<String> mAccurateDistancesString = new ArrayList<>();
    ArrayList<Double> mLocationDistances = new ArrayList<>();
    ArrayList<Double> mLocationDistancesmtrs = new ArrayList<>();
    ArrayList<String> mCalculateDistances = new ArrayList<>();
    ArrayList<String> mCameraLat = new ArrayList<>();
    ArrayList<String> mCameraLong = new ArrayList<>();
    ArrayList<String> mCameraImageUrl = new ArrayList<>();
    ArrayList<Double> distancesmtrscurrentmap = new ArrayList<>();
    ArrayList<String> distancescurrentarrmap = new ArrayList<>();
    ArrayList<String> mCameraLocName = new ArrayList<>();
    ArrayList<String> mCameraId = new ArrayList<>();
    ArrayList<HashMap<String, Object>> rules = new ArrayList<HashMap<String, Object>>();
    private Boolean isPopUpShowing = false;
    double mAccurateDistance;
    GoogleMap Mmap;
    List<Address> yourAddresses;
    String yourplace,area;
    String documentID;
    double parkingSpaceRating;
    Boolean protectCar,bookingStatus;
    String lat,longi;
    String mBySearch, mByCarousel;
    LatLng laln;
    Location mLocation;
    List<Camera> datalist = new ArrayList<Camera>();
    RelativeLayout mBackIcon;
    TextView TitlaTxt;
    String mLat,mLongi,PlaceName, latt,Longg,plcname,Imageurl;
    int distance;
    String Nameval="home";
    String Nameval1="carousel",mapLat,mapLongi,cameraid,ParkingType;
    Camera camera;
    String parkytype,mUid,docid,CameraId;
    private InfiniteScrollAdapter infiniteAdapter;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String docIdnew;
    Map<String, Object> bookingid = new HashMap<>();

    Map<String, Object> bookingid1=new HashMap<>();
    Boolean val;
    RelativeLayout BackImgRelay;
    private GoogleApiClient mGoogleApiClient;
    private android.support.v7.app.AlertDialog dialog;
    Boolean isBookedAny = false;
    String documentIDs;
    private TrackGPS mCurrentGpsLoc;
    double mCurLocLat, mCurLocLong,curLat,curLong;
    HashMap<String, Object> mrlslist;
    List<NearestDestnetionData> mNearestDataList = new ArrayList<NearestDestnetionData>();

    List<Address> mCurLocAddress = null;
    private SparseIntArray mDeniedKeys;
    private static final int REQUEST_CONTACT_PERMISSIONS = 101;
    private static final int REQUEST_CAMERA_PERMISSIONS = 102;
    private static final int REQUEST_EXTERNAL_PERMISSIONS = 103;
    // ===============

    // Group permission request code
    private static final int REQUEST_GROUP_PERMISSIONS = 104;


    public static NearestLocMapsActivity newInstance(String latt, String longitude, String carousel, int adapterPosition, String Placename, int distanceval, String img, String mCameraID, Map<String, Object> parkingRules, String parkingTypes) {

        NearestLocMapsActivity home = new NearestLocMapsActivity();

        Bundle args = new Bundle();
        args.putString("latt", latt);
        args.putString("longg", longitude);
        args.putString("carousel", carousel);
        args.putInt("adapterPosition", adapterPosition);
        args.putString("place", Placename);
        args.putInt("distance",distanceval);
        args.putString("imgurl",img);
        args.putString("cameraid",mCameraID);
        args.putString("parkingtype",parkingTypes);
        args.putSerializable("rulslist",(Serializable) parkingRules);

        home.setArguments(args);
        return home;
    }
    @Override
    public void onResume() {
        super.onResume();

//        mGoogleApiClient = new GoogleApiClient.Builder(context)
//                .addApi(Places.GEO_DATA_API)
//                .enableAutoManage((FragmentActivity) getActivity(), 1, NearestLocMapsActivity.this)
//                .addConnectionCallbacks(NearestLocMapsActivity.this)
//                .build();

//        mNearestPlaceRecycler.setVisibility(View.VISIBLE);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_nearest_loc_maps, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getMapAsync(NearestLocMapsActivity.this);

        mDeniedKeys = new SparseIntArray();

        ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.GONE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        mNearestPlaceRecycler =  view.findViewById(R.id.nearest_places_recycler);
//        mNearestPlaceRecycler.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUid = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        docid=PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_DOCUMENTID);

        mBackIcon = (RelativeLayout) view.findViewById(R.id.back_icon);
        TitlaTxt = (TextView) view. findViewById(R.id.extra_title);
        TitlaTxt.setText("Map");
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                refreshActivity();
                getActivity().onBackPressed();
            }
        });

//        TrackGPS trackGps = new TrackGPS(context);
//
//        if (trackGps.canGetLocation()) {
//
//
//            Log.e("curLat", String.valueOf(curLat));
//            Log.e("curLong =", String.valueOf(curLong));
//        }

        String[] field = {"SecurityRating","parkingTypes","carCategory"};
        String[] value = {"5 stars","Paid street parking","Small"};

        HashMap<String,ArrayList<String>> keyValue = new HashMap<>();
        ArrayList<String> fields = new ArrayList<>();
        fields.add("SecurityRating");
        fields.add("parkingTypes");
        fields.add("carCategory");

        ArrayList<String> parkingTypes = new ArrayList<>();
        if (Constants.PARKING_TYPES.size()>0){
            parkingTypes.addAll(Constants.PARKING_TYPES);
        }

        ArrayList<String> carCategory = new ArrayList<>();
        if (Constants.CAR_CATEGORY.size()>0){
            carCategory.addAll(Constants.CAR_CATEGORY);
        }

        ArrayList<String> sRatings = new ArrayList<>();
        if (Constants.SECURITY_RATINGS.size()>0){
            sRatings.addAll(Constants.SECURITY_RATINGS);
        }
        keyValue.put(field[0],sRatings);
        keyValue.put(field[1],parkingTypes);
        keyValue.put(field[2],carCategory);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Bundle bundle = this.getArguments();
        if(bundle!=null){
            mBySearch =bundle.getString("value");
            mByCarousel =bundle.getString("carousel");

            if(Nameval.equals(mBySearch)){

                mLat = bundle.getString("latitude").trim();
                mLongi = bundle.getString("longitude").trim();
                PlaceName= bundle.getString("place").trim();
                ParkingType= bundle.getString("parkingtype").trim();
                Log.e("hlattitude", String.valueOf(mLat));
                Log.e("hlongitude", String.valueOf(mLongi));
                Log.e("hplace", String.valueOf(PlaceName));
                mNearestPlaceRecycler.setVisibility(View.VISIBLE);

                getCurrentLocation(mLat,mLongi);

                if (parkingTypes.size() < 1 && carCategory.size() < 1 && sRatings.size() < 1){
                    Query query = db.collection("camera");
                    datalist.clear();
                    mNearestDataList.clear();
                    loadCameraLocation(query);
                }
                else{

                    for (String key : field) {

                        for (String values : keyValue.get(key)) {
                            datalist.clear();
                            mNearestDataList.clear();
                            Query query = db.collection("camera").whereEqualTo(key, values);
                            loadCameraLocation(query);

                        }
                    }
                }
            }
            else if(Nameval1.equals(mByCarousel)){

                mLat = bundle.getString("latt").trim();
                mLongi = bundle.getString("longg").trim();
                mListPosition = bundle.getInt("adapterPosition");
                PlaceName= bundle.getString("place").trim();
                distance=bundle.getInt("distance");
                Imageurl=bundle.getString("imgurl");
                CameraId=bundle.getString("cameraid");
                mrlslist= (HashMap<String, Object>) bundle.getSerializable("rulslist");
                ParkingType=bundle.getString("parkingtype");

                Log.e("lattitude", String.valueOf(mLat));
                 Log.e("longitude", String.valueOf(mLongi));
                Log.e("plc", String.valueOf(mListPosition));
                Log.e("distance", String.valueOf(mrlslist));

                mNearestPlaceRecycler.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        showDialog1(mrlslist,CameraId, mLat,mLongi,Imageurl,PlaceName);
                    }
                }, DEFAULT_MARKER_LENGTH);



                getCurrentLocation(mLat,mLongi);

//                mNearestPlaceRecycler.setOrientation(DSVOrientation.HORIZONTAL);
//                mNearestPlaceRecycler.addOnItemChangedListener(NearestLocMapsActivity.this);
//                mNearestrecyclerAdapter1 = new Carouselfirebaseadapter(getActivity(), Imageurl, latt, Longg,plcname,distance);
//                mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter1);
////                                    mNearestPlaceRecycler.scrollToPosition(mListPosition);
//                mNearestrecyclerAdapter1.notifyDataSetChanged();
//                mNearestPlaceRecycler.setItemTransformer(new ScaleTransformer.Builder()
//                        .setMinScale(0.8f)
//                        .build());
            }
            else{

            }


        }





//        abll
/*
        for (String key : field){

            for (String values : keyValue.get(key)){
                Query query = db.collection("camera").whereEqualTo(key,values);

                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Camera comment = document.toObject(Camera.class);
                            Log.d("Response", document.toString());
                        }

                    }
                });


            }

        }
*/

        return view;
        }

    private void refreshActivity() {

        HomeFragment newFragment = new HomeFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.main_frame_layout, newFragment).commit();

    }


    private void loadCameraLocation(Query query){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query docRef = db.collection("camera").orderBy("cameraLat", Query.Direction.ASCENDING);
        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() < 1) {
                            return;
                        }

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                            camera = document.toObject(Camera.class);
                            datalist.add(camera);

                            Log.e("dbbd", String.valueOf(document.getData()));
//



                            mLocationDistancesmtrs.clear();
                            mCameraLat.clear();
                            mLocationDistances.clear();
                            mCameraLong.clear();
                            mAccurateDistancesString.clear();
                            mCameraImageUrl.clear();
                            mCameraLocName.clear();
                            mCameraId.clear();
                            rules.clear();

                            mNearestDataList.clear();

                            for (int i = 0; i < datalist.size(); i++) {
                                mCurrentLoc.setLatitude(Double.parseDouble(mLat));
                                mCurrentLoc.setLongitude(Double.parseDouble(mLongi));

                                mNearestLocations.setLatitude(Double.parseDouble(datalist.get(i).getCameraLat()));
                                mNearestLocations.setLongitude(Double.parseDouble(datalist.get(i).getCameraLong()));

                                double locationDistance = mCurrentLoc.distanceTo(mNearestLocations);
                                mLocationDistances.add(locationDistance);
                                Log.e("distancemtrsmap", String.valueOf(mLocationDistances));


                                //Calculate distances by 1000 to show to the users
                                mAccurateDistance = mCurrentLoc.distanceTo(mNearestLocations) / 1000;
                                mAccurateDistancesString.add(String.valueOf(mAccurateDistance));
                                Log.e("distancemap", String.valueOf(mAccurateDistancesString));




                                if (locationDistance < 2500) {
//
                                    NearestDestnetionData nearestdata = new NearestDestnetionData();
                                    nearestdata.setLocationDistance(locationDistance);
                                    nearestdata.setCameraLat(datalist.get(i).getCameraLat());
                                    nearestdata.setCameraLong(datalist.get(i).getCameraLong());
                                    nearestdata.setParkingTypes(datalist.get(i).getParkingTypes());
                                    nearestdata.setCameraID(datalist.get(i).getCameraID());
                                    nearestdata.setParkingRules(datalist.get(i).getParkingRules());
                                    nearestdata.setCameraLocationName(datalist.get(i).getCameraLocationName());
                                    nearestdata.setCameraImageUrl(datalist.get(i).getCameraImageUrl());


                                    mNearestDataList.add(nearestdata);

                                    for (int j = 0; j < mNearestDataList.size(); j++) {

                                        Collections.sort(mNearestDataList, new Comparator<NearestDestnetionData>() {
                                            @Override
                                            public int compare(NearestDestnetionData lhs, NearestDestnetionData rhs) {
                                                return lhs.getLocationDistance().compareTo(rhs.getLocationDistance());
                                            }
                                        });


                                        Log.e("sortlist", String.valueOf(mNearestDataList.get(j).getLocationDistance()));


                                        mNearestPlaceRecycler.setOrientation(DSVOrientation.HORIZONTAL);
                                        mNearestPlaceRecycler.addOnItemChangedListener(NearestLocMapsActivity.this);
                                        mNearestrecyclerAdapter = new CarouselDetailMapAdapter(getActivity(),mNearestDataList,distance,NearestLocMapsActivity.this);
                                        mNearestPlaceRecycler.setAdapter(mNearestrecyclerAdapter);
                                        mNearestPlaceRecycler.scrollToPosition(mListPosition);
                                        mNearestrecyclerAdapter.notifyDataSetChanged();
                                        mNearestPlaceRecycler.setItemTransformer(new ScaleTransformer.Builder()
                                                .setMinScale(0.8f)
                                                .build());
                                    }




//                                            parkytype = datalist.get(i).getParkingType();



//                                    onItemChanged(mCameraLat.get(0), mCameraLong.get(0), mCameraId.get(0),datalist.get(i).getParkingTypes(),rules.get(i));

                if((!datalist.get(i).getParkingTypes().equals(null))||(!datalist.get(i).getParkingTypes().isEmpty())){

                    if (datalist.get(i).getParkingTypes().equals("Free street parking")) {
                        LatLng sydney = new LatLng(Double.parseDouble(datalist.get(i).getCameraLat()), Double.parseDouble(datalist.get(i).getCameraLong()));
                        Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_free_marker));

                    } else {
                        LatLng sydney = new LatLng(Double.parseDouble(datalist.get(i).getCameraLat()), Double.parseDouble(datalist.get(i).getCameraLong()));
                        Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_paid_marker));
                    }

                }

                hideProgressDialog();
                                }
//

                            }
                        }

                    }
                });

    }

//    @Override
//    public void onClickimageButton(final int position, final String actionLikeButtonClicked, final String s, final String s1, String mapvalues, String s2, String s3) {
//
//
////        Toast.makeText(NearestLocMapsActivity.this, "click", Toast.LENGTH_SHORT).show();
////        mLat= String.valueOf(s);
////        mLongi= String.valueOf(s1);
////        valuestr=mapvalues;
////        PlaceNameval=s2;
////        Mmap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLongi))));
////        Log.e("maplattitude", String.valueOf(mLat));
////                Log.e("maplongitude", String.valueOf(mLongi));
////        Log.e("valuemap",valuelat+valuelongi+valuestr);
//
//    }

    private void getCurrentLocation(String mLat, String mLongi) {


            Double lat = Double.valueOf(mLat);
            Double lng = Double.valueOf(mLongi);
            mCurLocLat = Double.parseDouble(mLat);
            mCurLocLong = Double.parseDouble(mLongi);


            try {
                Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                mCurLocAddress = geo.getFromLocation(lat, lng, 1);
                if (mCurLocAddress.isEmpty()) {
                } else {
                    if (mCurLocAddress.size() > 0) {
                        String address = mCurLocAddress.get(0).getAddressLine(0);
                        double latt1 = mCurLocAddress.get(0).getLatitude();
                        double longi1 = mCurLocAddress.get(0).getLongitude();
                        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = mCurLocAddress.get(0).getLocality();
                        String state = mCurLocAddress.get(0).getAdminArea();
                        String country = mCurLocAddress.get(0).getCountryName();
                        String postalCode = mCurLocAddress.get(0).getPostalCode();
                        String knownName = mCurLocAddress.get(0).getFeatureName();
                        String address1 = (address + "," + city + "," + state + "," + country + "," + postalCode);
//                        Toast.makeText(getActivity(), address1, Toast.LENGTH_SHORT).show();
                        Log.e("address1", address1);
                        mLat= String.valueOf(mCurLocAddress.get(0).getLatitude());
                        mLongi=String.valueOf( mCurLocAddress.get(0).getLongitude());




                        curLat = mCurLocAddress.get(0).getLatitude();
                        curLong = mCurLocAddress.get(0).getLongitude();
                        Log.e("curLat", String.valueOf(curLat));
                        Log.e("curLong", String.valueOf(curLong));

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }



    }

    private void onItemChanged(String lat, String lng, String cameraId, String parkingType, HashMap<String, Object> rules) {
        mapLat = lat.trim();
        mapLongi = lng.trim();
        cameraid = cameraId;
        Log.e("mapLongi",mapLat);
        Log.e("mapLat",mapLongi);
        Log.e("cameraid", String.valueOf(rules));

        LatLng sydney = new LatLng(Double.parseDouble(mapLat), Double.parseDouble(mapLongi));

//            LatLng sydney = new LatLng(Double.parseDouble(mapLat), Double.parseDouble(mapLongi));
//             if (parkingType.equals("Free street parking")){

//                 int height = 70;
//                 int width = 40;
//                 BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker);
//                 Bitmap b=bitmapdraw.getBitmap();
//                 Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//                 Mmap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
//              Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_free_marker));
//            }else{
//            Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.paid));

//                 int height = 70;
//                 int width = 50;
//                 BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.paid);
//                 Bitmap b=bitmapdraw.getBitmap();
//                 Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//                 Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_paid_marker));
//              }
//            Mmap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
             Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,14 ));



                 Mmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                     @Override
                     public boolean onMarkerClick(Marker marker) {
                         showDialog(marker,cameraid,rules,mNearestDataList.get(mNearestPlaceRecycler.getCurrentItem()).getCameraImageUrl());
                         mNearestPlaceRecycler.setVisibility(View.INVISIBLE);
                         return false;

                     }

                 });

//            Mmap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

            mNearestPlaceRecycler.setVisibility(View.VISIBLE);
        }

    private void pulseMarker(final Bitmap markerIcon, final Marker marker, final long onePulseDuration) {
        final Handler handler = new Handler();
        final long startTime = System.currentTimeMillis();

        final Interpolator interpolator = new CycleInterpolator(1f);
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / onePulseDuration);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(markerIcon));
                handler.postDelayed(this, 16);
            }
        });
    }

    public Bitmap scaleBitmap(Bitmap bitmap, float scaleFactor) {
        final int sizeX = Math.round(bitmap.getWidth() * scaleFactor);
        final int sizeY = Math.round(bitmap.getHeight() * scaleFactor);
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, sizeX, sizeY, false);
        return bitmapResized;
    }
    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = adapterPosition;
        onItemChanged(mNearestDataList.get(positionInDataSet).getCameraLat(), mNearestDataList.get(positionInDataSet).getCameraLong(), mNearestDataList.get(positionInDataSet).getCameraID(),mNearestDataList.get(positionInDataSet).getParkingTypes(), mNearestDataList.get(adapterPosition).getParkingRules());


    }



    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
//        mNearestPlaceRecycler.setVisibility(View.VISIBLE);
        LatLng latLng = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLongi));
//        Mmap.clear();

        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.5f));
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        Mmap = gMap;
//        Mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        mNearestPlaceRecycler.setVisibility(View.VISIBLE);
        // Load custom marker icon


        LatLng sydney = new LatLng(Double.parseDouble(mLat),Double.parseDouble(mLongi));


        if(ParkingType.equals("Free street parking")){
            Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_free_marker));
            Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));


//
        }else if (ParkingType.equals("Paid street parking")){
            Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_paid_marker));
            Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
//            showDialog1(CameraId, mLat,mLongi,Imageurl,PlaceName);
        }

        else if (ParkingType.equals("Paid parking")){
            Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_paid_marker));
            Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
//            showDialog1(CameraId, mLat,mLongi,Imageurl,PlaceName);
        }
        else {

//            Mmap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_paid_marker));
//            Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

        }

        Mmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                showDialog1(mrlslist,CameraId, mLat,mLongi,Imageurl,PlaceName);
                mNearestPlaceRecycler.setVisibility(View.INVISIBLE);
                return false;

            }

        });

        try {
            Mmap.setMyLocationEnabled(false);
        } catch (SecurityException se) {

        }
//        mNearestPlaceRecycler.setVisibility(View.VISIBLE);

    }

    public void showDialog(Marker m, String cameraid, HashMap<String, Object> listofparkingRules, String cameraImageUrl){

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.ruls_layout , null);
        final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();

            TextView ViewTxt,NavigateTxt,rule1,rule2,rule3,rule4;

        ViewTxt=promptView.findViewById(R.id.view_txt);
        NavigateTxt=promptView.findViewById(R.id.navi_txt);

        rule1 = promptView.findViewById(R.id.rule1_txt);
        rule2 = promptView.findViewById(R.id.rule2_txt);
        rule3 =promptView.findViewById(R.id.rule3_txt);
        rule4 =promptView.findViewById(R.id.rule4_txt);

        if((!listofparkingRules.equals(null)) || (!listofparkingRules.isEmpty())){
            rule1.setText((CharSequence) listofparkingRules.get("0"));
            rule2.setText((CharSequence) listofparkingRules.get("1"));
            rule3.setText((CharSequence) listofparkingRules.get("2"));
            rule4.setText((CharSequence) listofparkingRules.get("3"));
        }



        Geocoder geocoder;
////
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            yourAddresses= geocoder.getFromLocation(Double.parseDouble(String.valueOf(m.getPosition().latitude)),Double.parseDouble(String.valueOf(m.getPosition().longitude)) , 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (yourAddresses.size() > 0)
        {
            yourplace = yourAddresses.get(0).getAddressLine(0);
            String yourCity = yourAddresses.get(0).getAddressLine(1);
            String yourCountry = yourAddresses.get(0).getAddressLine(2);


        }

        ViewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String maltval= String.valueOf(m.getPosition().latitude);
                String mlongival=String.valueOf(m.getPosition().longitude);

                Intent intent=new Intent(getActivity(), ViewImageActivity.class);
                intent.putExtra("latitude",maltval.trim());
                intent.putExtra("longitude",mlongival.trim());
                intent.putExtra("place",yourplace);
                intent.putExtra("cameraid",cameraid);
                intent.putExtra("cameraImageUrl",cameraImageUrl);
                intent.putExtra("recycler","recyclervalue");

                Log.e("lattitude", String.valueOf(m.getPosition().latitude));
                Log.e("longitude", String.valueOf(m.getPosition().longitude));
                Log.e("yourplace",yourplace);
                Log.e("cameraid",cameraid);
                Log.e("cameraidimg",cameraImageUrl);
                startActivity(intent);
                alertD.cancel();
                mNearestPlaceRecycler.setVisibility(View.VISIBLE);
            }
        });
        NavigateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showBottomSheet(m.getPosition().latitude,m.getPosition().longitude, yourplace, cameraid);
                lat= String.valueOf(m.getPosition().latitude);
                longi= String.valueOf(m.getPosition().longitude);



//                SaveData(lat,longi,yourplace);
                alertD.cancel();
                mNearestPlaceRecycler.setVisibility(View.VISIBLE);
            }
        });
        alertD.setView(promptView);
        WindowManager.LayoutParams params = alertD.getWindow().getAttributes();
        params.y = 200;
        alertD.getWindow().setAttributes(params);
        alertD.getWindow().setDimAmount(0.0f);
        alertD.getWindow().setGravity(Gravity.TOP);
        alertD.show();

        alertD.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mNearestPlaceRecycler.setVisibility(View.VISIBLE);
            }
        });

    }

    public void showDialog1(HashMap<String, Object> mrlslist,String cameraId, String mLat, String mLongi, String Imageurl1, String place){
        mNearestPlaceRecycler.setVisibility(View.INVISIBLE);

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.ruls_layout , null);
        final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();

        TextView ViewTxt,NavigateTxt,rule1,rule2,rule3,rule4;

        ViewTxt=promptView.findViewById(R.id.view_txt);
        NavigateTxt=promptView.findViewById(R.id.navi_txt);

        rule1 = promptView.findViewById(R.id.rule1_txt);
        rule2 = promptView.findViewById(R.id.rule2_txt);
        rule3 =promptView.findViewById(R.id.rule3_txt);
        rule4 =promptView.findViewById(R.id.rule4_txt);

        if((!mrlslist.equals(null)) || (!mrlslist.isEmpty())){
            rule1.setText((CharSequence) mrlslist.get("0"));
            rule2.setText((CharSequence) mrlslist.get("1"));
            rule3.setText((CharSequence) mrlslist.get("2"));
            rule4.setText((CharSequence) mrlslist.get("3"));
        }


//
//        Geocoder geocoder;
//////
//        geocoder = new Geocoder(getActivity(), Locale.getDefault());
//        try {
//            yourAddresses= geocoder.getFromLocation(String.valueOf(mLat),mLongi) , 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (yourAddresses.size() > 0)
//        {
//            yourplace = yourAddresses.get(0).getAddressLine(0);
//            String yourCity = yourAddresses.get(0).getAddressLine(1);
//            String yourCountry = yourAddresses.get(0).getAddressLine(2);
//
//
//        }

        ViewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), ViewImageActivity.class);
                intent.putExtra("latitude",mLat);
                intent.putExtra("longitude",mLongi);
                intent.putExtra("cameraid",cameraId);
                intent.putExtra("cameraImageUrl",Imageurl1);
                intent.putExtra("place",place);
//                intent.putExtra("recycler","firebasevalue");
//
                Log.e("lattitude", String.valueOf(mLat));
                Log.e("longitude", String.valueOf(mLongi));
//                Log.e("cameraid",cameraid);
                Log.e("cameraidimg",Imageurl1);
                Log.e("yourplace",place);
                startActivity(intent);
                alertD.cancel();
//                mNearestPlaceRecycler.setVisibility(View.VISIBLE);
            }
        });
        NavigateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showBottomSheet(Double.parseDouble(mLat),Double.parseDouble(mLongi),place,cameraId);

                Log.e("latval", String.valueOf(Double.parseDouble(mLat)));
                Log.e("longival", String.valueOf(Double.parseDouble(mLongi)));
//                NearestLocMapsActivity.this.lat = String.valueOf(m.getPosition().latitude);
//                longi= String.valueOf(m.getPosition().longitude);

//                SaveData(lat,longi,yourplace);
                alertD.cancel();
//                mNearestPlaceRecycler.setVisibility(View.VISIBLE);
            }
        });
        alertD.setView(promptView);
        WindowManager.LayoutParams params = alertD.getWindow().getAttributes();
        params.y = 200;
        alertD.getWindow().setAttributes(params);
        alertD.getWindow().setDimAmount(0.0f);
        alertD.getWindow().setGravity(Gravity.TOP);
        alertD.show();

        alertD.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
//                mNearestPlaceRecycler.setVisibility(View.VISIBLE);
            }
        });

    }

    private void SaveData(double latitude, double longitude, String yourplace, String cameraid) {


        final String uid = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);


        parkingSpaceRating=0;
        protectCar=false;
        bookingStatus=true;
//          locationTxt=Location_Txt.getText().toString();
//        String photoURL = PreferencesHelper.getPreference(this, PreferencesHelper.PREFERENCE_PHOTOURL);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Map<String, Boolean> likeData = new HashMap<>();
        likeData.put(uid, false);
        documentID="";

        Booking bookingdata = new Booking(uid,String.valueOf(latitude),String.valueOf(longitude),yourplace,getPostTime(),bookingStatus,cameraid,documentID,parkingSpaceRating,protectCar);


        db.collection("Bookings").add(bookingdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                documentID = documentReference.getId();

                PreferencesHelper.setPreference(getActivity(), PreferencesHelper.PREFERENCE_DOCUMENTIDNEWONE,documentID);
//                PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_DOCMENTID, documentID);

                Booking bookingdata = new Booking(uid,String.valueOf(latitude),String.valueOf(longitude),yourplace,getPostTime(),bookingStatus,cameraid,documentID,parkingSpaceRating,protectCar);
                Map<String, Object> docID = new HashMap<>();
                docID.put("documentID", documentID);


                db.collection("Bookings").document(documentID).update(docID).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {




                        Map<String, Boolean> likeData1 = new HashMap<>();
                        likeData1.put( documentID, true);

                        Map<String, Map<String, Boolean>> likeData2 = new HashMap<>();
                        likeData2.put( "Booking_ID", likeData1);



                        FirebaseFirestore db = FirebaseFirestore.getInstance();


                        db.collection("users").document(uid)
                                .set(likeData2, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
//


            }
        });
    }
//

    private void showBottomSheet(double latitude, double longitude, String cameraid, String yourPlace) {


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View bottomSheetView = factory.inflate(R.layout.ar_pyrky_bottomsheet, null);
        TextView map = bottomSheetView.findViewById(R.id.maps_title);
        TextView pyrky = bottomSheetView.findViewById(R.id.pyrky_title);
        TextView cancel = bottomSheetView.findViewById(R.id.cancel_txt);

        if (Constants.IS_AR_ENABLED){

        }else{
           pyrky.setVisibility(View.GONE);
        }
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheetView.getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) bottomSheetView.getParent()).setBackgroundColor(Color.TRANSPARENT);



        map.setOnClickListener(view -> {

            String value="map";

            makeAlreadyBookedAlert(true,latitude,longitude,yourPlace,cameraid,value);

            bottomSheetDialog.dismiss();

        });

        pyrky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                        || (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        || (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    askRequestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            R.string.enable_permission, REQUEST_GROUP_PERMISSIONS);


                    String value="pyrky";
                    makeAlreadyBookedAlert(true,latitude,longitude,yourPlace,cameraid,value);

                } else {
                    groupPermissionEnable();
                }



//                SaveData(latitude,longitude, yourPlace,cameraid);

//

                bottomSheetDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void groupPermissionEnable() {
        //TODO Add your code
        Toast.makeText(getActivity(), "Group runtime permission enable successfully...", Toast.LENGTH_SHORT).show();



    }


    private void popup(String valuedoc, String key, Boolean bookingRequest, double latitude, double longitude, String cameraid, String yourPlace, String value) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.status_alert_lay, null);
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
        alertDialog.setView(deleteDialogView);
        TextView ok = deleteDialogView.findViewById(R.id.ok_button);
        TextView cancel = deleteDialogView.findViewById(R.id.cancel_button);

        final android.support.v7.app.AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Boolean> likeData1 = new HashMap<>();
                likeData1.put( key, false);

                Map<String, Map<String, Boolean>> likeData2 = new HashMap<>();
                likeData2.put( "Booking_ID", likeData1);



                FirebaseFirestore db = FirebaseFirestore.getInstance();


                db.collection("users").document(mUid)
                        .set(likeData2, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");

//                                docIdnew=PreferencesHelper.getPreference(getActivity(),PreferencesHelper.PREFERENCE_DOCUMENTIDNEW);
                                PopUpprotectcar(bookingRequest,latitude,longitude,yourPlace,value);
                           isBookedAny = false;
                                if (bookingRequest){
                                    makeAlreadyBookedAlert(true,latitude,longitude, yourPlace, cameraid, value);
                                }else{
                                    makeAlreadyBookedAlert(false,latitude,longitude, yourPlace, cameraid, value);
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });


//                Map<String, Object> likeupdate = new HashMap<>();
//                likeupdate.put( "bookingStatus", false);
//
//                db.collection("Bookings").document(mUid)
//                        .update(likeupdate)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d(TAG, "DocumentSnapshot successfully written!");
//
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error writing document", e);
//                            }
//                        });

//                PopUpprotectcar(documentID);

                alertDialog1.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    private void PopUpprotectcar(Boolean bookingRequest, double latitude, double longitude, String yourPlace, String value) {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.protetcar_alert, null);
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
        alertDialog.setView(deleteDialogView);
        TextView ok = deleteDialogView.findViewById(R.id.ok_button);
        TextView cancel = deleteDialogView.findViewById(R.id.cancel_button);
        final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.parking_alert );

        final android.support.v7.app.AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(value=="map")
                {

//                    Toast.makeText(getActivity(), "map", Toast.LENGTH_SHORT).show();
                    bookAndNavigate(latitude, longitude);
                }
                else {

//                    Toast.makeText(getActivity(), "pyrky", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), ArNavActivity.class);
//                try {
                    intent.putExtra("SRC", "Current Location");
                    intent.putExtra("DEST", "Some Destination");
                    intent.putExtra("SRCLATLNG", curLat + "," + curLong);
                    intent.putExtra("DESTLATLNG", latitude + "," + longitude);
                    getActivity().overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
                    startActivity(intent);

//                }catch (NullPointerException npe){
//
//                    Log.d(TAG, "onClick: The IntentExtras are Empty");
//                }

                }



                    mp.start();
//                isBookedAny = false;
//                if (bookingRequest){
//                    makeAlreadyBookedAlert(true,latitude,longitude, yourPlace, yourPlace);
//                }else{
//                    makeAlreadyBookedAlert(false,latitude,longitude, yourPlace, yourPlace);
//                }
                documentIDs =PreferencesHelper.getPreference(getActivity(),PreferencesHelper.PREFERENCE_DOCUMENTIDNEWONE);
                Log.e("doc",documentIDs);
                protectCar(true,false,documentIDs);
                alertDialog1.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                bookAndNavigate(latitude, longitude);

                if(value=="map")
                {

                    Toast.makeText(getActivity(), "map", Toast.LENGTH_SHORT).show();
                    bookAndNavigate(latitude, longitude);
                }
                else {

                    Toast.makeText(getActivity(), "pyrky", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), ArNavActivity.class);
//                    try {
                        intent.putExtra("SRC", "Current Location");
                        intent.putExtra("DEST", "Some Destination");
                        intent.putExtra("SRCLATLNG", curLat + "," + curLong);
                        intent.putExtra("DESTLATLNG", latitude + "," + longitude);
                        getActivity().overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
                        startActivity(intent);
//
//                    }catch (NullPointerException npe){
//
//                        Log.d(TAG, "onClick: The IntentExtras are Empty");
//                    }
//
//
//                    Intent prkyintent=new Intent(getActivity(), ArNavActivity.class);
//
//                    startActivity(prkyintent);
                }


//                isBookedAny = false;
//
//                if (bookingRequest){
//                    makeAlreadyBookedAlert(false,latitude,longitude, yourPlace, yourPlace);
//                }else{
//                    makeAlreadyBookedAlert(false,latitude,longitude, yourPlace, yourPlace);
//                }
//
//                protectCar(false,true);
                alertDialog1.dismiss();
            }
        });



        alertDialog1.setCanceledOnTouchOutside(false);
        try {
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        alertDialog1.getWindow().setLayout((int) Utils.convertDpToPixel(228,getActivity()),(int)Utils.convertDpToPixel(220,getActivity()));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());
//        lp.height=200dp;
//        lp.width=228;
        lp.gravity = Gravity.CENTER;
//        lp.windowAnimations = R.style.DialogAnimation;
        alertDialog1.getWindow().setAttributes(lp);
        alertDialog1.show();
    }

    private void protectCar(Boolean protectCar, Boolean bookingStatus, String docIdnew){
        final Map<String, Object> protectdata = new HashMap<>();
        protectdata.put("protectCar", protectCar);
        protectdata.put("bookingStatus", bookingStatus);



        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("Bookings").document(docIdnew)
                .update(protectdata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");


//                            DocumentReference washingtonRef = db.collection("users").document(uid);
//
//                            washingtonRef
//                                    .update("Booking_ID",likeData1)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Log.w(TAG, "Error updating document", e);
//                                        }
//                                    });



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void makeAlreadyBookedAlert(Boolean bookingRequest, double latitude, double longitude, String cameraid, String yourPlace, String value){
        final FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    DocumentReference docRef = db.collection("users").document(mUid);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if(document.contains("Booking_ID")){
                                        bookingid = document.getData();

                                        bookingid1= (Map<String, Object>) bookingid.get("Booking_ID");

                                        for (Map.Entry<String, Object> bookingEntry : bookingid1.entrySet()){
                                            Boolean value = (Boolean) bookingEntry.getValue();
                                            if (value){
                                                isBookedAny = true;
                                                break;
                                            }
                                        }

                                        if (isBookedAny){
                                            for (Map.Entry<String, Object> entry : bookingid1.entrySet()) {
                                                System.out.println(entry.getKey() + " = " + entry.getValue());

                                                Boolean val = (Boolean) entry.getValue();
//                                                                                            Log.e("values", booking_id);
//
                                                if (val) {

//                                                Toast.makeText(getActivity(), values, Toast.LENGTH_SHORT).show();
                                                    String valuedoc=PreferencesHelper.getPreference(getActivity(),PreferencesHelper.PREFERENCE_DOCUMENTID);

                                                    popup(valuedoc,entry.getKey(),bookingRequest,latitude,longitude,yourPlace,cameraid,value);
                                                    break;

//                                Toast.makeText(getActivity(), followcount, Toast.LENGTH_SHORT).show();
                                                }else{
//                                                    Toast.makeText(getActivity(), "False value", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                    }

                                        else{


                                            if (bookingRequest){

                                                SaveData(latitude, longitude, yourPlace,cameraid);
//                                                bookAndNavigate(latitude,longitude,yourPlace,cameraid);
                                            }
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                    }

                                    }

                                } else {
//                        Log.d(TAG, "No such document");

                                }
                            } else {
//                    Log.d(TAG, "get failed with ", task.getException());

                            }
                        }
                    });

//                    Toast.makeText(ViewImageActivity.this, "ok", Toast.LENGTH_SHORT).show();


                } else {



                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.w("Error", "Error adding document", e);
                Toast.makeText(getActivity(),"Login failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bookAndNavigate(double latitude, double longitude){
//        showBottomSheet(latitude, longitude,yourPlace);
//        SaveData(latitude, latitude, yourPlace,cameraid);
        PackageManager pm =getActivity().getPackageManager();
        if(isPackageInstalled()){
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+"&daddr="+latitude+","+longitude));
            startActivity(intent);
//                    Toast.makeText(ViewImageActivity.this, "true", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.co.in/maps?saddr="+"&daddr="+latitude+","+longitude));
            startActivity(intent);
//                    Toast.makeText(ViewImageActivity.this, "false", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean isPackageInstalled() {
        try
        {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
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

    public double getPostTime() {

        Date currentDate = new Date();
        long unixTime = currentDate.getTime() / 1000;
        return unixTime;


    }




//    @Override
//    public void onPause() {
//        super.onPause();
//        mGoogleApiClient.stopAutoManage((FragmentActivity) context);
//        mGoogleApiClient.disconnect();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mGoogleApiClient.stopAutoManage((FragmentActivity) context);
//        mGoogleApiClient.disconnect();
//    }
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

    public void askRequestPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mDeniedKeys.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(getActivity(), permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                // You can either show Snackbar or AlertDialog based on your requirement
//                Snackbar.make(findViewById(android.R.id.content), stringId,
//                        Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).setAction("GRANT",
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        }).show();

                ActivityCompat.requestPermissions(getActivity(), requestedPermissions, requestCode);
            } else {
                ActivityCompat.requestPermissions(getActivity(), requestedPermissions, requestCode);
            }
        } else {
            onRequestPermissionsGranted(requestCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onRequestPermissionsGranted(requestCode);
        } else {
            openAppSetting(requestCode);
        }
    }
    private void openAppSetting(int requestCode) {
        Snackbar.make(getView().findViewById(android.R.id.content), mDeniedKeys.get(requestCode), Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).setAction("ENABLE", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        }).show();
    }

    public void onRequestPermissionsGranted(int requestCode) {

    }

}
