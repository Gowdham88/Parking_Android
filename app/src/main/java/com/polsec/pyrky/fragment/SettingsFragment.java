package com.polsec.pyrky.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.polsec.pyrky.BuildConfig;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.adapter.CarouselAdapter;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.CircleTransformation;
import com.polsec.pyrky.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by thulirsoft on 7/21/18.
 */

public class SettingsFragment extends Fragment  implements EasyPermissions.PermissionCallbacks{

    EditText NameEdt,EmailEdt;
    ImageView ProfileImg;
    TextView Camera,Gallery,cancel,save;
    CircleImageView mProfileImage;
    private android.support.v7.app.AlertDialog dialog;
    LinearLayout cancelLay;
    int mCarouselCount;
    String mEmail,mName,mProfilepic,mUid,userName;
    String[] mCarCategory = { "Compact", "Small", "Mid size", "Full", "Van/Pick-up" };
    String[] mCarCategoryId = {"0", "1", "2", "3", "4" };
    String[] mCarranze = { "3.5 - 4.5m", "2.5 - 3.5m", "4 -5m", "5 - 5.5m", "5.5 - 6.5m" };
    int mIcons[] = {R.drawable.compactcar_icon,R.drawable.smallcar_icon,R.drawable.midsizecar_icon,R.drawable.fullcar_icon, R.drawable.vanpickupcar_icon};
    private static final int PERMISSIONS_REQUEST_CAMERA = 1888;
    private static final int PERMISSIONS_REQUEST_GALLERY = 1889;
    private static final String[] CAMERA = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private String selectedImagePath = "";
    final private int RC_PICK_IMAGE = 1;
    final private int RC_CAPTURE_IMAGE = 2;
    private Uri fileUri;
    String postimageurl = "";
    Uri contentURI;
    boolean isPhotoValid = false;
    private String mCurrentPhotoPath;
    private int avatarSize;
    private FirebaseAuth mAuth;
    ImageView BackImg;
    TextView  TitlaTxt;
    int mCarIcon;
     CarouselLayoutManager layoutManager;
    String Pass;
    LinearLayout LinLay;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mEmail = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_EMAIL);
        mName = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_USER_NAME);
        mProfilepic = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_PROFILE_PIC);
        mUid= PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        mCarIcon = Integer.parseInt(PreferencesHelper.getPreference(getActivity(),PreferencesHelper.PREFERENCE_PROFILE_CAR));
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.GONE);
//        ((AppCompatActivity)getActivity()).getActionBar().hide();
        BackImg=(ImageView) view.findViewById(R.id.back_icon);
        TitlaTxt=(TextView)view.findViewById(R.id.extra_title);
        TitlaTxt.setText("Settings");
        BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
//        toolbar = view.findViewById(R.id.toolbar);
        ProfileImg=view.findViewById(R.id.profile_img);
        mProfileImage = view.findViewById(R.id.profile_img);
        save = view.findViewById(R.id.save_button);
//        toolbarText.setText("Settings");
        NameEdt=view.findViewById(R.id.et_name);
        EmailEdt=view.findViewById(R.id.et_email);
        NameEdt.setText(mName);
        EmailEdt.setText(mEmail);
        LinLay=view.findViewById(R.id.scrollviewlin);
        LinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity());
            }
        });

        mAuth = FirebaseAuth.getInstance();
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        Pass =PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_LOGGED_INPASS);

        if (mProfilepic != null && !mProfilepic.isEmpty()) {
            Picasso.with(getActivity())
                    .load(mProfilepic)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(mProfileImage);
        }

        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(1);;

