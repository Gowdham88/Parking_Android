package com.polsec.pyrky.activity.signin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.activity.signup.SignupScreenActivity;
import com.polsec.pyrky.activity.forgotpassword.ForgotpasswordActivity;
import com.polsec.pyrky.pojo.Users;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.Utils;

public class SignInActivity extends AppCompatActivity {
    private Context mContext = this;
    private EditText mEmail, mPassword;
    private FirebaseAuth mAuth;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_in);
        TextView toSignUp = findViewById(R.id.dont_have_account);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        TextView forgotPassTxt = findViewById(R.id.tx_forget_password);
        TextView login = findViewById(R.id.sign_in_button);
        //
        LinearLayout parentLinLay = findViewById(R.id.linearLayout);
        // parentLinLay.setOnClickListener(view -> hideSoftKeyboard(SignInActivity.this));
        checkAlreadyLogin();
        login.setOnClickListener(v -> {
            mAuth = FirebaseAuth.getInstance();
            signIn(mEmail.getText().toString().trim(), mPassword.getText().toString().trim());
            PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_LOGGED_INPASS, mPassword.getText().toString().trim());

        });
        forgotPassTxt.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this, ForgotpasswordActivity.class));
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            SignInActivity.this.finish();
        });

        toSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignupScreenActivity.class));
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            SignInActivity.this.finish();
        });
    }

    private void checkAlreadyLogin() {

        if ((PreferencesHelper.getPreferenceBoolean(mContext, PreferencesHelper.PREFERENCE_ISLOGGEDIN))) {
            Log.d("PreferencesHelper---", "fdjkfjfh");
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
            SignInActivity.this.finish();
        }

    }

    private void signIn(final String email, final String password) {

        String email1 = mEmail.getText().toString().trim();
        String password1 = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email1) && TextUtils.isEmpty(password1)) {
            alertpopup();
        } else if ((email1.isEmpty())) {

            alertEmailpopup();

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {

            alertEmailmatchpopup();

        } else if (TextUtils.isEmpty(password1)) {

            alertPasswordpopup();

        } else if ((password1.length() < 6)) {

            alertPasswordcharpopup();

        } else {

            showProgressDialog();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignInActivity.this, task -> {
                        if (task.isSuccessful()) {

                            final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
                            docRef.get().addOnSuccessListener(documentSnapshot -> {

                                if (documentSnapshot.exists()) {

                                    Users user = documentSnapshot.toObject(Users.class);

                                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_EMAIL, user.getEmail());
                                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_USER_NAME, user.getUsername());
                                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_PIC, user.getProfileImageURL());
                                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_PROFILE_CAR, user.getCarCategory());
                                    PreferencesHelper.setPreference(getApplicationContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID, firebaseUser.getUid());
                                    PreferencesHelper.setPreferenceBoolean(getApplicationContext(), PreferencesHelper.PREFERENCE_ISLOGGEDIN, true);

                                    Intent in = new Intent(SignInActivity.this, HomeActivity.class);
                                    startActivity(in);
                                    finish();
                                    hideProgressDialog();

                                } else {

                                    String userstr = "User doesn't exits";
                                    nouser(userstr);
                                    hideProgressDialog();
                                }

                            }).addOnFailureListener(e -> {

                                Log.w("Error", "Error adding document", e);
                                String str = "Login failed";
                                failathenticaationpopup(str);
                                hideProgressDialog();
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            String str1 = "Oops! Enter valid Password";
                            athenticaationpopup(str1);
                            hideProgressDialog();
                        }

                    });
        }

    }

    public void showProgressDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignInActivity.this);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void hideProgressDialog() {
        if (dialog != null)
            dialog.dismiss();
    }


    private void alertpopup() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.signinempty_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = (TextView) deleteDialogView.findViewById(R.id.ok_txt);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(view -> alertDialog1.dismiss());


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


    private void alertEmailpopup() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.signinempty_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = (TextView) deleteDialogView.findViewById(R.id.ok_txt);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(view -> alertDialog1.dismiss());


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

    private void alertEmailmatchpopup() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.emailmatch, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = (TextView) deleteDialogView.findViewById(R.id.ok_txt);

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
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        alertDialog1.getWindow().setAttributes(lp);
    }


    private void alertPasswordpopup() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.password_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = (TextView) deleteDialogView.findViewById(R.id.ok_txt);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(view -> alertDialog1.dismiss());


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

    private void alertPasswordcharpopup() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.pass_charecter_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView ok = (TextView) deleteDialogView.findViewById(R.id.ok_txt);

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
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        alertDialog1.getWindow().setAttributes(lp);
    }

    private void athenticaationpopup(String message) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.authentication_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView AthuntTxt = (TextView) deleteDialogView.findViewById(R.id.txt_authent);
        TextView ok = (TextView) deleteDialogView.findViewById(R.id.ok_txt);
        AthuntTxt.setText(message);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(view -> alertDialog1.dismiss());


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

    private void failathenticaationpopup(String str) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.authentication_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView AthuntTxt = (TextView) deleteDialogView.findViewById(R.id.txt_authent);
        TextView ok = (TextView) deleteDialogView.findViewById(R.id.ok_txt);
        AthuntTxt.setText(str);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(view -> alertDialog1.dismiss());


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

    private void nouser(String str) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.authentication_alert, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        TextView AthuntTxt = (TextView) deleteDialogView.findViewById(R.id.txt_authent);
        TextView ok = (TextView) deleteDialogView.findViewById(R.id.ok_txt);
        AthuntTxt.setText(str);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(view -> alertDialog1.dismiss());


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

}
