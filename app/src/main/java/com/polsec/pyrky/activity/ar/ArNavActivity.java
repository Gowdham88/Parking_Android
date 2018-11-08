package com.polsec.pyrky.activity.ar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondar.android.opengl.renderable.Renderable;
import com.beyondar.android.util.location.BeyondarLocationManager;

import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.polsec.pyrky.R;
import com.polsec.pyrky.helper.CameraPermissionHelper;
import com.polsec.pyrky.network.RetrofitInterface;
import com.polsec.pyrky.network.model.Step;
import com.polsec.pyrky.pojo.Example;
import com.polsec.pyrky.pojo.Locationlatlong;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.LocationCalc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sin;

public class ArNavActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener{

    // Set to true ensures requestInstall() triggers installation if necessary.
    private boolean mUserRequestedInstall = true;
    Session mSession;
    Button mArButton;
    ImageView CloseBtn;

    //    @BindView(R.id.ar_source_dest)
    TextView srcDestText;
    //    @BindView(R.id.ar_dir_distance)
    TextView dirDistance;
    //    @BindView(R.id.ar_dir_time)
    TextView dirTime;
    Display mDisplay;
    int coordinates;

    int step_array_size;
ArrayList<String>val=new ArrayList<>();
    private final static String TAG = "ArCamActivity";
    private String srcLatLng;
    private String destLatLng;
            String documentIDs;
    private Step steps[];

    private LocationManager locationManager;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private ArFragment arFragmentSupport;
    ArObject arobject;
    boolean isLineDrawn = false;

    private ModelRenderable andyRenderable;
    Anchor anchor;
    AnchorNode lastAnchorNode;
    Camera mCamera;
    Method rotateMethod;
    Iterator<Plane> planes;

    float valx;
    float valy;

    List<Example> mDistanceDataList = new ArrayList<Example>();
    private World world;

    private Intent intent;
    private static final int REQUEST_CONTACT_PERMISSIONS = 101;
    private static final int REQUEST_CAMERA_PERMISSIONS = 102;
    private static final int REQUEST_EXTERNAL_PERMISSIONS = 103;
    // ===============

    // Group permission request code
    private static final int REQUEST_GROUP_PERMISSIONS = 104;

    Vector3 point1, point2;

    ArrayList<Locationlatlong> latlonglist=new ArrayList<>();

    float[] mRotatedProjectionMatrix;
    GeoObject inter_polyGeoObj;
    ModelRenderable  lineRenderable;
    Frame frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_nav);
        Get_intent();
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CloseBtn = findViewById(R.id.close_iconimg);
//        mArButton = findViewById(R.id.btn_ar_enable);
        arFragmentSupport = (ArFragment) getSupportFragmentManager().findFragmentById(
                R.id.ar_fragment);
//        srcDestText = findViewById(R.id.ar_source_dest);
        dirDistance = findViewById(R.id.ar_dir_distance);
        dirTime = findViewById(R.id.ar_dir_time);
        documentIDs =PreferencesHelper.getPreference(ArNavActivity.this,PreferencesHelper.PREFERENCE_DOCUMENTIDNEW);
        Log.e("doc",documentIDs);

        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });






//        PopUpprotectcar();

//        if ((ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
//                || (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                || (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
//            askRequestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    R.string.enable_permission, REQUEST_GROUP_PERMISSIONS);
//        } else {
//            groupPermissionEnable();
//        }

        // Enable AR related functionality on ARCore supported devices only.
//        maybeEnableArButton();

        Set_googleApiClient(); //Sets the GoogleApiClient




        //Configure_AR(); //Configure AR Environment

