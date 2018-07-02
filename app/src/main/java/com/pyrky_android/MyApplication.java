package com.pyrky_android;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

/**
 * Created by thulirsoft on 7/2/18.
 */

public class MyApplication extends Application {
    Context context = this;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(context);
    }
}
