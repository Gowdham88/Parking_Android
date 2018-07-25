package com.pyrky_android.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.pyrky_android.R;
import com.pyrky_android.pojo.Users;
import com.pyrky_android.adapter.CarouselAdapter;
import com.pyrky_android.preferences.PreferencesHelper;
import com.pyrky_android.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "signup";
    private final int PICK_IMAGE_REQUEST = 71;
    private FirebaseAuth mAuth;
    private AlertDialog dialog;

    Context mContext = this;
    ProgressDialog progressDialog;
    Spinner mSpinner;
    int mCarouselCount = 0;
    String[] mCarCategory = { "Compact", "Small", "Mid size", "Full", "Van/Pick-up" };
    String[] mCarCategoryId = { "1", "2", "3", "4", "5" };
    String[] mCarCategoryImages = {"compactCar", "smallCar", "mediumCar", "fullCar", "vanPickup"};
    String[] carCategoryMeter = {"3.5 - 4.5m", "2.5 - 3.5m", "4 - 5m", "5 - 5.5m", "5.5 - 6.5m"};

    int mIcons[] = {R.drawable.compactcar_icon,R.drawable.smallcar_icon,R.drawable.midsizecar_icon,R.drawable.fullcar_icon, R.drawable.vanpickupcar_icon};
    TextInputEditText mEmail,mPassword,mUserName;
    ImageView mProfileImage;
    ImagePicker mImagePicker;
    Uri mTempImageUrl;
    String mProfileImageUrl = "";
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    UploadTask uploadTask;
    String email,password,username;
    @Override
    protected void onStart() {
        super.onStart();
        //If the user already logged in, the screen should navigate to HomeActivity
        if (PreferencesHelper.getPreferenceBoolean(mContext,PreferencesHelper.PREFERENCE_ISLOGGEDIN)){
            final Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            SignUpActivity.this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Image picker
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        RelativeLayout parentLayout = findViewById(R.id.signup_parent_layout);
        TextView toSignIn = findViewById(R.id.already_have_account);
        Button signUp = findViewById(R.id.sign_up_button);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mUserName = findViewById(R.id.et_user_name);
        mProfileImage = findViewById(R.id.profile_img);


        mImagePicker = new ImagePicker(SignUpActivity.this,null,imageUri -> {mProfileImage.setImageURI(imageUri);})
                .setWithImageCrop(1,1);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showBottomSheet();
            }
        });


        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
               if (!hasFocus){
//                   Utils.hideKeyboard(SignUpActivity.this);
                   hideKeyboard(v);
               }
           }
       });
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
//                    Utils.hideKeyboard(SignUpActivity.this);
                    hideKeyboard(v);
                }
            }
        });
        mUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
//                    Utils.hideKeyboard(SignUpActivity.this);
                    hideKeyboard(v);
                }
            }
        });

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImagePicker.choosePicture(true);
            }
        });
//        signUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Utils.hideKeyboard(SignUpActivity.this);
//
//                email = mEmail.getText().toString();
//                username = mUserName.getText().toString().trim();
//                password = mPassword.getText().toString().trim();
//
//
//
//                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(username)) {
//                    Toast.makeText(SignUpActivity.this, "Enter email address and username.", Toast.LENGTH_SHORT).show();
//
//                }
//                else if((email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()))
//                {
//                    Toast.makeText(SignUpActivity.this, "enter a valid email address", Toast.LENGTH_SHORT).show();
////            mEmail.setError("enter a valid email address");
//
//                }
//                else if(username.equals(null) && username.isEmpty()){
//                    Toast.makeText(SignUpActivity.this, "Please enter the user name", Toast.LENGTH_SHORT).show();
//                }
//                else if(password.length()<6){
//                    Toast.makeText(SignUpActivity.this, "Please enter the user name more than 6 charecters", Toast.LENGTH_SHORT).show();
//                }
//
//                else if(password.contains(" ")){
//                    Toast.makeText(SignUpActivity.this, "Please remove the space", Toast.LENGTH_SHORT).show();
//                }
//
//                else if(postimageurl != null)
//                {
//                    uploadImage(view);
//
//
//                } else {
//
//
//
//                    AddDatabase(email,username,postimageurl);
//
//                }
//
//              signUp(mEmail.getText().toString().trim(),mPassword.getText().toString().trim(),mUserName.getText().toString().trim(), mProfileImageUrl);
//            }
//        });
        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail.setEnabled(true);
            }
        });
        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword.setEnabled(true);
            }
        });
        mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName.setEnabled(true);
            }
        });
        if (!mEmail.isFocused()||!mPassword.isFocused()||!mUserName.isFocused()){
            Utils.hideKeyboard(SignUpActivity.this);
        }
