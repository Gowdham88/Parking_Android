package com.polsec.pyrky.activity.ar;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.polsec.pyrky.R;
import com.polsec.pyrky.helper.CameraPermissionHelper;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class ArActivity extends AppCompatActivity

{
    Context context = this;
    ArFragment arFragment;
    Frame frame;
    Plane plane;
    Iterator<Plane> planes;

    ArrayList<String> xvalue=new ArrayList<>();
    ArrayList<String> yvalue=new ArrayList<>();
    ArrayList<String> zvalue=new ArrayList<>();
    Vector3 point1, point2;
//    A x,y,degree;
    double degree;
    Anchor modelAnchor;
    String sLatLng,dLatLng;
    private ModelRenderable andyRenderable;
    Anchor anchor;
    AnchorNode lastAnchorNode;
    Session mSession;
    boolean isLineDrawn = false;
//    private LocationScene locationScene;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
//        sLatLng = getIntent().getStringExtra("slatlng");
//        dLatLng =  getIntent().getStringExtra("dlatlng");
        xvalue =getIntent().getStringArrayListExtra("xvalue");
        yvalue =getIntent().getStringArrayListExtra("yvalue");
        zvalue =getIntent().getStringArrayListExtra("zvalue");

        Log.e("xvalue", String.valueOf(xvalue));
        Log.e("yvalue", String.valueOf(yvalue));
        Log.e("zvalue", String.valueOf(zvalue));

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

//        Directions_call();

        for(int k=0;k<zvalue.size();k++){
            degree= Double.parseDouble(zvalue.get(k));
        }

        makePolyLine("directionarrow.sfb");


        if (mSession!=null){

            try {
                mSession = new Session(context);
            } catch (UnavailableArcoreNotInstalledException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UnavailableApkTooOldException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UnavailableSdkTooOldException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        if (arFragment != null) {
            arFragment.setOnTapArPlaneListener(
                    (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

//                        addLineBetweenHits(hitResult, plane, motionEvent);

                    });
        }


    }

    private void addLineBetweenHits(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        lastAnchorNode = new AnchorNode();
        if (lastAnchorNode != null) {
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            Vector3 point1, point2;
            point1 = lastAnchorNode.getWorldPosition();
            point2 = anchorNode.getWorldPosition();

        /*
            First, find the vector extending between the two points and define a look rotation
            in terms of this Vector.
        */
            final Vector3 difference = Vector3.subtract(point1, point2);
            final Vector3 directionFromTopToBottom = difference.normalized();
            final Quaternion rotationFromAToB =
                    Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                MaterialFactory.makeOpaqueWithColor(getApplicationContext(), new Color())
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
                                    node.setWorldPosition(Vector3.add(point1, point2).scaled(.15f));
                                    node.setWorldRotation(rotationFromAToB);
                                }
                        );
            }
            lastAnchorNode = anchorNode;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // ARCore requires camera permission to operate.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }

    }

    private Vector3 screenCenter() {
        View vw = findViewById(android.R.id.content);
        return new Vector3(vw.getWidth() / 2f, vw.getHeight() / 2f, 0f);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                    .show();
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this);
            }
            finish();
        }
    }


    private void makePolyLine(String direction) {

        //Create the arrow renderable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ModelRenderable.builder()
                    //get the context of the ARFragment and pass the name of your .sfb file
                    .setSource(context, Uri.parse(direction))
                    .build()
                    .thenAcceptAsync(modelRenderable -> andyRenderable = modelRenderable);
            Toast.makeText(context, direction, Toast.LENGTH_SHORT).show();
//                    .exceptionally(throwable -> {Toast toast = Toast.makeText(this,"Unable to load renderable ", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();});
                    //I accepted the CompletableFuture using Async since I created my model on creation of the activity. You could simply use .thenAccept too.
                    //Use the returned modelRenderable and save it to a global variable of the same name
        }

        arFragment.getArSceneView().getScene().addOnUpdateListener(new Scene.OnUpdateListener() {
            @Override
            public void onUpdate(FrameTime frameTime) {



                //get the frame from the scene for shorthand
                Frame frame = arFragment.getArSceneView().getArFrame();

                if (frame != null) {
                    //get the trackables to ensure planes are detected
                    planes = frame.getUpdatedTrackables(Plane.class).iterator();

                    while (planes.hasNext()) {

                        Plane plane = planes.next();
                        //If a planes has been detected & is being tracked by ARCore
                        if (plane.getTrackingState() == TrackingState.TRACKING) {

                            //Hide the planes discovery helper animation
                            arFragment.getPlaneDiscoveryController().hide();

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
                                    anchorNode.setParent(arFragment.getArSceneView().getScene());


                                    TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
                                    transformableNode.setParent(anchorNode);
                                    transformableNode.setRenderable(ArActivity.this.andyRenderable);

//                                    float xx = modelAnchor.getPose().tx();
//                                    float yy = modelAnchor.getPose().compose(Pose.makeTranslation(0f, 0f, 0f)).ty();
//                                    float zz = modelAnchor.getPose().tz();
//                                    transformableNode.setWorldPosition(new Vector3(xx,yy,zz));

                                    lastAnchorNode = new AnchorNode();


                                    point2 = lastAnchorNode.getWorldPosition();

                                    for(int i=0;i<xvalue.size();i++){
                                        point1 = new Vector3(Float.parseFloat(xvalue.get(i)),Float.parseFloat(yvalue.get(i)),-90);

                                        transformableNode.setWorldPosition(new Vector3(Float.parseFloat(xvalue.get(i)),Float.parseFloat(yvalue.get(i)),-90));
                                    }
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

        if (!isLineDrawn){

            final Vector3 difference = Vector3.subtract(point1, point2);
            final Vector3 directionFromTopToBottom = difference.negated();
            final Quaternion rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.left());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                MaterialFactory.makeOpaqueWithColor(getApplicationContext(), new Color(android.graphics.Color.BLUE))
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
                                    node.setWorldPosition(Vector3.add(point1, point2).scaled(.15f));
//                                node.setWorldRotation(rotationFromAToB);

                                    isLineDrawn = true;
                                }
                        );
            }
        }

    }



}


