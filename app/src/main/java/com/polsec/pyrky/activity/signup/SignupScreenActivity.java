package com.polsec.pyrky.activity.signup;

import android.Manifest;
import android.app.Activity;
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
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.polsec.pyrky.BuildConfig;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.activity.forgotpassword.ForgotpasswordActivity;
import com.polsec.pyrky.activity.signin.SignInActivity;
import com.polsec.pyrky.adapter.CarouselSignupAdapter;
import com.polsec.pyrky.pojo.Users;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.Constants;
import com.polsec.pyrky.utils.Utils;

import java.io.ByteArrayOutputStream;
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

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class SignupScreenActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    EditText mEtEmail, mEtUsername, mEtPassword;
    TextView SignupBtn;
    TextView AccntTxt;
    LinearLayout cancelLay;
    CircleImageView profileImg;
    TextView Camera,Gallery,cancel;
    RelativeLayout Signuprellay;
    private FirebaseAuth mAuth;
    private AlertDialog dialog;
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
    int mCarouselCount = 0;
    String[] mCarCategory = { "Compact", "Small", "Mid size", "Full", "Van/Pick-up" };
    String[] mCarCategoryId = {"0", "1", "2", "3", "4" };
    String[] mCarranze = { "3.5 - 4.5m", "2.5 - 3.5m", "4 -5m", "5 - 5.5m", "5.5 - 6.5m" };
    int mIcons[] = {R.drawable.compactcar_icon,R.drawable.smallcar_icon,R.drawable.midsizecar_icon,R.drawable.fullcar_icon, R.drawable.vanpickupcar_icon};
    LinearLayout signupScrlin;
    Button imageUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEtEmail = findViewById(R.id.et_email);
        mEtUsername = findViewById(R.id.et_user_name);
        mEtPassword = findViewById(R.id.et_password);
        AccntTxt = findViewById(R.id.already_have_account);
        Signuprellay = findViewById(R.id.signup_parent_layout);
        signupScrlin = findViewById(R.id.scrollviewlin);

        Signuprellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideSoftKeyboard(SignupScreenActivity.this);

            }
        });
//        dialog.setCanceledOnTouchOutside(false);
        signupScrlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(SignupScreenActivity.this);
            }
        });
        SignupBtn = findViewById(R.id.sign_up_button);
        profileImg = findViewById(R.id.profile_img);

//        mEtEmail.setInputType(mEtEmail.getInputType()
//                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
//                | EditorInfo.TYPE_TEXT_VARIATION_FILTER);
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(1);

        final RecyclerView recyclerView = findViewById(R.id.carousel_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new CarouselSignupAdapter(this, mIcons, mCarCategory,mCarranze));
        recyclerView.addOnScrollListener(new CenterScrollListener());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mCarouselCount = layoutManager.getCenterItemPosition();
                }
            });
        }
        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Utils.hideKeyboard(SignupScreenActivity.this);
                createAccount(mEtEmail.getText().toString().trim(), mEtPassword.getText().toString().trim(), mEtUsername.getText().toString(),view);
                PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_LOGGED_INPASS, mEtPassword.getText().toString().trim());
            }
        });
        AccntTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupScreenActivity.this,SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_righ);
                finish();

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void showBottomSheet() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SignupScreenActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
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
        Camera.setOnClickListener(view -> {

            bottomSheetDialog.dismiss();

            if (hasPermissions()) {
                captureImage();
            } else {
                EasyPermissions.requestPermissions(SignupScreenActivity.this, "Permissions required", PERMISSIONS_REQUEST_CAMERA, CAMERA);
            }
        });

        Gallery.setOnClickListener(view -> {
            if (hasPermissions()) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RC_PICK_IMAGE);
            } else {
                EasyPermissions.requestPermissions(SignupScreenActivity.this, "Permissions required", PERMISSIONS_REQUEST_GALLERY, CAMERA);
            }
            bottomSheetDialog.dismiss();
        });
        cancel.setOnClickListener(view -> bottomSheetDialog.dismiss());
        cancelLay.setOnClickListener(view -> bottomSheetDialog.dismiss());
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
        return EasyPermissions.hasPermissions(SignupScreenActivity.this, CAMERA);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, RC_CAPTURE_IMAGE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return FileProvider.getUriForFile(getApplicationContext(),
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
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), contentURI);
                        profileImg.setImageBitmap(bitmap);
                        selectedImagePath=getRealPathFromURI(contentURI);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == RC_CAPTURE_IMAGE) {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                this.contentURI = imageUri;
                isPhotoValid = true;
                File file = new File(imageUri.getPath());
                try {
                    InputStream ims = new FileInputStream(file);
                    profileImg.setImageBitmap(BitmapFactory.decodeStream(ims));
                } catch (FileNotFoundException e) {
                    return;
                }

                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{imageUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                selectedImagePath = imageUri.getPath();
            }

        } else {
            super.onActivityResult(requestCode, resultCode,
                    data);
        }
    }

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

    private void createAccount(final String email, final String password, final String username, final View view) {

        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(username)) {

            alertpopup();
//                Toast.makeText(this, "E-mail or Password or Username should not be empty", Toast.LENGTH_SHORT).show();
//                valid = false;
        }
        else if((email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()))
        {

            alertEmailpopup();
//            Toast.makeText(this, "Enter valid e-mail address", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(password) || password.length()<6) {

            alertPasswordpopup();
//            Toast.makeText(this, "Password should have minimum 6 characters", Toast.LENGTH_SHORT).show();

        }

        else if (username.isEmpty() || username.equals(null)) {
            alertusernamepopup();
//            Toast.makeText(this, "Enter username.", Toast.LENGTH_SHORT).show();

        }
        else if (!isPhotoValid) {

            alertimagepopup();
//            Toast.makeText(this, "Profile image should not be empty"
//                    , Toast.LENGTH_SHORT).show();

        }

        else {
//                Toast.makeText(this, "Enter email address.", Toast.LENGTH_SHORT).show();
//                valid = false;

            showProgressDialog();
            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupScreenActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.getIdToken(true)
                                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                if (task.isSuccessful()) {
                                                    final FirebaseUser user = mAuth.getCurrentUser();
                                                    Log.e("user", String.valueOf(user));
                                                    hideProgressDialog();
                                                    uploadImage(view);
                                                }
                                            }
                                        });


                            } else {

                                String str="Oops! This email id is already exists";

                                athenticaationpopup(str);
//                                Toast.makeText(getApplicationContext(), "Authentication Error:"+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                hideProgressDialog();
                            }
                        }

                    });

        }

    }




    public void uploadImage(final View view) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        if(contentURI != null)
        {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
            showProgressDialog();

            Uri file = Uri.fromFile(new File(selectedImagePath));
            StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());

            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = riversRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            hideProgressDialog();
