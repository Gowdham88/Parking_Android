package com.pyrky_android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.pyrky_android.R;
import com.pyrky_android.Users;
import com.pyrky_android.adapter.MyDataAdapter;
import com.pyrky_android.preferences.PreferencesHelper;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "signup";
    private FirebaseAuth mAuth;
    private AlertDialog dialog;

    Context mContext = this;
    Spinner mSpinner;
    int mCarouselCount = 0;
    String[] mLanguages = { "Compact[3 - 4.5m]", "Small", "Mid size", "Full", "Van/Pick-up" };
    int mIcons[] = {R.mipmap.ic_launcher,R.mipmap.ic_launcher_round,R.mipmap.ic_launcher,R.mipmap.ic_launcher_round, R.mipmap.ic_launcher};
    TextInputEditText mEmail,mPassword,mUserName;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        TextView toSignIn = findViewById(R.id.already_have_account);
        Button signUp = findViewById(R.id.sign_up_button);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mUserName = findViewById(R.id.et_user_name);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              login(mEmail.getText().toString().trim(),mPassword.getText().toString().trim(),mUserName.getText().toString().trim());
            }
        });
//        Spinner mSpinner = findViewById(R.id.car_category);
//        mSpinner.setAdapter(new MySpinnerAdapter(SignUpActivity.this, R.layout.item_spinner,
//                mLanguages));

        //Carousel
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(1);


        final RecyclerView recyclerView = findViewById(R.id.carousel_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MyDataAdapter(this, mIcons, mLanguages));
        recyclerView.addOnScrollListener(new CenterScrollListener());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mCarouselCount = layoutManager.getCenterItemPosition();
                    String s = String.valueOf(layoutManager.getCenterItemPosition());
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                }
            });
        }

        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
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

    private void login(String email, String password, String userName){
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
//                                                Intent in=new Intent(SignupActivity.this,LoginScreen.class);
//                                                startActivity(in);
                                                final FirebaseUser user = mAuth.getCurrentUser();

                                                AddDatabase(user);
                                                Log.e("user", String.valueOf(user));
                                                // Sign in success, update UI with the signed-in user's information
                                            }
                                        }
                                    });

                            Toast.makeText(mContext, "Successfully Signed in "+user.getEmail(), Toast.LENGTH_LONG).show();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void AddDatabase(final FirebaseUser user){

        final Users users = new Users(mUserName.getText().toString().trim(),mEmail.getText().toString().trim(),mPassword.getText().toString().trim(),mLanguages[mCarouselCount]);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//        hideProgressDialog();

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){

                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getDisplayName());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, String.valueOf(user.getPhotoUrl()));
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());

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

                    final Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SignUpActivity.this.finish();
                    overridePendingTransition(0, 0);

                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getDisplayName());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, String.valueOf(user.getPhotoUrl()));
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());

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

    public void showProgressDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);
        //View view = getLayoutInflater().inflate(R.layout.progress);
//        alertDialog.setView(R.layout.progress);
        alertDialog.setTitle("In Progress");
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void hideProgressDialog(){
        if(dialog!=null)
            dialog.dismiss();
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString();
        String username = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
//                && isPhotoValid) {

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
                Toast.makeText(this, "Enter password.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
     /*       else if (!isPhotoValid) {
                Toast.makeText(this, "" +
                        "please fill the image", Toast.LENGTH_SHORT).show();
                valid = false;
            }
*/
            else {
                Toast.makeText(this, "Enter email address.", Toast.LENGTH_SHORT).show();
                valid = false;
            }


        }

        return valid;
    }
}