//        layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {
//            @Override
//            public void onCenterItemChanged(int adapterPosition) {
//                int pos=adapterPosition;
//
//                Toast.makeText(getActivity(),String.valueOf(pos) , Toast.LENGTH_SHORT).show();
//            }
//        });


        final RecyclerView recyclerView = view.findViewById(R.id.carousel_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new CarouselAdapter(getActivity(), mIcons, mCarCategory,mCarranze,mCarIcon));
        recyclerView.addOnScrollListener(new CenterScrollListener());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mCarIcon = layoutManager.getCenterItemPosition();
                }
            });
        }
        recyclerView.scrollToPosition(mCarIcon);

        ProfileImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheet();
                    }
                });

        NameEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUp();
            }
        });
        String profileimg= PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_PROFILE_PIC);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveUserImage(postimageurl);

//                saveUserImage(profileimg);
                String name = NameEdt.getText().toString().trim();
                String email = EmailEdt.getText().toString().trim();

                updateData(email,mCarCategoryId[mCarIcon],name);





            }
        });

        return view;

    }
    private void PopUp() {


        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.edit_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(deleteDialogView);
        Button ok = (Button)deleteDialogView.findViewById(R.id.ok_button);
        Button cancel = (Button)deleteDialogView.findViewById(R.id.cancel_button);
        final EditText EdtPfl = (EditText)deleteDialogView.findViewById(R.id.profile_edt);
        EdtPfl.setText(userName);
        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Str=EdtPfl.getText().toString();
                        if(!Str.isEmpty()||!Str.equals(null)){
                            saveUserName(Str,alertDialog1);
                        }else{
                            Toast.makeText(getActivity(), "Please edit the name", Toast.LENGTH_SHORT).show();
                        }
//                Intent intent = new Intent(UserProfileActivity.this,LoginScreen.class);
//                startActivity(intent);
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

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());

        lp.gravity = Gravity.CENTER;

        alertDialog1.getWindow().setAttributes(lp);

    }
    public void saveUserName(final String name, final AlertDialog dialog) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("users").document(mUid);


        ref.update("username", name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
//                        NameEdt .setText(name);
                        Toast.makeText(getActivity(), "Update Successfully", Toast.LENGTH_SHORT).show();
                        PreferencesHelper.setPreference(getActivity(), PreferencesHelper.PREFERENCE_USER_NAME, name);
                        dialog.dismiss();
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
                Toast.makeText(getActivity(),"Update failed",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });

    }
    private void showBottomSheet() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View bottomSheetView = factory.inflate(R.layout.dialo_camera_bottomsheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();


        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheetView.getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) bottomSheetView.getParent()).setBackgroundColor(Color.TRANSPARENT);

        Camera = bottomSheetView.findViewById(R.id.camera_title);
        Gallery = bottomSheetView.findViewById(R.id.gallery_title);
        cancel = bottomSheetView.findViewById(R.id.cancel_txt);
        cancelLay = bottomSheetView.findViewById(R.id.cance_lay);
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();

                if (hasPermissions()) {
                    captureImage();
                } else {
                    EasyPermissions.requestPermissions(getActivity(), "Permissions required", PERMISSIONS_REQUEST_CAMERA, CAMERA);
                }
            }
        });

        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasPermissions()) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RC_PICK_IMAGE);
                } else {
                    EasyPermissions.requestPermissions(getActivity(), "Permissions required", PERMISSIONS_REQUEST_GALLERY, CAMERA);
                }
                bottomSheetDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        cancelLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        switch (requestCode){

            case PERMISSIONS_REQUEST_GALLERY:
                if(perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RC_PICK_IMAGE);
                }
                break;

            case PERMISSIONS_REQUEST_CAMERA:
                if(perms.contains(Manifest.permission.CAMERA)) {
                    captureImage();
                }
                break;

        }


    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }

    }



    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(getActivity(), CAMERA);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, RC_CAPTURE_IMAGE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return FileProvider.getUriForFile(getActivity(),
                BuildConfig.APPLICATION_ID + ".provider",
                getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Constants.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + mediaFile.getAbsolutePath();

        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == RC_PICK_IMAGE) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    isPhotoValid = true;
                    this.contentURI = contentURI;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                        ProfileImg.setImageBitmap(bitmap);
                        selectedImagePath=getRealPathFromURI(contentURI);
                        uploadImage(getRealPathFromURI(contentURI));

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == RC_CAPTURE_IMAGE) {
                // Show the thumbnail on ImageView
//                showProgressDialog();
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                this.contentURI = imageUri;
                isPhotoValid = true;
                File file = new File(imageUri.getPath());
                try {
                    InputStream ims = new FileInputStream(file);
                    ProfileImg.setImageBitmap(BitmapFactory.decodeStream(ims));
                } catch (FileNotFoundException e) {
                    return;
                }

                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(getActivity(),
                        new String[]{imageUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                selectedImagePath = imageUri.getPath();
                uploadImage(imageUri.getPath());

            }

        } else {
            super.onActivityResult(requestCode, resultCode,
                    data);
        }
    }



    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    public void uploadImage(final String view) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        if(contentURI != null)
        {
//            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();

            Uri file = Uri.fromFile(new File(selectedImagePath));
            StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
//            uploadTask = riversRef.putFile(file);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString()+".jpg");
            UploadTask uploadTask = riversRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressDialog.dismiss();
//                    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                    postimageurl =taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return riversRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            postimageurl = task.getResult().toString();
                            final FirebaseUser user = mAuth.getCurrentUser();
                            saveUserImage(postimageurl);
//                            AddDatabase(user,view);
                        }
                    });


                    Log.e("postimageurl",postimageurl);

                    if (postimageurl.equals("failed")) {


                        return;
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    postimageurl = "failed";
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
//                            progressDialog.setMessage("Uploading "+(int)progress+"%");
                        }
                    });

        } else {
            return ;
        }
    }


    private void updateData(final String email, String carCategory,String userName) {
        showProgressDialog();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference contact = db.collection("users").document(PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_FIREBASE_UUID));
