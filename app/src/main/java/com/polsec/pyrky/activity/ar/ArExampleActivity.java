package com.polsec.pyrky.activity.ar;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Point;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.polsec.pyrky.R;
import com.polsec.pyrky.pojo.Locationlatlong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.negateExact;
import static java.lang.Math.sin;

public class ArExampleActivity extends AppCompatActivity{

    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;
    Button ar_button, dir_button;
    EditText m_address;
    private ArFragment arFragmentSupport;
    LocationManager locationManager;
    String origin_latitude, origin_longitude, dest_longitude, dest_latitude;
    float valx;
    float valy;
    int coordinates;
    Vector3 point1, point2;
    ModelRenderable  lineRenderable;
    Anchor anchor;
    AnchorNode lastAnchorNode;
    private Intent intent;
    private String srcLatLng;
    private String destLatLng;
    ObjectAnimator objectAnimation;
    Node andy,endNode;
    Renderable andyRenderable;
    AnchorNode startNode;
    Anchor startAnchor,endAnchor;
    PropertyValuesHolder movingAnimation;

    ArrayList<Locationlatlong> latlonglist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_example);
        arFragmentSupport = (ArFragment) getSupportFragmentManager().findFragmentById(
                R.id.ar_fragment);

        if (getIntent() != null) {
            intent = getIntent();

//            srcDestText.setText(intent.getStringExtra("SRC")+" -> "+intent.getStringExtra("DEST"));
            srcLatLng =intent.getStringExtra("SRCLATLNG");
            destLatLng = intent.getStringExtra("DESTLATLNG");

            Log.e("SRCLATLNG", String.valueOf(srcLatLng));
            Log.e("DESTLATLNG", String.valueOf(destLatLng));
            //HTTP Google Directions API Call
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();
        String address = "(" + origin_latitude.substring(0, 8)  + ", " + origin_longitude.substring(0, 8) + ")";

        String url = createRequestURL();
        Log.e("url",url);
        RequestDirections requestDirections = new RequestDirections();
        requestDirections.execute(url);

    }


    private void getLocation() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    origin_latitude = String.valueOf(latitude);
                    origin_longitude = String.valueOf(longitude);
                }
            }
        }
    }

    private String createRequestURL() {
        String origin = "origin=" +srcLatLng;
        String dest = "destination=" +destLatLng;
        String mode = "mode=walking";

        String key="key"+"="+"AIzaSyD7rCvtQxAnej-1SdFj7H6ZKnTmyOTs2cQ";

        String url = "https://maps.googleapis.com/maps/api/directions/json?" + origin + "&" + dest+"&"+key+ "&" + mode;

        Log.e("url",url);
        return url;
    }

    private String getDirections(String reqURL) throws IOException {
        String response = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            response = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }

        return response;
    }


    public class RequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String responseString = "";
            try {
                responseString = getDirections(params[0]);

                if (responseString != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(responseString);
                        Log.e("jsonObj", String.valueOf(jsonObj));



                        JSONArray jRoutes = jsonObj.getJSONArray("routes");

                        for (int i = 0; i < jRoutes.length(); i++) {
                            JSONArray jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                            List path = new ArrayList<HashMap<String, String>>();

                            for (int j = 0; j < jLegs.length(); j++) {
                                JSONArray   jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                                for (int k = 0; k < jSteps.length(); k++) {

                                    JSONObject c = jSteps.getJSONObject(i);
                                    JSONObject Startlocation = c.getJSONObject("start_location");

                                    String strlat=Startlocation.getString("lat");
                                    String strlng=Startlocation.getString("lng");

                                    JSONObject Endlocation = c.getJSONObject("end_location");

                                    String endlat=Endlocation.getString("lat");
                                    String endlng=Endlocation.getString("lng");


                                    Locationlatlong locationlatlong=new Locationlatlong();
                                    locationlatlong.setStartlatitude(Double.parseDouble(strlat));
                                    locationlatlong.setStartlongitude(Double.parseDouble(strlng));
                                    locationlatlong.setEndlatitude(Double.parseDouble(endlat));
                                    locationlatlong.setEndlongitude(Double.parseDouble(endlng));
                                    latlonglist.add(locationlatlong);
                                }
                            }
                        }



                    } catch (final JSONException e) {


                    }
                } else {


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for(int k=0;k<latlonglist.size();k++){
                double lat1 = latlonglist.get(k).getStartlatitude()/ 180 * Math.PI;
                double lng1 = latlonglist.get(k).getStartlongitude() / 180 * Math.PI;
                double lat2 = latlonglist.get(k).getEndlatitude()/ 180 * Math.PI;
                double lng2 = latlonglist.get(k).getEndlongitude()/ 180 * Math.PI;

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

                lastAnchorNode = new AnchorNode();
//

                point2 = lastAnchorNode.getWorldPosition();

//                                    for(int i=0;i<xvalue.size();i++){
                point1 = new Vector3(valx,valy,coordinates);
//                lineBetweenPoints(point1,point2);

                addLineBetweenHits(point1,point2);

                arFragmentSupport.setOnTapArPlaneListener(
                        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

                        });

//                if (arFragmentSupport != null) {
//                    arFragmentSupport.setOnTapArPlaneListener(
//                            (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
//
//                                onPlaneTap(hitResult, plane, motionEvent);
//
//                            });
//                }

//
//                movingAnimation.setObjectValues(valx, valy);
//                movingAnimation.setPropertyName("localPosition");
//                movingAnimation.setEvaluator(new Vector3Evaluator());


//                Vector3 start = new Vector3(startAnchor.getPose().tx(), startAnchor.getPose().ty(), startAnchor.getPose().tz());
//                Vector3 end = new Vector3(endAnchor.getPose().tx(), endAnchor.getPose().ty(), endAnchor.getPose().tz());
//
////                transformableNode.setWorldPosition(new Vector3(valx,valy,-90));
////                                    }
////                                    point2 = anchorNode.getWorldPosition();
//
//
//                polyLineCode(lastAnchorNode,point1,point2);



//
            }