//                            Toast.makeText(SignupScreenActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
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
                                    AddDatabase(user,view);
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
                    hideProgressDialog();
                    Toast.makeText(SignupScreenActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    postimageurl = "failed";
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
//                            progressDialog.setMessage("Uploading "+(int)progress+"%");
                            showProgressDialog();
                        }
                    });

        } else {
            return ;
        }
    }

    public void showProgressDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignupScreenActivity.this);
        //View view = getLayoutInflater().inflate(R.layout.progress);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void hideProgressDialog(){
        if(dialog!=null)
            dialog.dismiss();
    }

    private void AddDatabase(final FirebaseUser user, final View view){

//        final Map<String, Boolean> likeData1 = new HashMap<>();
//        likeData1.put("bookind_id", false);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Users users = new Users(mEtUsername.getText().toString(), mEtEmail.getText().toString(), postimageurl, mCarCategoryId[mCarouselCount]);
        showProgressDialog();

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                if (documentSnapshot.exists()){

                    hideProgressDialog();

                    Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();

                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, mEtEmail.getText().toString());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_USER_NAME, mEtUsername.getText().toString());

                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, postimageurl);
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());

                    PreferencesHelper.setPreference(getApplicationContext(),PreferencesHelper.PREFERENCE_PROFILE_CAR, String.valueOf(mCarouselCount));
                    PreferencesHelper.setPreferenceBoolean(getApplicationContext(),PreferencesHelper.PREFERENCE_ISLOGGEDIN,true);
                        Intent mainIntent = new Intent(SignupScreenActivity.this, HomeActivity.class);
                        startActivity(mainIntent);
                        finish();

                } else {

                    hideProgressDialog();

                    db.collection("users").document(user.getUid())
                            .set(users)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Error", "Error adding document", e);
                                    hideProgressDialog();
                                    Snackbar.make(view,"Login failed", Snackbar.LENGTH_SHORT).show();
                                    return;
                                }
                            });

                        Intent mainIntent = new Intent(SignupScreenActivity.this, HomeActivity.class);
                        startActivity(mainIntent);
                        finish();

                    PreferencesHelper.setPreferenceBoolean(getApplicationContext(), PreferencesHelper.PREFERENCE_ISLOGGEDIN,true);
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, mEtEmail.getText().toString());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_USER_NAME, mEtUsername.getText().toString());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, String.valueOf(postimageurl));
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());
                    PreferencesHelper.setPreference(getApplicationContext(),PreferencesHelper.PREFERENCE_PROFILE_CAR, String.valueOf(mCarouselCount));


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.w("Error", "Error adding document", e);
                Toast.makeText(getApplicationContext(),"Login failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEtEmail.getText().toString();
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && isPhotoValid) {

            valid = true;

        } else {


        }

        return valid;
    }

    private void alertpopup() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.empty_alrt, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = (TextView)deleteDialogView.findViewById(R.id.ok_txt);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
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


    private void checkFieldsForEmptyValues() {
        if ((TextUtils.isEmpty(mEtEmail.getText()))
                || (TextUtils.isEmpty(mEtUsername.getText())||(TextUtils.isEmpty(mEtPassword.getText())))){
            SignupBtn.setVisibility(View.VISIBLE);
        }
        else{
            SignupBtn.setVisibility(View.VISIBLE);
        }

    }
    private void alertEmailpopup() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.email_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = (TextView)deleteDialogView.findViewById(R.id.ok_txt);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
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

    private void alertPasswordpopup() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.password_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = (TextView)deleteDialogView.findViewById(R.id.ok_txt);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
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
    private void alertusernamepopup() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.username_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = (TextView)deleteDialogView.findViewById(R.id.ok_txt);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
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
    private void alertimagepopup() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.image_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = (TextView)deleteDialogView.findViewById(R.id.ok_txt);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignupScreenActivity.this,SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_righ);
        finish();

    }

    private void athenticaationpopup(String message) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.authentication_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView AthuntTxt=(TextView)deleteDialogView.findViewById(R.id.txt_authent);
        TextView ok = (TextView)deleteDialogView.findViewById(R.id.ok_txt);
        AthuntTxt.setText(message);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
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


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkFieldsForEmptyValues();
        }
    };


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    }