//        Directions_call();

    }


    void maybeEnableArButton() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    maybeEnableArButton();
                }
            }, 200);
        }
        if (availability.isSupported()) {
//            mArButton.setVisibility(View.INVISIBLE);
//            mArButton.setEnabled(true);
            // indicator on the button.
        } else { // Unsupported or unknown.
//            mArButton.setVisibility(View.INVISIBLE);
//            mArButton.setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BeyondarLocationManager.disable();
    }

    /* @Override
     protected void onResume() {
         super.onResume();
         BeyondarLocationManager.enable();
     }*/
    @Override
    protected void onResume() {
        super.onResume();

        BeyondarLocationManager.enable();


        // ARCore requires camera permission to operate.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }


        // Make sure ARCore is installed and up to date.
        try {
            if (mSession == null) {
                switch (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                    case INSTALLED:
                        // Success, create the AR session.
                        mSession = new Session(this);

                        break;
                    case INSTALL_REQUESTED:
                        // Ensures next invocation of requestInstall() will either return
                        // INSTALLED or throw an exception.
                        mUserRequestedInstall = false;
                        return;
                }
            }
        } catch (UnavailableUserDeclinedInstallationException e) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "TODO: handle exception " + e, Toast.LENGTH_LONG)
                    .show();
            return;
        } catch (UnavailableArcoreNotInstalledException e) {
            e.printStackTrace();
        } catch (UnavailableDeviceNotCompatibleException e) {
            e.printStackTrace();
        } catch (UnavailableSdkTooOldException e) {
            e.printStackTrace();
        } catch (UnavailableApkTooOldException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to use this feature", Toast.LENGTH_LONG)
                    .show();
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this);
            }
            finish();
        }
    }

    /**
     * Callback when requested permission is granted. By overriding this method user can process further on permission granted.
     *
//     * @param requestCode The request code passed in {@link #onRequestPermissionsResult(int, String[], int[])}
     */