//        Spinner mSpinner = findViewById(R.id.car_category);
//        mSpinner.setAdapter(new MySpinnerAdapter(SignUpActivity.this, R.layout.item_carousel,
//                mCarCategory));

        //Carousel
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(1);

        final RecyclerView recyclerView = findViewById(R.id.carousel_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new CarouselAdapter(this, mIcons, mCarCategory));
        recyclerView.addOnScrollListener(new CenterScrollListener());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mCarouselCount = layoutManager.getCenterItemPosition();

                }
            });
        }

        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                SignUpActivity.this.finish();
            }
        });

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null) {

            currentUser.unlink(currentUser.getProviderId());
//            LoginManager.getInstance().logOut();
            mAuth.signOut();

        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        // Forward results to EasyPermissions
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    private void showBottomSheet() {
//
//        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SignUpActivity.this);
//        LayoutInflater factory = LayoutInflater.from(this);
//        View bottomSheetView = factory.inflate(R.layout.dialo_camera_bottomsheet, null);
//        bottomSheetDialog.setContentView(bottomSheetView);
//        bottomSheetDialog.show();
//
//        Camera = (TextView) bottomSheetView.findViewById(R.id.camera_title);
//        Gallery = (TextView) bottomSheetView.findViewById(R.id.gallery_title);
//        cancel = (TextView)bottomSheetView.findViewById(R.id.cancel_txt);
//        cancelLay = (LinearLayout) bottomSheetView.findViewById(R.id.cance_lay);
//        Camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                bottomSheetDialog.dismiss();
//
//                if (hasPermissions()) {
//                    captureImage();
//                } else {
//                    EasyPermissions.requestPermissions(SignupScreenActivity.this, "Permissions required", PERMISSIONS_REQUEST_CAMERA, CAMERA);
//                }
//            }
//        });
//
//        Gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (hasPermissions()) {
//                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, RC_PICK_IMAGE);
//                } else {
//                    EasyPermissions.requestPermissions(SignupScreenActivity.this, "Permissions required", PERMISSIONS_REQUEST_GALLERY, CAMERA);
//                }
//                bottomSheetDialog.dismiss();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetDialog.dismiss();
//            }
//        });
//        cancelLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetDialog.dismiss();
//            }
//        });
//
//    }
//
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> perms) {
//
//        switch (requestCode){
//
//            case PERMISSIONS_REQUEST_GALLERY:
//                if(perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, RC_PICK_IMAGE);
//                }
//                break;
//
//            case PERMISSIONS_REQUEST_CAMERA:
//                if(perms.contains(Manifest.permission.CAMERA)) {
//                    captureImage();
//                }
//                break;
//
//        }
//
//
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
//
//        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
//        // This will display a dialog directing them to enable the permission in app settings.
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this).build().show();
//        }
//
//    }
//
//
//
//    private boolean hasPermissions() {
//        return EasyPermissions.hasPermissions(SignupScreenActivity.this, CAMERA);
//    }
//
//    private void captureImage() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        fileUri = getOutputMediaFileUri(1);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        startActivityForResult(intent, RC_CAPTURE_IMAGE);
//    }
//
//    public Uri getOutputMediaFileUri(int type) {
//        return FileProvider.getUriForFile(getApplicationContext(),
//                BuildConfig.APPLICATION_ID + ".provider",
//                getOutputMediaFile(type));
//    }
//
//    private File getOutputMediaFile(int type) {
//
//        File mediaStorageDir = new File(
//                Environment
//                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                Constants.IMAGE_DIRECTORY_NAME);
//
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d(TAG, "Oops! Failed create "
//                        + Constants.IMAGE_DIRECTORY_NAME + " directory");
//                return null;
//            }
//        }
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");
//        } else {
//            return null;
//        }
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + mediaFile.getAbsolutePath();
//
//        return mediaFile;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode,
//                                 Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        showProgressDialog();
//        if (resultCode != Activity.RESULT_CANCELED) {
//            if (requestCode == RC_PICK_IMAGE) {
//                if (data != null) {
//                    Uri contentURI = data.getData();
//                    isPhotoValid = true;
//                    this.contentURI = contentURI;
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), contentURI);
//                        profileImg.setImageBitmap(bitmap);
//                        selectedImagePath=getRealPathFromURI(contentURI);
////                        uploadImage(getRealPathFromURI(contentURI));
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            } else if (requestCode == RC_CAPTURE_IMAGE) {
//                // Show the thumbnail on ImageView
////                showProgressDialog();
//                Uri imageUri = Uri.parse(mCurrentPhotoPath);
//                this.contentURI = imageUri;
//                isPhotoValid = true;
//                File file = new File(imageUri.getPath());
//                try {
//                    InputStream ims = new FileInputStream(file);
//                    profileImg.setImageBitmap(BitmapFactory.decodeStream(ims));
//                } catch (FileNotFoundException e) {
//                    return;
//                }
//
//                // ScanFile so it will be appeared on Gallery
//                MediaScannerConnection.scanFile(getApplicationContext(),
//                        new String[]{imageUri.getPath()}, null,
//                        new MediaScannerConnection.OnScanCompletedListener() {
//                            public void onScanCompleted(String path, Uri uri) {
//                            }
//                        });
//                selectedImagePath = imageUri.getPath();
////                uploadImage(imageUri.getPath());
//
//            }
//
//        } else {
//            super.onActivityResult(requestCode, resultCode,
//                    data);
//        }
//    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager inputMethodManager =(InputMethodManager )getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void signUp(String email, String password, String userName, String mProfileImageUrl){
        showProgressDialog();
        if (validateForm()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.getIdToken(true)
                                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                if (task.isSuccessful()) {
                                                    final FirebaseUser user = mAuth.getCurrentUser();

                                                    AddDatabase(user,email,password,userName, mProfileImageUrl);
                                                    Log.e("user", String.valueOf(user));
                                                    // Sign in success, update UI with the signed-in user's information
                                                }
                                            }
                                        });
                                hideProgressDialog();
                                Toast.makeText(mContext, "Successfully Signed in " + user.getEmail(), Toast.LENGTH_LONG).show();