//


        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addLineBetweenHits(Vector3 point1, Vector3 point2) {
//        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);

        if (lastAnchorNode != null) {
            anchorNode.setParent(arFragmentSupport.getArSceneView().getScene());
//            Vector3 point1, point2;
//            point1 = lastAnchorNode.getWorldPosition();
//            point2 = anchorNode.getWorldPosition();

        /*
            First, find the vector extending between the two points and define a look rotation
            in terms of this Vector.
        */
            final Vector3 difference = Vector3.subtract(point2, point1);
            final Vector3 directionFromTopToBottom = difference.normalized();
//            final Quaternion rotationFromAToB =
//                    Quaternion.lookRotation(directionFromTopToBottom, Vector3.forward());
            MaterialFactory.makeOpaqueWithColor(getApplicationContext(), new com.google.ar.sceneform.rendering.Color(Color.BLUE))
                    .thenAccept(
                            material -> {
/* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
       to extend to the necessary length.  */
                                ModelRenderable model = ShapeFactory.makeCube(
                                        new Vector3(.01f, .01f, difference.length()),
                                        Vector3.zero(), material);
/* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
       the midpoint between the given points . */
                                Node node = new Node();
                                node.setParent(anchorNode);
                                node.setRenderable(model);
                                node.setWorldPosition(Vector3.add(point1, point2).scaled(.5f));
//                                node.setWorldRotation(rotationFromAToB);
                            }
                    );
            lastAnchorNode = anchorNode;
        }
    };



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void onPlaneTap(HitResult hitResult, Plane plane, MotionEvent motionEvent) {

        if (andyRenderable == null) {
            return;
        }
        // Create the Anchor.
        Anchor anchor = hitResult.createAnchor();

        // Create the starting position.

        if (startNode == null) {
            startNode = new AnchorNode(anchor);
            startNode.setParent(arFragmentSupport.getArSceneView().getScene());

            // Create the transformable andy and add it to the anchor.
            andy = new Node();
            andy.setParent(startNode);
            andy.setRenderable(andyRenderable);
        } else {
            // Create the end position and start the animation.
            endNode = new AnchorNode(anchor);
            endNode.setParent(arFragmentSupport.getArSceneView().getScene());
            startWalking();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startWalking() {
         objectAnimation= new ObjectAnimator();
        objectAnimation.setAutoCancel(true);

        objectAnimation.setTarget(andy);

        // All the positions should be world positions
        // The first position is the start, and the second is the end.

        objectAnimation.setObjectValues(andy.getWorldPosition(), point2);

        // Use setWorldPosition to position andy.
        objectAnimation.setPropertyName("worldPosition");

        // The Vector3Evaluator is used to evaluator 2 vector3 and return the next
        // vector3.  The default is to use lerp.
        objectAnimation.setEvaluator(new Vector3Evaluator());
        // This makes the animation linear (smooth and uniform).
        objectAnimation.setInterpolator(new LinearInterpolator());
        // Duration in ms of the animation.
        objectAnimation.setDuration(500);
        objectAnimation.start();
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


//        final Quaternion rotationFromAToB =
//                Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());


        MaterialFactory.makeOpaqueWithColor(this,new com.google.ar.sceneform.rendering.Color(Color.BLUE))
                .thenAccept(
                        material -> {
                            lineRenderable = ShapeFactory.makeCube(new Vector3(.02f, .02f, difference.length()),
                                    Vector3.zero(), material);

//                            Node background = new Node();
                            lineNode.setLocalPosition(Vector3.forward());
                            lineNode.setParent(anchorNode);
                            lineNode.setRenderable(lineRenderable);
                            lineNode.setLocalScale(new Vector3(.02f, .02f, difference.length()));

//                            ModelRenderable boxo = ShapeFactory.makeCube(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), material);
//                            lineNode.setRenderable(boxo);
                        });

//        lineNode.setParent(anchorNode);
////        lineNode.setLocalPosition(Vector3.add(point1, point2).scaled(.5f));
//        lineNode.setLocalRotation(rotationFromAToB);

        lastAnchorNode = anchorNode;


//        addObject(Uri.parse("directionarrow.sfb"),point1,point2);



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addObject(Uri parse, Vector3 point1, Vector3 point2) {
        Frame frame = arFragmentSupport.getArSceneView().getArFrame();
//        Point point = getScreenCenter();
        if (frame != null) {
            List<HitResult> hits = frame.hitTest((float) point1.x, (float) point2.y);

            for (int i = 0; i < hits.size(); i++) {
                Trackable trackable = hits.get(i).getTrackable();
                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hits.get(i).getHitPose())) {
                    placeObject(arFragmentSupport, hits.get(i).createAnchor(), parse);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private final void placeObject(final ArFragment fragment, final Anchor createAnchor, Uri model) {
        ModelRenderable.builder().setSource(fragment.getContext(), model).build().thenAccept((new Consumer() {
            public void accept(Object var1) {
                this.accept((ModelRenderable) var1);
            }

            public final void accept(ModelRenderable it) {
                if (it != null)
                    ArExampleActivity.this.addNode(arFragmentSupport, createAnchor, it);
            }
        })).exceptionally(new java.util.function.Function() {
            @Override
            public Object apply(Object o) {
                return null;
            }
        });
    }

    private void addNode(ArFragment fragment, Anchor createAnchor, ModelRenderable renderable) {

        AnchorNode anchorNode = new AnchorNode(createAnchor);
        TransformableNode transformableNode = new TransformableNode(fragment.getTransformationSystem());
        transformableNode.setRenderable(renderable);
        transformableNode.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }

//    private Point getScreenCenter() {
//        View vw = findViewById(android.R.id.content);
//        return new Point(vw.getWidth() / 2, vw.getHeight() / 2);
//    }
//    private void polyLineCode(AnchorNode anchorNode, Vector3 point1, Vector3 point2){
//
////        if (!isLineDrawn){
//
//        final Vector3 difference = Vector3.subtract(point1, point2);
//        final Vector3 directionFromTopToBottom = difference.negated();
//        final Quaternion rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.left());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            MaterialFactory.makeOpaqueWithColor(ArExampleActivity.this, new com.google.ar.sceneform.rendering.Color(Color.BLUE))
//                    .thenAccept(
//                            material -> {
//    /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
//           to extend to the necessary length.  */
//                                ModelRenderable model = ShapeFactory.makeCube(
//                                        new Vector3(.05f, .05f, difference.length()),
//                                        Vector3.zero(), material);
//
//    /* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
//           the midpoint between the given points . */
//                                Node node = new Node();
//                                node.setParent(anchorNode);
//                                node.setRenderable(model);
//                                node.setWorldPosition(Vector3.add(point1, point2).scaled(.15f));
////                                node.setWorldRotation(rotationFromAToB);
//
////                                    isLineDrawn = true;
//
//
//                            }
//                    );
//        }
//
//        lastAnchorNode = anchorNode;
//    }

    }