//    @Override
//    public void onRequestPermissionsGranted(int requestCode) {
//        groupPermissionEnable();
//    }

    private void groupPermissionEnable() {

        Toast.makeText(this, "Group runtime permission enable successfully...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;

            // mLastLocation = locationManager.getLastKnownLocation(locationProvider);

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                try {
                    Get_intent(); //Fetch Intent Values
                } catch (Exception e) {
                    Log.d(TAG, "onCreate: Intent Error");
                }
            }
        }

        startLocationUpdates();
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, createLocationRequest(), this);

        } catch (SecurityException e) {
            Toast.makeText(this, "Location Permission not granted . Please Grant the permissions",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (world != null) {
            world.setGeoPosition(location.getLatitude(), location.getLongitude());
        }
    }

    private void Set_googleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public static Drawable setTint(Drawable d, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }


    private void Configure_AR(){
        List<List<LatLng>> polylineLatLng=new ArrayList<>();

        world=new World(ArNavActivity.this);
//
        world.setGeoPosition(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        Log.d(TAG, "Configure_AR: LOCATION"+mLastLocation.getLatitude()+" "+mLastLocation.getLongitude());
////        world.setDefaultImage(R.drawable.ar_sphere_default);


//               polylineLatLng.add(mDistanceDataList);
        arFragmentSupport = (ArFragment) getSupportFragmentManager().findFragmentById(
                R.id.ar_fragment);

        GeoObject signObjects[]=new GeoObject[steps.length];

        Log.d(TAG, "Configure_AR: STEP.LENGTH:"+steps.length);
        //TODO The given below is for rendering MAJOR STEPS LOCATIONS
        for(int i=0;i<steps.length;i++){
            polylineLatLng.add(i,PolyUtil.decode(steps[i].getPolyline().getPoints()));

//            arobject.setGeoPosition(mLastLocation.getLatitude(),mLastLocation.getLongitude(),mLastLocation.getAltitude());
//
//            Log.d(TAG, "Configure_ARlat",String.valueOf(mLastLocation.getLatitude()));
//            arobject.setVisible(true);
//            arobject.setImageResource(R.drawable.ar_sphere);

            String instructions=steps[i].getHtmlInstructions();

            if(i==0){
                GeoObject signObject = new GeoObject(10000+i);
                signObject.setImageResource(R.drawable.start);
                signObject.setGeoPosition(steps[i].getStartLocation().getLat(),steps[i].getStartLocation().getLng());
                world.addBeyondarObject(signObject);
                Log.d(TAG, "Configure_AR: START SIGN:"+i);
            }

            if(i==steps.length-1){
                GeoObject signObject = new GeoObject(10000+i);
                signObject.setImageResource(R.drawable.stop);
                LatLng latlng=SphericalUtil.computeOffset(
                        new LatLng(steps[i].getEndLocation().getLat(),steps[i].getEndLocation().getLng()),
                        4f, SphericalUtil.computeHeading(
                                new LatLng(steps[i].getStartLocation().getLat(),steps[i].getStartLocation().getLng()),
                                new LatLng(steps[i].getEndLocation().getLat(),steps[i].getEndLocation().getLng())));
                signObject.setGeoPosition(latlng.latitude,latlng.longitude);
                world.addBeyondarObject(signObject);
                Log.d(TAG, "Configure_AR: STOP SIGN:"+i);
            }

            if(instructions.contains("right")) {
                Log.d(TAG, "Configure_AR: " + instructions);
                GeoObject signObject = new GeoObject(10000+i);
                signObject.setImageResource(R.drawable.turn_right);
                signObject.setGeoPosition(steps[i].getStartLocation().getLat(),steps[i].getStartLocation().getLng());
                world.addBeyondarObject(signObject);
                Log.d(TAG, "Configure_AR: RIGHT SIGN:"+i);
            }else if(instructions.contains("left")){
                Log.d(TAG, "Configure_AR: " + instructions);
                GeoObject signObject = new GeoObject(10000+i);
                signObject.setImageResource(R.drawable.turn_left);
                signObject.setGeoPosition(steps[i].getStartLocation().getLat(),steps[i].getStartLocation().getLng());
                world.addBeyondarObject(signObject);
                Log.d(TAG, "Configure_AR: LEFT SIGN:"+i);
            }
        }

        int temp_polycount=0;
        int temp_inter_polycount=0;

        //TODO The Given below is for rendering all the LatLng in THe polylines , which is more accurate
        for(int j=0;j<polylineLatLng.size();j++){
            for(int k=0;k<polylineLatLng.get(j).size();k++){
                GeoObject polyGeoObj=new GeoObject(1000+temp_polycount++);

                polyGeoObj.setGeoPosition(polylineLatLng.get(j).get(k).latitude,
                        polylineLatLng.get(j).get(k).longitude);
                polyGeoObj.setImageResource(R.drawable.ar_sphere_150x);
                polyGeoObj.setName("arObj"+j+k);

                /*
                To fill the gaps between the Poly objects as AR Objects in the AR View , add some more
                AR Objects which are equally spaced and provide a continuous AR Object path along the route

                Haversine formula , Bearing Calculation and formula to find
                Destination point given distance and bearing from start point is used .
                 */

                try {

                    //Initialize distance of consecutive polyobjects
                    double dist = LocationCalc.haversine(polylineLatLng.get(j).get(k).latitude,
                            polylineLatLng.get(j).get(k).longitude, polylineLatLng.get(j).get(k + 1).latitude,
                            polylineLatLng.get(j).get(k + 1).longitude) * 1000;

                    //Log.d(TAG, "Configure_AR: polyLineLatLng("+j+","+k+")="+polylineLatLng.get(j).get(k).latitude+","+polylineLatLng.get(j).get(k).longitude);
                    //Log.d(TAG, "Configure_AR: polyLineLatLng("+j+","+(k+1)+")="+polylineLatLng.get(j).get(k+1).latitude+","+polylineLatLng.get(j).get(k+1).longitude);

                    //Check if distance between polyobjects is greater than twice the amount of space
                    // intended , here it is (3*2)=6 .
                    if (dist > 6) {

                        //Initialize count of ar objects to be added
                        int arObj_count = ((int) dist / 3) - 1;

                        //Log.d(TAG, "Configure_AR: Dist:" + dist + " # No of Objects: " + arObj_count + "\n --------");

                        double bearing = LocationCalc.calcBearing(polylineLatLng.get(j).get(k).latitude,
                                polylineLatLng.get(j).get(k + 1).latitude,
                                polylineLatLng.get(j).get(k).longitude,
                                polylineLatLng.get(j).get(k + 1).longitude);

                        double heading = SphericalUtil.computeHeading(new LatLng(polylineLatLng.get(j).get(k).latitude,
                                        polylineLatLng.get(j).get(k).longitude),
                                new LatLng(polylineLatLng.get(j).get(k + 1).latitude,
                                        polylineLatLng.get(j).get(k + 1).longitude));

                        LatLng tempLatLng = SphericalUtil.computeOffset(new LatLng(polylineLatLng.get(j).get(k).latitude,
                                        polylineLatLng.get(j).get(k).longitude)
                                ,3f
                                ,heading);

                        //The distance to be incremented
                        double increment_dist = 3f;

                        for (int i = 0; i < arObj_count; i++) {
                            GeoObject inter_polyGeoObj = new GeoObject(5000 + temp_inter_polycount++);

                            //Store the Lat,Lng details into new LatLng Objects using the functions
                            //in LocationCalc class.
                            if (i > 0 && k < polylineLatLng.get(j).size()) {
                                increment_dist += 3f;

                                tempLatLng = SphericalUtil.computeOffset(new LatLng(polylineLatLng.get(j).get(k).latitude,
                                                polylineLatLng.get(j).get(k).longitude),
                                        increment_dist,
                                        SphericalUtil.computeHeading(new LatLng(polylineLatLng.get(j).get(k).latitude
                                                , polylineLatLng.get(j).get(k).longitude), new LatLng(
                                                polylineLatLng.get(j).get(k + 1).latitude
                                                , polylineLatLng.get(j).get(k + 1).longitude)));
                            }

                            //Set the Geoposition along with image and name
                            inter_polyGeoObj.setGeoPosition(tempLatLng.latitude, tempLatLng.longitude);
                            inter_polyGeoObj.setImageResource(R.drawable.ar_sphere_default_125x);
                            inter_polyGeoObj.setName("inter_arObj" + j + k + i);

                            //Log.d(TAG, "Configure_AR: LOC: k="+k+" "+ inter_polyGeoObj.getLatitude() + "," + inter_polyGeoObj.getLongitude());

                            //Add Intermediate ArObjects to Augmented Reality World
                            world.addBeyondarObject(inter_polyGeoObj);
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Configure_AR: EXCEPTION CAUGHT:" + e.getMessage());
                }

                //Add PolyObjects as ArObjects to Augmented Reality World
                world.addBeyondarObject(polyGeoObj);
                Log.d(TAG, "\n\n");
            }
        }

        // Send to the fragment
//        arFragmentSupport.setWorld(world);
    }

    private void Get_intent() {
        if (getIntent() != null) {
            intent = getIntent();

//            srcDestText.setText(intent.getStringExtra("SRC")+" -> "+intent.getStringExtra("DEST"));
            srcLatLng =intent.getStringExtra("SRCLATLNG");
            destLatLng = intent.getStringExtra("DESTLATLNG");

            Log.e("SRCLATLNG", String.valueOf(srcLatLng));
            Log.e("DESTLATLNG", String.valueOf(destLatLng));

            Directions_call(); //HTTP Google Directions API Call
        }
    }

    private void Directions_call() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.directions_base_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface apiService =
                retrofit.create(RetrofitInterface.class);
        Boolean sensor = true;

        final Call<Example> call = apiService.getDirections(String.valueOf(srcLatLng), String.valueOf(destLatLng),
                getResources().getString(R.string.google_maps_key));

        Log.d(TAG, "Directions_call: srclat lng:" + srcLatLng + "\n" + "destLatlng:" + destLatLng);

        call.enqueue(new Callback<Example>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                Example directionsResponse = response.body();
                mDistanceDataList.add(directionsResponse);

                 step_array_size=directionsResponse.getRoutes().get(0).getLegs().get(0).getSteps().size();

                steps=new Step[step_array_size];
                for(int i=0;i<step_array_size;i++) {
                    steps[i] = directionsResponse.getRoutes().get(0).getLegs().get(0).getSteps().get(i);
                    Log.d(TAG, "onResponse: STEP endkm "+i+": "+steps[i].getEndLocation().getLat()
                            +" "+steps[i].getEndLocation().getLng());
                    Log.d(TAG, "onResponse: STEP start"+i+": "+steps[i].getStartLocation().getLat()
                            +" "+steps[i].getStartLocation().getLng());

                    dirDistance.setVisibility(View.VISIBLE);
                    dirDistance.setText( steps[i]
                            .getDistance().getText());

                    dirTime.setVisibility(View.VISIBLE);
                    dirTime.setText(steps[i]
                            .getDuration().getText());

                    Locationlatlong locationlatlong=new Locationlatlong();
                    locationlatlong.setStartlatitude(steps[i].getStartLocation().getLat());
                    locationlatlong.setStartlongitude(steps[i].getStartLocation().getLng());
                    locationlatlong.setEndlatitude(steps[i].getEndLocation().getLat());
                    locationlatlong.setEndlongitude(steps[i].getEndLocation().getLng());
                    latlonglist.add(locationlatlong);
                    Log.e("latlonglist", String.valueOf(latlonglist.get(0).getStartlatitude()));



//                    Configure_AR();

                    double lat1 = steps[i].getStartLocation().getLat()/ 180 * Math.PI;
                    double lng1 = steps[i].getStartLocation().getLng() / 180 * Math.PI;
                    double lat2 = steps[i].getEndLocation().getLat() / 180 * Math.PI;
                    double lng2 = steps[i].getEndLocation().getLng() / 180 * Math.PI;

                    double y = sin(lng2 - lng1) * cos(lat2);
                    double x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lng2 - lng1);
                     valx= (float) x;
                     valy= (float) y;


                    Log.e("x", String.valueOf(valx));
                    Log.e("y", String.valueOf(valy));
                    double tan2 = atan2(y, x);
                    double degree = tan2 * 180 / Math.PI;

                    if (degree < 0)
                    {
                        coordinates = (int) (degree+360);
                    } else {
                        coordinates = (int) degree;
                    }
//
                }
                lastAnchorNode = new AnchorNode();
//

                point2 = lastAnchorNode.getWorldPosition();

//                                    for(int i=0;i<xvalue.size();i++){
                point1 = new Vector3(valx,valy,-90);

//                transformableNode.setWorldPosition(new Vector3(valx,valy,-90));
//                                    }
//                                    point2 = anchorNode.getWorldPosition();


                polyLineCode(lastAnchorNode,point1,point2);

//                makePolyLine("directionarrow.sfb",valx,valy);


            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Log.d(TAG, "onFailure: FAIL" + t.getMessage());
                new AlertDialog.Builder(getApplicationContext()).setMessage("Fetch Failed").show();
            }
        });
    }


    public void onUpdate(int coordinates) {



        frame = arFragmentSupport.getArSceneView().getArFrame();

        if (frame != null) {

            for (Object o : frame.getUpdatedTrackables(Plane.class)) {

                Plane plane = (Plane) o;

                if (plane.getTrackingState() == TrackingState.TRACKING) {
                    arFragmentSupport.getPlaneDiscoveryController().hide();

                    Iterator iterableAnchor = frame.getUpdatedAnchors().iterator();

                    if (!iterableAnchor.hasNext()) {
                        makeAr(plane, frame,coordinates);
                    }
                }
            }
        }

    }

    public void makeAr(Plane plane, Frame frame, int coordinates) {

        for (int k = 0; k <10 ; k ++) {
            if (this.coordinates >= 160 && this.coordinates <= 170) {
                Toast.makeText(this, "walk", Toast.LENGTH_SHORT).show();
                List<HitResult> hitTest = frame.hitTest(screenCenter().x, screenCenter().y);

                Iterator hitTestIterator = hitTest.iterator();

                while (hitTestIterator.hasNext()) {
                    HitResult hitResult = (HitResult) hitTestIterator.next();

                    Anchor modelAnchor = null;
                    modelAnchor = plane.createAnchor(hitResult.getHitPose());

                    AnchorNode anchorNode = new AnchorNode(modelAnchor);
                    anchorNode.setParent(arFragmentSupport.getArSceneView().getScene());

                    TransformableNode transformableNode = new TransformableNode(arFragmentSupport.getTransformationSystem());
                    transformableNode.setParent(anchorNode);
                    transformableNode.setRenderable(ArNavActivity.this.andyRenderable);

                    float x = modelAnchor.getPose().tx();
                    float y = modelAnchor.getPose().compose(Pose.makeTranslation(0f, 0f, 0)).ty();

                    transformableNode.setWorldPosition(new Vector3(x, y, -k));

                }
            }
        }
    }

    private Vector3 screenCenter() {
        View vw = findViewById(android.R.id.content);
        return new Vector3(vw.getWidth() / 2f, vw.getHeight() / 2f, 0f);
    }







    @RequiresApi(api = Build.VERSION_CODES.N)
    public void lineBetweenPoints(Vector3 point1, Vector3 point2) {
        Node lineNode = new Node();
        AnchorNode anchorNode = new AnchorNode(anchor);

   /* First, find the vector extending between the two points and define a look rotation in terms of this
        Vector. */

        final Vector3 difference = Vector3.subtract(point1, point2);
        final Vector3 directionFromTopToBottom = difference.normalized();

        Log.d("MainActivity", String.valueOf(difference));


        final Quaternion rotationFromAToB =
                Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());

   /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
         to extend to the necessary length.  */


        MaterialFactory.makeOpaqueWithColor(this,new com.google.ar.sceneform.rendering.Color())
                .thenAccept(
                        material -> {
                              lineRenderable = ShapeFactory.makeCube(new Vector3(.01f, .01f, difference.length()),
                                    Vector3.zero(), material);


                        });

   /* Last, set the local rotation of the node to the rotation calculated earlier and set the local position to
       the midpoint between the given points . */

        lineNode.setParent(anchorNode);
        lineNode.setRenderable(lineRenderable);
        lineNode.setLocalPosition(Vector3.add(point1, point2).scaled(.5f));
        lineNode.setLocalRotation(rotationFromAToB);

        lastAnchorNode = anchorNode;



    }

