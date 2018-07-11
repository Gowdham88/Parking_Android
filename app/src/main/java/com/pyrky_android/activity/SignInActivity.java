package com.pyrky_android.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.pyrky_android.Users;
import com.pyrky_android.preferences.PreferencesHelper;

public class SignInActivity extends AppCompatActivity {
    Context context = this;
    TextInputEditText mEmail,mPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_sign_in);
        TextView toSignUp = findViewById(R.id.dont_have_account);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        Button login = findViewById(R.id.sign_in_button);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(mEmail.getText().toString().trim(),mPassword.getText().toString().trim());
            }
        });

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();

                            AddDatabase(user);
                            Toast.makeText(context, "Successfully Signed in"+user.getEmail(), Toast.LENGTH_LONG).show();
                            PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getEmail());
//                            PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, String.valueOf(user.getPhotoUrl()));
                            PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());

                        }else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                    }
                });

    }

    private void AddDatabase(final FirebaseUser user){



        final Users users = new Users("Dinesh",mEmail.getText().toString().trim(),mPassword.getText().toString().trim(),"Small");
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//        hideProgressDialog();

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                if (documentSnapshot.exists()){

                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getEmail());
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, String.valueOf(user.getPhotoUrl()));
                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, user.getUid());

                    final Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SignInActivity.this.finish();
                    overridePendingTransition(0, 0);

                } else {
                    db.collection("users").document(user.getUid())
                            .set(users)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Error", "Error adding document", e);
//                                    hideProgressDialog();
                                    Toast.makeText(context,"Login failed",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            });

                    final Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SignInActivity.this.finish();
                    overridePendingTransition(0, 0);

                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getEmail());
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




}
