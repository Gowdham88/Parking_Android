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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gtomato.android.ui.transformer.LinearViewTransformer;
import com.gtomato.android.ui.widget.CarouselView;
import com.pyrky_android.R;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "signup";
    private FirebaseAuth mAuth;


    Context context = this;
    Spinner spinner;
    String[] Languages = { "Compact", "Small", "Mid size", "Full", "Van/Pick-up" };
    CarouselView carouselView;
    int icons[] = {R.mipmap.ic_launcher,R.mipmap.ic_launcher_round,R.mipmap.ic_launcher,R.mipmap.ic_launcher_round, R.mipmap.ic_launcher};
    TextInputEditText mEmail,mPassword,mUserName;
    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        Toast.makeText(context, currentUser.getDisplayName()+"already signed in", Toast.LENGTH_SHORT).show();

    }

   /* @Override
    protected void onResume() {
        super.onResume();
        mAuth.addIdTokenListener(new FirebaseAuth.IdTokenListener() {
            @Override
            public void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null)
                    updateToken(firebaseAuth.getCurrentUser());
            }
        });
    }
    private void updateToken(FirebaseUser user) {
        user.getIdToken(false)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Log.d("Token:", idToken);
//                            SyncStateContract.Constants.FIREBASE_TOKEN = idToken;
//                            PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_TOKEN, idToken);
                        }
                    }
                });
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        TextView toSignIn = findViewById(R.id.already_have_account);
        Button categoryBack = findViewById(R.id.type_back);
        Button categoryFront = findViewById(R.id.type_front);
        Button signUp = findViewById(R.id.sign_up_button);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mUserName = findViewById(R.id.et_user_name);
        carouselView = findViewById(R.id.carousel);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              login(mEmail.getText().toString().trim(),mPassword.getText().toString().trim());
            }
        });
//        Spinner spinner = findViewById(R.id.car_category);
//        spinner.setAdapter(new MySpinnerAdapter(SignUpActivity.this, R.layout.item_spinner,
//                Languages));
        carouselView.setTransformer(new LinearViewTransformer());
        carouselView.setAdapter(new MyDataAdapter(context,icons,Languages));

        categoryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                carouselView.smoothScrollToPosition(carouselView.getCurrentPosition()-1);
            }
        });

        categoryFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carouselView.smoothScrollToPosition(carouselView.getCurrentPosition()+1);
            }
        });
        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            }
        });

    }

    private void login(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(context, "Successfully Signed in"+user.getDisplayName(), Toast.LENGTH_LONG).show();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