//    private void addLineBetweenHits(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
//        anchor = hitResult.createAnchor();
//        AnchorNode anchorNode = new AnchorNode(anchor);
//        lastAnchorNode = new AnchorNode();
//        if (lastAnchorNode != null) {
//            anchorNode.setParent(arFragmentSupport.getArSceneView().getScene());
//            Vector3 point1, point2;
//            point1 = lastAnchorNode.getWorldPosition();
//            point2 = anchorNode.getWorldPosition();
//            Log.d("points", point1+","+point2);
//
//        /*
//            First, find the vector extending between the two points and define a look rotation
//            in terms of this Vector.
//        */
//            final Vector3 difference = Vector3.subtract(point1, point2);
//            final Vector3 directionFromTopToBottom = difference.normalized();
//            final Quaternion rotationFromAToB =
//                    Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                MaterialFactory.makeOpaqueWithColor(getApplicationContext(), new com.google.ar.sceneform.rendering.Color())
//                        .thenAccept(
//                                material -> {
//    /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
//           to extend to the necessary length.  */
//                                    ModelRenderable model = ShapeFactory.makeCube(
//                                            new Vector3(.01f, .01f, difference.length()),
//                                            Vector3.zero(), material);
//    /* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
//           the midpoint between the given points . */
////                                    Node node = new Node();
////                                    node.setParent(anchorNode);
////                                    node.setRenderable(model);
////                                    node.setWorldPosition(Vector3.add(point1, point2).scaled(.5f));
////                                    node.setWorldRotation(rotationFromAToB);
//
//                                    Anchor anchor = hitResult.createAnchor();
//                                    AnchorNode anchorNode1 = new AnchorNode(anchor);
//                                    anchorNode.setParent(arFragmentSupport.getArSceneView().getScene());
//
//                                    // Create the transformable andy and add it to the anchor.
//                                    TransformableNode andy = new TransformableNode(arFragmentSupport.getTransformationSystem());
//                                    andy.setParent(anchorNode1);
//                                    andy.setRenderable(andyRenderable);
//                                    andy.select();
//                                }
//                        );
//            }
//            lastAnchorNode = anchorNode;
//        }
//    }



    private void makePolyLine(String direction, float valx, float valy) {
        Log.e("valx", String.valueOf(this.valx));
        //Create the arrow renderable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ModelRenderable.builder()
                    //get the context of the ARFragment and pass the name of your .sfb file
                    .setSource(ArNavActivity.this, Uri.parse(direction))
                    .build()
                    .thenAcceptAsync(modelRenderable -> andyRenderable = modelRenderable);
            Toast.makeText(ArNavActivity.this, direction, Toast.LENGTH_SHORT).show();
//                    .exceptionally(throwable -> {Toast toast = Toast.makeText(this,"Unable to load renderable ", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();});
            //I accepted the CompletableFuture using Async since I created my model on creation of the activity. You could simply use .thenAccept too.
            //Use the returned modelRenderable and save it to a global variable of the same name
        }

        arFragmentSupport.getArSceneView().getScene().addOnUpdateListener(new Scene.OnUpdateListener() {
            @Override
            public void onUpdate(FrameTime frameTime) {



                //get the frame from the scene for shorthand
                Frame frame = arFragmentSupport.getArSceneView().getArFrame();

                if (frame != null) {
                    //get the trackables to ensure planes are detected
                    planes = frame.getUpdatedTrackables(Plane.class).iterator();

                    while (planes.hasNext()) {

                        Plane plane = planes.next();
                        //If a planes has been detected & is being tracked by ARCore
                        if (plane.getTrackingState() == TrackingState.TRACKING) {

                            //Hide the planes discovery helper animation
                            arFragmentSupport.getPlaneDiscoveryController().hide();

                            //Get all added anchors to the frame
                            Iterator iterableAnchor = frame.getUpdatedAnchors().iterator();

                            //place the first object only if no previous anchors were added
                            if (!iterableAnchor.hasNext()) {

//                                makeAr(plane,frame);

                                //Perform a hit test at the center of the screen to place an object without tapping
                                List<HitResult> hitTest = frame.hitTest(screenCenter().x, screenCenter().y);

                                //iterate through all hits
                                Iterator<HitResult> hitTestIterator = hitTest.iterator();
                                while (hitTestIterator.hasNext()){
                                    HitResult hitResult = hitTestIterator.next();


                                    //Create an anchor at the plane hit
                                    Anchor modelAnchor = plane.createAnchor(hitResult.getHitPose());
                                    //Attach a node to this anchor with the scene as the parent
                                    AnchorNode anchorNode = new AnchorNode(modelAnchor);
                                    anchorNode.setParent(arFragmentSupport.getArSceneView().getScene());


                                    TransformableNode transformableNode = new TransformableNode(arFragmentSupport.getTransformationSystem());
                                    transformableNode.setParent(anchorNode);
                                    transformableNode.setRenderable(ArNavActivity.this.andyRenderable);

//                                    float xx = modelAnchor.getPose().tx();
//                                    float yy = modelAnchor.getPose().compose(Pose.makeTranslation(0f, 0f, 0f)).ty();
//                                    float zz = modelAnchor.getPose().tz();
//                                    transformableNode.setWorldPosition(new Vector3(xx,yy,zz));

                                    lastAnchorNode = new AnchorNode();


                                    point2 = lastAnchorNode.getWorldPosition();

//                                    for(int i=0;i<xvalue.size();i++){
                                    point1 = new Vector3(valx,valy,-90);

//                                    transformableNode.setWorldPosition(new Vector3(valx,valy,-90));
//                                    }
//                                    point2 = anchorNode.getWorldPosition();


                                    polyLineCode(anchorNode,point1,point2);

                                }


//                                makeAr(planes, frame);
                            }
                        }
                    }
                }
//                Toast.makeText(context, "Wait for surface detection", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void polyLineCode(AnchorNode anchorNode, Vector3 point1, Vector3 point2){

//        if (!isLineDrawn){

            final Vector3 difference = Vector3.subtract(point1, point2);
            final Vector3 directionFromTopToBottom = difference.negated();
            final Quaternion rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.left());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                MaterialFactory.makeOpaqueWithColor(getApplicationContext(), new com.google.ar.sceneform.rendering.Color(Color.BLACK))
                        .thenAccept(
                                material -> {
    /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
           to extend to the necessary length.  */
                                    ModelRenderable model = ShapeFactory.makeCube(
                                            new Vector3(.05f, .05f, difference.length()),
                                            Vector3.zero(), material);

    /* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
           the midpoint between the given points . */
                                    Node node = new Node();
                                    node.setParent(anchorNode);
                                    node.setRenderable(model);
                                    node.setWorldPosition(Vector3.add(point1, point2).scaled(.15f));
//                                node.setWorldRotation(rotationFromAToB);

//                                    isLineDrawn = true;


                                }
                        );
            }

            lastAnchorNode = anchorNode;
        }

//    }





    private void PopUpprotectcar() {

        LayoutInflater factory = LayoutInflater.from(ArNavActivity.this);
        final View deleteDialogView = factory.inflate(R.layout.protetcar_alert, null);
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(ArNavActivity.this);
        alertDialog.setView(deleteDialogView);
        TextView ok = deleteDialogView.findViewById(R.id.ok_button);
        TextView cancel = deleteDialogView.findViewById(R.id.cancel_button);
        final MediaPlayer mp = MediaPlayer.create(ArNavActivity.this, R.raw.parking_alert );

        final android.support.v7.app.AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mp.start();
//                isBookedAny = false;
//                if (bookingRequest){
//                    makeAlreadyBookedAlert(true,latitude,longitude, yourPlace, yourPlace);
//                }else{
//                    makeAlreadyBookedAlert(false,latitude,longitude, yourPlace, yourPlace);
//                }

                protectCar(true,true,documentIDs);
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

    private void protectCar(Boolean protectCar, Boolean bookingStatus, String documentIDs){
        final Map<String, Object> protectdata = new HashMap<>();
        protectdata.put("protectCar", protectCar);
        protectdata.put("bookingStatus", false);



        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("Bookings").document(documentIDs)
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


}