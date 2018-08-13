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

        mWindow = getWindow();
        mWindow.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

//        setLightStatusBar(mWindow,SplashActivity.this);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                boolean isLoggedIn = PreferencesHelper.getPreferenceBoolean(SplashActivity.this, PreferencesHelper.PREFERENCE_ISLOGGEDIN);
                if (isLoggedIn) {
//                    boolean isDashboard = PreferencesHelper.getPreferenceBoolean(SplashActivity.this, PreferencesHelper.PREFERENCE_DASHBOARD);
//                    if(isDashboard){
                    Intent mainIntent = new Intent(SplashActivity.this,HomeActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);
                    SplashActivity.this.finish();

////                    }
////                    else{
////                        Intent inservice=new Intent(SplashActivity.this,ServiceProviderActivity.class);
////                        inservice.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////                        startActivity(inservice);
////                        finish();
////                    }
//
//
                } else {
                    Intent in = new Intent(SplashActivity.this, SignInActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    SplashActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

//    public static void setLightStatusBar(Window view, Activity activity){
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            int flags = view.getSystemUiVisibility();
//            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            view.setSystemUiVisibility(flags);
//            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//    }

}
