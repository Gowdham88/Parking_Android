package com.pyrky_android.activity.ViewImage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pyrky_android.R;

import java.util.Locale;

public class ViewImageActivity extends AppCompatActivity {
    TextView BookBtn;
    ImageView CloseImg;
    double latitude,longitude;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
//        BackImg = (ImageView) findViewById(R.id.back_image);
//        BackImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitude = extras.getDouble("latitude");
            longitude= extras.getDouble("longitude");

            Log.e("lattitudeview", String.valueOf(latitude));
                Log.e("longitudeview", String.valueOf(longitude));


//            Toast.makeText(ViewImageActivity.this, latitude, Toast.LENGTH_SHORT).show();

            //The key argument here must match that used in the other activity
        }


        BookBtn=(TextView)findViewById(R.id.book_btn);
        BookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet(latitude,longitude);
            }
        });
        CloseImg=(ImageView)findViewById(R.id.close_iconimg);
        CloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private boolean isPackageInstalled() {
        try
        {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    private void showBottomSheet(double latitude, double longitude) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ViewImageActivity.this);
        LayoutInflater factory = LayoutInflater.from(ViewImageActivity.this);
        View bottomSheetView = factory.inflate(R.layout.mapspyrky_bottomsheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        TextView map = (TextView) bottomSheetView.findViewById(R.id.maps_title);
        TextView pyrky = (TextView) bottomSheetView.findViewById(R.id.pyrky_title);
//        GalleryIcon = (ImageView) bottomSheetView.findViewById(R.id.gallery_icon);
//        CameraIcon = (ImageView) bottomSheetView.findViewById(R.id.camera_image);
        TextView cancel = (TextView) bottomSheetView.findViewById(R.id.cancel_txt);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PackageManager pm = ViewImageActivity.this.getPackageManager();
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
//

//                Intent postintent = new Intent(getActivity(), PostActivity.class);
//                startActivity(postintent);
                bottomSheetDialog.dismiss();
//
//                if (hasPermissions()) {
//                    captureImage();
//                } else {
//                    EasyPermissions.requestPermissions(getActivity(), "Permissions required", PERMISSIONS_REQUEST_CAMERA, CAMERA);
//                }
            }
        });

        pyrky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (hasPermissions()) {
//                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, RC_PICK_IMAGE);
//                } else {
//                    EasyPermissions.requestPermissions(getActivity(), "Permissions required", PERMISSIONS_REQUEST_GALLERY, CAMERA);
//                }
//                Intent checkinintent = new Intent(getActivity(), CheckScreenActivity.class);
//                startActivity(checkinintent);
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


}
