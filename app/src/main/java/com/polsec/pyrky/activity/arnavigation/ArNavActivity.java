package com.polsec.pyrky.activity.arnavigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondar.android.util.location.BeyondarLocationManager;

import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.polsec.pyrky.R;
import com.polsec.pyrky.ar.ArFragmentSupport;
import com.polsec.pyrky.helper.CameraPermissionHelper;
import com.polsec.pyrky.network.RetrofitInterface;
import com.polsec.pyrky.network.model.Step;
import com.polsec.pyrky.pojo.Example;
import com.polsec.pyrky.utils.LocationCalc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArNavActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

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

    private final static String TAG = "ArCamActivity";
    private String srcLatLng;
    private String destLatLng;
    private Step steps[];

    private LocationManager locationManager;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private ArFragmentSupport arFragmentSupport;
    ArFragment arFragment;

    Camera mCamera;
    Method rotateMethod;

    List<Example> mDistanceDataList = new ArrayList<Example>();
    private World world;

    private Intent intent;
    Color color;
    HitResult hitResult;
    AnchorNode lastAnchorNode;
    Location currentLoc;
    Renderable arrowRenderable;
    float[] mRotatedProjectionMatrix;
    GeoObject inter_polyGeoObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_nav);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CloseBtn = findViewById(R.id.close_iconimg);
//        mArButton = findViewById(R.id.btn_ar_enable);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(
                R.id.ar_cam_fragment);