//        mProfilepic = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_PROFILE_PIC);
        contact.update("email", email);
        contact.update("username",userName);
        contact.update("profileImageURL",mProfilepic);
        contact.update("carCategory", carCategory)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressDialog();
                        PreferencesHelper.setPreference(getActivity(),PreferencesHelper.PREFERENCE_EMAIL,email);
                        PreferencesHelper.setPreference(getActivity(),PreferencesHelper.PREFERENCE_PROFILE_CAR, String.valueOf(carCategory));
                        Toast.makeText(getActivity(), "Updated Successfully",
                                Toast.LENGTH_SHORT).show();

//                        getActivity().finish();
//                        startActivity(getActivity().getIntent());
                    }
                });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(),Pass); // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                        //Now change your email address \\
                        //----------------Code for Changing Email Address----------\\
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User email address updated.");
                                        }
                                    }
                                });
                        //----------------------------------------------------------\\
                    }
                });


    }

    public void saveUserImage(final String postimageurl) {
//        showProgressDialog();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("users").document(mUid);

        ref.update("profileImageURL", postimageurl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        if(!postimageurl.equals(null)&&!postimageurl.isEmpty()){
                            Picasso.with(getActivity())
                                    .load(postimageurl)
                                    .resize(avatarSize, avatarSize)
                                    .centerCrop()
                                    .transform(new CircleTransformation())
                                    .into(ProfileImg);
                        }
                        else{
//            Picasso.with(this)
//                    .load(profilePhoto)
//                    .placeholder(R.drawable.background)
//                    .resize(avatarSize, avatarSize)
//                    .centerCrop()
//                    .transform(new CircleTransformation())
//                    .into(ivUserProfilePhoto);
                        }
                        Toast.makeText(getActivity(), "Update Successfully", Toast.LENGTH_SHORT).show();
                        PreferencesHelper.setPreference(getActivity(), PreferencesHelper.PREFERENCE_PROFILE_PIC, postimageurl);
//                        hideProgressDialog();
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
                Toast.makeText(getActivity(),"Update failed",Toast.LENGTH_SHORT).show();
//                hideProgressDialog();
//                dialog.dismiss();
            }

        });

    }
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
