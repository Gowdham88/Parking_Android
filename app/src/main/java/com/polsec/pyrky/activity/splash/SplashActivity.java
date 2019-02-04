package com.polsec.pyrky.activity.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.activity.signin.SignInActivity;
import com.polsec.pyrky.preferences.PreferencesHelper;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private Window mWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(() -> {
            /* Create an Intent that will start the Menu-Activity. */
            boolean isLoggedIn = PreferencesHelper.getPreferenceBoolean(SplashActivity.this, PreferencesHelper.PREFERENCE_ISLOGGEDIN);
            if (isLoggedIn) {

                Intent mainIntent = new Intent(SplashActivity.this,HomeActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                SplashActivity.this.finish();

            } else {
                Intent in = new Intent(SplashActivity.this, SignInActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


}
