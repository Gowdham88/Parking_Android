package com.pyrky_android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
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
import com.pyrky_android.R;
import com.pyrky_android.pojo.Users;
import com.pyrky_android.adapter.CarouselAdapter;
import com.pyrky_android.preferences.PreferencesHelper;
import com.pyrky_android.utils.Utils;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "signup";
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

    int mIcons[] = {R.mipmap.ic_launcher,R.mipmap.ic_launcher_round,R.mipmap.ic_launcher,R.mipmap.ic_launcher_round, R.mipmap.ic_launcher};
    TextInputEditText mEmail,mPassword,mUserName;

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



        RelativeLayout parentLayout = findViewById(R.id.signup_parent_layout);
        TextView toSignIn = findViewById(R.id.already_have_account);
        Button signUp = findViewById(R.id.sign_up_button);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mUserName = findViewById(R.id.et_user_name);

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
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              signUp(mEmail.getText().toString().trim(),mPassword.getText().toString().trim(),mUserName.getText().toString().trim());
            }
        });
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

    protected void hideKeyboard(View view)
    {
        InputMethodManager inputMethodManager =(InputMethodManager )getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void signUp(String email, String password, String userName){
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

                                                    AddDatabase(user);
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

    private void AddDatabase(final FirebaseUser user){

        final Users users = new Users(mUserName.getText().toString().trim(),mEmail.getText().toString().trim(),"default", mCarCategoryId[mCarouselCount]);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//        hideProgressDialog();
        deleteField(db,user.getUid());
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){

                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getEmail());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, String.valueOf(user.getPhotoUrl()));
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());
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
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, String.valueOf(user.getPhotoUrl()));
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());
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
}
