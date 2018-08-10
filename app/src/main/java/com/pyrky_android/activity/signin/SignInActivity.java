package com.pyrky_android.activity.signin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pyrky_android.R;
import com.pyrky_android.activity.HomeActivity;
import com.pyrky_android.activity.signup.SignupScreenActivity;
import com.pyrky_android.activity.forgotpassword.ForgotpasswordActivity;
import com.pyrky_android.pojo.Users;
import com.pyrky_android.preferences.PreferencesHelper;
import com.pyrky_android.utils.Utils;

public class SignInActivity extends AppCompatActivity {
    Context mContext = this;
    EditText mEmail,mPassword;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    RelativeLayout parentsigninlay;
    private DatabaseReference mDatabase;
    private AlertDialog dialog;
    TextView ForgotPassTxt;
    @Override
    protected void onStart() {
        super.onStart();
        //Check whether the user already loggedIn
        if ((PreferencesHelper.getPreferenceBoolean(mContext,PreferencesHelper.PREFERENCE_ISLOGGEDIN))){
            final Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            SignInActivity.this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_sign_in);
        TextView toSignUp = findViewById(R.id.dont_have_account);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        ForgotPassTxt = findViewById(R.id.tx_forget_password);
        TextView login = findViewById(R.id.sign_in_button);
        parentsigninlay =findViewById(R.id.signin_layout);

        parentsigninlay.setOnClickListener(view -> Utils.hideKeyboard(SignInActivity.this));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        login.setOnClickListener(v -> {
            if (validateForm()){
                signIn(mEmail.getText().toString().trim(),mPassword.getText().toString().trim());
                PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_LOGGED_INPASS, mPassword.getText().toString().trim());

//                    SigninViewModel model = ViewModelProviders.of(SignInActivity.this).get(SigninViewModel.class);
            }

        });
        ForgotPassTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,ForgotpasswordActivity.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                SignInActivity.this.finish();
            }
        });

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignupScreenActivity.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                SignInActivity.this.finish();
            }
        });
    }

    private void signIn(final String email, final String password) {
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if (documentSnapshot.exists()){

                                        Users user = documentSnapshot.toObject(Users.class);

                                        PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL,user.getEmail());
                                        PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_USER_NAME,user.getUsername());
                                        PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, user.getProfileImageURL());
                                        PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_CAR, user.getCarCategory());
                                        PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, firebaseUser.getUid());
                                        PreferencesHelper.setPreferenceBoolean(getApplicationContext(), PreferencesHelper.PREFERENCE_ISLOGGEDIN,true);

                                        Intent in=new Intent(SignInActivity.this,HomeActivity.class);
                                        startActivity(in);
                                        finish();
                                        hideProgressDialog();

                                    } else {
                                        hideProgressDialog();
                                        Toast.makeText(SignInActivity.this, "No user exits", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Log.w("Error", "Error adding document", e);
                                    Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Registration failed! " + "\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            hideProgressDialog();
                        }

                    }
                });
    }

    public void showProgressDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignInActivity.this);
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

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && password.length()>=6) {

            valid = true;

        } else {
            if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter e-mail address and password", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if (email.isEmpty()){
                Toast.makeText(getApplicationContext(), "Enter e-mail address", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if((!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                Toast.makeText(getApplicationContext(), "Enter valid e-mail address", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter valid password", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if (password.length()<6){
                Toast.makeText(this, "Enter valid password", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else {
                Toast.makeText(this, "Something wrong with the credentials", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        return valid;
    }

    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager =(InputMethodManager )getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