//                            updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                hideProgressDialog();
//                            updateUI(null);
                            }

                            // ...
                        }
                    });
        }else{
            hideProgressDialog();
        }
    }

    private void AddDatabase(final FirebaseUser user, String email, String password, String userName, String mProfileImageUrl){
        final Users users = new Users(userName,email,mProfileImageUrl, mCarCategoryId[mCarouselCount]);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//        hideProgressDialog();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){

                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getEmail());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC,mProfileImageUrl);
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());
                    PreferencesHelper.setPreference(getApplicationContext(),PreferencesHelper.PREFERENCE_PROFILE_CAR, String.valueOf(mCarouselCount));
                    PreferencesHelper.setPreferenceBoolean(getApplicationContext(),PreferencesHelper.PREFERENCE_ISLOGGEDIN,true);

                    final Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    SignUpActivity.this.finish();

                } else {
                    db.collection("users").document(user.getUid())
                            .set(users)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Error", "Error adding document", e);
//                                    hideProgressDialog();
                                    Toast.makeText(mContext,"Login failed",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            });
                    hideProgressDialog();
//                    progressDialog.dismiss();
                    final Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SignUpActivity.this.finish();
                    overridePendingTransition(0, 0);

                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getEmail());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC,mProfileImageUrl);
                    PreferencesHelper.setPreferenceBoolean(getApplicationContext(),PreferencesHelper.PREFERENCE_ISLOGGEDIN,true);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.w("Error", "Error adding document", e);
                Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void uploadImage() {

        if(mTempImageUrl != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String profileImageUrl = UUID.randomUUID().toString();
            Uri file = Uri.fromFile(new File(String.valueOf(mTempImageUrl)));
            PreferencesHelper.setPreference(this,PreferencesHelper.PREFERENCE_PROFILE_PIC,profileImageUrl);

            // Create file metadata including the content type
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();

            StorageReference ref = storageReference.child("profile_image/"+ mTempImageUrl.getLastPathSegment());
            uploadTask = ref.putFile(mTempImageUrl);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, taskSnapshot.getUploadSessionUri().toString(), Toast.LENGTH_SHORT).show();
                    downloadUrl();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploading "+(int)progress+"%");
                }
            });

        }
    }
    private void deleteField(FirebaseFirestore db, String uid){

        DocumentReference docRef = db.collection("users").document(uid);

        // Remove the 'capital' field from the document
        Map<String,Object> updates = new HashMap<>();
        updates.put("password", FieldValue.delete());

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            // [START_EXCLUDE]
            @Override
            public void onComplete(@NonNull Task<Void> task) {}
            // [START_EXCLUDE]
        });
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void hideProgressDialog(){
        if(progressDialog!=null)
            progressDialog.dismiss();
    }
    private void downloadUrl(){
        final StorageReference ref = storageReference.child("profile_image/"+ mTempImageUrl.getLastPathSegment());
        uploadTask = ref.putFile(mTempImageUrl);
    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
        @Override
        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
            if (!task.isSuccessful()) {
                throw task.getException();
            }


            // Continue with the task to get the download URL
            return ref.getDownloadUrl();
        }
    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                mProfileImageUrl = String.valueOf(downloadUri);
                PreferencesHelper.setPreference(mContext,PreferencesHelper.PREFERENCE_PROFILE_PIC,mProfileImageUrl);
                Toast.makeText(mContext, downloadUri.toString(), Toast.LENGTH_SHORT).show();
            } else {
                // Handle failures
                // ...
            }
        }
    });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString().trim();
        String username = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {

            valid = true;

        } else {

            if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter email address and password.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if((email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()))
            {
                Toast.makeText(getApplicationContext(), "enter a valid email address", Toast.LENGTH_SHORT).show();
//            mEmail.setError("enter a valid email address");
                valid = false;
            }else if (username.isEmpty()&&username.equals(null)) {
                Toast.makeText(this, "Enter username.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if (TextUtils.isEmpty(password) || password.length()<4) {
                Toast.makeText(this, "Enter valid password.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, "" +
                        "Enter Username", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else {
                Toast.makeText(this, "Something wrong with the credentials", Toast.LENGTH_SHORT).show();
                valid = false;
            }


        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mImagePicker.handleActivityResult(resultCode,requestCode,data);
        File file = mImagePicker.getImageFile();
        if (file!=null){
            mTempImageUrl = Uri.fromFile(file);
            uploadImage();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mImagePicker.handlePermission(requestCode, grantResults);
    }
}