//        srcDestText = findViewById(R.id.ar_source_dest);
        dirDistance = findViewById(R.id.ar_dir_distance);
        dirTime = findViewById(R.id.ar_dir_time);

        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Configure_AR() {
        List<List<LatLng>> polylineLatLng=new ArrayList<>();


        world=new World(getApplicationContext());

//        world.setGeoPosition(mLastLocation.getLatitude(),mLastLocation.getLongitude());
//        Log.d(TAG, "Configure_AR: LOCATION"+mLastLocation.getLatitude()+" "+mLastLocation.getLongitude());

        world = new World(getApplicationContext());

        world.setGeoPosition(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getEndLocation().getLat(),
                mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getEndLocation().getLng());
        Log.d(TAG, "Configure_AR: LOCATION" + mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getEndLocation().getLat() + " "
                + mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getEndLocation().getLng());
        world.setDefaultImage(R.drawable.ar_sphere_default);
//        polylineLatLng.add(mDistanceDataList);
//

        GeoObject signObjects[] = new GeoObject[mDistanceDataList.size()];

        Log.d(TAG, "Configure_AR: STEP.LENGTH:" + mDistanceDataList.size());
        //TODO The given below is for rendering MAJOR STEPS LOCATIONS
        for(int i=0;i<steps.length;i++){
            polylineLatLng.add(i, PolyUtil.decode(steps[i].getPolyline().getPoints()));

            Log.d(TAG, "polylineLatLng:" + polylineLatLng);

            String instructions = mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getManeuver();

//                    steps[i].getHtmlInstructions();

//

//            if(instructinstructionsions.equals("Turn")) {
//                Log.d(TAG, "Configure_AR: " + instructions);
//                GeoObject signObject = new GeoObject(10000+i);
//                signObject.setImageResource(R.drawable.turn_right);
//                signObject.setGeoPosition(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getStartLocation().getLat(),
//                        mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getStartLocation().getLng());
//                world.addBeyondarObject(signObject);
//                Log.d(TAG, "Configure_AR: RIGHT SIGN:"+i);
//            }else if(instructions.equals("Turn")){
//                Log.d(TAG, "Configure_AR: " + instructions);
//                GeoObject signObject = new GeoObject(10000+i);
//                signObject.setImageResource(R.drawable.turn_left);
//                signObject.setGeoPosition(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getStartLocation().getLat(),
//                        mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getStartLocation().getLng());
//                world.addBeyondarObject(signObject);
//                Log.d(TAG, "Configure_AR: LEFT SIGN:"+i);
//            }
        }

        int temp_polycount = 0;
        int temp_inter_polycount = 0;

        //TODO The Given below is for rendering all the LatLng in THe polylines , which is more accurate
        for (int j = 0; j < polylineLatLng.size(); j++) {
            for (int k = 0; k < polylineLatLng.get(j).size(); k++) {
//                GeoObject polyGeoObj = new GeoObject();

                GeoObject polyGeoObj=new GeoObject(1000+temp_polycount++);

//                polyGeoObj.setGeoPosition(polylineLatLng.get(j).get(k).get,
//                        polylineLatLng.get(j).get(k).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getEndLocation().getLng());

                                polyGeoObj.setGeoPosition(polylineLatLng.get(j).get(k).latitude,
                polylineLatLng.get(j).get(k).longitude);

                polyGeoObj.setImageResource(R.drawable.ar_sphere_150x);
                polyGeoObj.setName("arObj"+j+k);



//                polyGeoObj.setGeoPosition(polylineLatLng.get(j).get(k).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getEndLocation().getLat(),
//                        polylineLatLng.get(j).get(k).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getEndLocation().getLng());
//                polyGeoObj.setImageResource(R.drawable.ar_sphere);
//                polyGeoObj.setName("arObj" + j + k);

                Log.d(TAG, "Configure_AR: polyLineLatLng ="+polylineLatLng.get(j).get(k).latitude
                        +","+polylineLatLng.get(j).get(k).longitude);

//                addObject(Uri.parse("Coffee Cup_Final.sfb"));
//                addObject(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getPolyline().getPoints());

//                Anchor anchor = ArNavActivity.this.hitResult.createAnchor();
//                AnchorNode anchorNode = new AnchorNode();
//
//
//
//                if (lastAnchorNode != null) {
////                    anchorNode.setParent(arFragment.getArSceneView().getScene());
//                    Vector3 point1, point2;
//                    point1 = lastAnchorNode.getWorldPosition();
//                    point2 = anchorNode.getWorldPosition();
//
//
//
//
//        /*
//            First, find the vector extending between the two points and define a look rotation
//            in terms of this Vector.
//        */
//                     Vector3 difference = Vector3.subtract(point1, point2);
//                     Vector3 directionFromTopToBottom = difference.normalized();
//                     Quaternion rotationFromAToB =
//                            Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());
//                    MaterialFactory.makeOpaqueWithColor(getApplicationContext(), color)
//                            .thenAccept(
//                                    material -> {
///* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
//       to extend to the necessary length.  */
//                                        ModelRenderable model = ShapeFactory.makeCube(
//                                                new Vector3(.01f, .01f, difference.length()),
//                                                Vector3.zero(), material);
///* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
//       the midpoint between the given points . */
//
//
//
////                                        Node node = new Node();
////                                        node.setR
////                                        node.setParent(anchorNode);
////                                        node.setRenderable(model);
////                                        node.setWorldPosition(Vector3.add(point1, point2).scaled(.5f));
////                                        node.setWorldRotation(rotationFromAToB);
//                                    }
//                            );
//                    lastAnchorNode = anchorNode;
//                }
                /*
                To fill the gaps between the Poly objects as AR Objects in the AR View , add some more
                AR Objects which are equally spaced and provide a continuous AR Object path along the route

                Haversine formula , Bearing Calculation and formula to find
                Destination point given distance and bearing from start point is used .
                 */
//
                try {

                    double dist = LocationCalc.haversine(polylineLatLng.get(j).get(k).latitude,
                            polylineLatLng.get(j).get(k).longitude
                            , polylineLatLng.get(j).get(k + 1).latitude,
                            polylineLatLng.get(j).get(k + 1).longitude) * 1000;

                    Log.d(TAG, "Configure_AR: polyLineLatLng("+j+","+k+")="+polylineLatLng.get(j).get(k).latitude+","+polylineLatLng.get(j).get(k).longitude);


                    //Initialize distance of consecutive polyobjects

//                    Toast.makeText(ArNavActivity.this, "arscreen", Toast.LENGTH_SHORT).show();


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
                                new LatLng(polylineLatLng.get(j).get(k).latitude,
                                        polylineLatLng.get(j).get(k).longitude));

                        LatLng tempLatLng = SphericalUtil.computeOffset(new LatLng(polylineLatLng.get(j).get(k).latitude,
                                        polylineLatLng.get(j).get(k).longitude)
                                , 3f
                                , heading);
                    GeoObject inter_polyGeoObj = new GeoObject(5000 + temp_inter_polycount++);

                    double increment_dist = 3f;
                    Log.d(TAG, "Configure_AR: polyLineLatLngtemp("+j+","+k+")="+tempLatLng.latitude+","+tempLatLng.longitude);
//
//                        //The distance to be incremented
//
//
                        for (int i = 0; i < arObj_count; i++) {

//                            //Store the Lat,Lng details into new LatLng Objects using the functions
//                            //in LocationCalc class.
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

//
//
                            Log.d(TAG, "Configure_AR: LOC: k="+k+" "+ inter_polyGeoObj.getLatitude() + "," + inter_polyGeoObj.getLongitude());

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
//
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void addObject(Uri parse) {
//        Frame frame = arFragment.getArSceneView().getArFrame();
////        Point point = getScreenCenter();
//        if (frame != null) {
//            List<HitResult> hits = frame.hitTest((float) 1, (float) 1);
//
//            for (int i = 0; i < hits.size(); i++) {
//                Trackable trackable = hits.get(i).getTrackable();
//                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hits.get(i).getHitPose())) {
//                    placeObject(arFragment, hits.get(i).createAnchor(), parse);
//                }
//            }
//        }
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private final void placeObject(final ArFragment fragment, final Anchor createAnchor, Uri model) {
//        ModelRenderable.builder().setSource(fragment.getContext(), model).build().thenAccept((new Consumer() {
//            public void accept(Object var1) {
//                this.accept((ModelRenderable) var1);
//
//                ArNavActivity.this.addNode(arFragment, createAnchor);
//            }
//
//
//        }));
//    }
//
//    private void addNode(ArFragment fragment, Anchor createAnchor) {
//
//        AnchorNode anchorNode = new AnchorNode(createAnchor);
//        TransformableNode transformableNode = new TransformableNode(fragment.getTransformationSystem());
////        transformableNode.setRenderable(renderable);
//        transformableNode.setParent(anchorNode);
//        fragment.getArSceneView().getScene().addChild(anchorNode);
//        transformableNode.select();
//    }

//    private Point getScreenCenter() {
//        View vw = findViewById(android.R.id.content);
//        return new Point(vw.getWidth() / 2, vw.getHeight() / 2);
//    }
    private void Get_intent() {
        if (getIntent() != null) {
            intent = getIntent();

//            srcDestText.setText(intent.getStringExtra("SRC")+" -> "+intent.getStringExtra("DEST"));
            srcLatLng = intent.getStringExtra("SRCLATLNG");
            destLatLng = intent.getStringExtra("DESTLATLNG");

            Log.e("SRCLATLNG", srcLatLng);
            Log.e("DESTLATLNG", destLatLng);

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

        final Call<Example> call = apiService.getDirections(srcLatLng, destLatLng,
                getResources().getString(R.string.google_maps_key));

        Log.d(TAG, "Directions_call: srclat lng:" + srcLatLng + "\n" + "destLatlng:" + destLatLng);

        call.enqueue(new Callback<Example>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                Example directionsResponse = response.body();

                mDistanceDataList.add(directionsResponse);
//           Northeast responsvale=  response.body().getRoutes().get(0).getBounds().getNortheast();
                Log.e("response", String.valueOf(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getPolyline().getPoints()));
                int step_array_size = mDistanceDataList.size();


                steps = new Step[step_array_size];

                for (int i = 0; i < mDistanceDataList.size(); i++) {
                    steps[i] = mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(i);
                    Log.d(TAG, "onResponse: STEP " + i + ": " + steps[i].getEndLocation().getLat()
                            + " " + steps[i].getEndLocation().getLng());
                }
//                Configure_AR();

                createNavigationPath(mDistanceDataList);

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Log.d(TAG, "onFailure: FAIL" + t.getMessage());
//                new AlertDialog.Builder(getApplicationContext()).setMessage("Fetch Failed").show();
            }
        });
    }

    public void createNavigationPath(List<Example> mDistanceDataList) {
        // loop through the wayfinding route

        for(int i=0;i<mDistanceDataList.size();i++)



            dirDistance.setVisibility(View.VISIBLE);
        dirDistance.setText(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0)
                .getDistance().getText());

        dirTime.setVisibility(View.VISIBLE);
        dirTime.setText(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0)
                .getDuration().getText());

            // current loc
            currentLoc = new Location("Current Location");
            currentLoc.setLatitude(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getStartLocation().getLat());
        currentLoc.setLongitude(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getStartLocation().getLat());

            // end of wayfinding route segment
            Location legEnd = new Location("Leg End");
            legEnd.setLatitude(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getEndLocation().getLat());
            legEnd.setLongitude(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getEndLocation().getLat());
        Log.d("currentLoc:", String.valueOf(currentLoc));
        Log.d("legEnd:", String.valueOf(legEnd));
            // conversion from (lat,long) to camera space
            // conversion implementation credit:
////            // https://github.com/dat-ng/ar-location-based-android
//            float[] currentLocationInECEF = WSG84toECEF(currentLoc);
//            float[] pointInECEF = WSG84toECEF(legEnd);
//        float[] pointInENU = ECEFtoENU(currentLoc, currentLocationInECEF, pointInECEF);

            float[] cameraCoordinateVector = new float[4];
        inter_polyGeoObj = new GeoObject();


        //Set the Geoposition along with image and name
        inter_polyGeoObj.setGeoPosition(mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getEndLocation().getLat()
                , mDistanceDataList.get(0).getRoutes().get(0).getLegs().get(0).getSteps().get(0).getStartLocation().getLat());
        inter_polyGeoObj.setImageResource(R.drawable.ar_sphere_150x);
//        inter_polyGeoObj.setName("inter_arObj" + j + k + i);


        Log.d(TAG, "Render Completed");
//        Matrix.multiplyMV(cameraCoordinateVector, 0, mRotatedProjectionMatrix, 0, pointInENU, 0);

            Frame frame = arFragment.getArSceneView().getArFrame();

            // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
            // if z > 0, the point will display on the opposite
//            if (cameraCoordinateVector[2] < 0) {
//                float x = (0.1f - cameraCoordinateVector[0] / cameraCoordinateVector[1]) * arFragment.getArSceneView().getWidth();
//                float y = (0.1f -  cameraCoordinateVector[1] / cameraCoordinateVector[1]) * arFragment.getArSceneView().getHeight();
//
//
//
//                // Pose pose = new Pose(new float[]{x,y,cameraCoordinateVector[2]}, new float[]{0,0,0});
//
////                Anchor anchor = arFragment.getArSceneView().getSession().createAnchor(frame.getCamera().getPose().compose(Pose.makeTranslation(x,y,cameraCoordinateVector[2])).extractTranslation());
////                AnchorNode anchorNode = new AnchorNode(anchor);
////
////                Node tempNode = new Node();
//////                                tempNode.setParent(inter_polyGeoObj);
////                tempNode.setRenderable(arrowRenderable);
//
////                frame.getUpdatedAnchors();
//
//            }

        arFragment.setReenterTransition(inter_polyGeoObj);
        }

//    private float[] WSG84toECEF(Location currentLoc) {
//
//        Log.d("lat:", String.valueOf(currentLoc));
//
//
//        return new float[0];
//    }
//
//    private float[] ECEFtoENU(Location currentLoc, float[] currentLocationInECEF, float[] pointInECEF) {
//
//
//        Log.d("currentLocationInECEF:", String.valueOf(currentLocationInECEF));
//        return currentLocationInECEF;
//    }


}