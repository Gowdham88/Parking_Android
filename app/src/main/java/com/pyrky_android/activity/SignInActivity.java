package com.pyrky_android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pyrky_android.R;
import com.pyrky_android.pojo.Users;
import com.pyrky_android.preferences.PreferencesHelper;
import com.pyrky_android.utils.Utils;

public class SignInActivity extends AppCompatActivity {
    Context mContext = this;
    EditText mEmail,mPassword;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    RelativeLayout parentsigninlay;

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
        Button login = findViewById(R.id.sign_in_button);
        parentsigninlay=(RelativeLayout)findViewById(R.id.signin_layout);
        parentsigninlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(SignInActivity.this);
            }
        });

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(v);
                }
            }
        });

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(v);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()){
                    login(mEmail.getText().toString().trim(),mPassword.getText().toString().trim());
                }

            }
        });

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignupScreenActivity.class));
                SignInActivity.this.finish();
            }
        });
    }

    private void login(String email, String password) {
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();

                            AddDatabase(user);
                            Toast.makeText(mContext, "Successfully Signed in"+user.getEmail(), Toast.LENGTH_LONG).show();
                            hideProgressDialog();
                            PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getEmail());
                            PreferencesHelper.setPreferenceBoolean(getApplicationContext(),PreferencesHelper.PREFERENCE_ISLOGGEDIN,true);
//                            PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, String.valueOf(user.getPhotoUrl()));

                        }else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
//                            updateUI(null);
                        }

                    }
                });

    }

    private void AddDatabase(final FirebaseUser user){

        final Users users = new Users("Dinesh",mEmail.getText().toString().trim(),mPassword.getText().toString().trim(),"Small");
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

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

                    final Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SignInActivity.this.finish();
                    hideProgressDialog();
                    overridePendingTransition(0, 0);

                } else {
                    db.collection("users").document(user.getUid())
                            .set(users)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    hideProgressDialog();
                                    Log.w("Error", "Error adding document", e);
//                                    hideProgressDialog();
                                    Toast.makeText(mContext,"Login failed",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            });
                    PreferencesHelper.setPreferenceBoolean(getApplicationContext(),PreferencesHelper.PREFERENCE_ISLOGGEDIN,true);
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getEmail());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, String.valueOf(user.getPhotoUrl()));
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());


                    final Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SignInActivity.this.finish();
                    hideProgressDialog();
                    overridePendingTransition(0, 0);


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Log.w("Error", "Error adding document", e);
                Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
            }
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
        String password = mPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

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
            }
            else if (TextUtils.isEmpty(password) || password.length()<4) {
                Toast.makeText(this, "Enter valid password.", Toast.LENGTH_SHORT).show();
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
