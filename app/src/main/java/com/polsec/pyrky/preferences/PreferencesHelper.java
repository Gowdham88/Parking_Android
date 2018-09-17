package com.polsec.pyrky.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by thulirsoft on 7/3/18.
 */

public class PreferencesHelper {

    //Region Constants
    private static final String USER_PREFERENCES = "userPreferences";
    public static final String PREFERENCE_USER_NAME = USER_PREFERENCES + ".name";
    public static final String PREFERENCE_EMAIL = USER_PREFERENCES + ".email";
    public static final String PREFERENCE_ID = USER_PREFERENCES + ".id";
    public static final String PREFERENCE_PROFILE_PIC = ".profilePic";
    public static final String PREFERENCE_ISLOGGEDIN = ".isloggedin";
    public static final String PREFERENCE_PROFILE_CAR = ".profileCar";
    public static final String PREFERENCE_CAR_ICON = ".profileCar";
    public static final String PREFERENCE_CAR_SIZE = ".profileCar";
    public static final String PREFERENCE_CAR_DIMENSION = ".profileCar";
    public static final String PREF_FIREBASE_TOKEN = ".fbtoken";
    public static final String PREFERENCE_FIREBASE_UUID = ".fbuid";
    public static final String PREFERENCE_LOGGED_IN ="logged in";
    public static final String PREFERENCE_LOGGED_INPASS ="pass in";
    public static final String PREFERENCE_DOCMENTID ="document id";
    public static final String PREFERENCE_DESTLATITUDE ="destlat";
    public static final String PREFERENCE_DESTLONGITUDE ="destlong";
    public static final String PREFERENCE_DESTNAME ="destname";
    public static final String PREFERENCE_CARPRO ="carprot";
    public static final String PREFERENCE_PARKINGSTATUS ="parkingstatus";
    public static final String PREFERENCE_DOCUMENTID ="docid";
    public static final String PREFERENCE_DOCUMENTIDNEW ="docidnew";
    public static final String PREFERENCE_DOCUMENTIDNEWONE ="docidnewone";
    public static final String PREFERENCE_POSTTIME ="posttime";
    public static final String PREFERENCE_PARKING_TYPES = "parkingtype";
    public static final String PREFERENCE_CAR_CATEGORY = "carcategory";
    public static final String PREFERENCE_SECURITY_RATINGS = "securityratings";
//    public static final String PREFERENCE_PARKINGSTATUS ="parkingstatus";
    //End Region

    //Region Constructor
    public PreferencesHelper() {
        //no instance
    }
    //End Region


    public static void signOut(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(USER_PREFERENCES);
        editor.remove(PREFERENCE_USER_NAME);
        editor.remove(PREFERENCE_EMAIL);
        editor.remove(PREFERENCE_FIREBASE_UUID);
        editor.remove(PREFERENCE_PROFILE_PIC);
        editor.remove(PREF_FIREBASE_TOKEN);
        editor.remove(PREFERENCE_PROFILE_CAR);
        editor.remove(PREFERENCE_ISLOGGEDIN);
        editor.remove(PREFERENCE_LOGGED_INPASS);

        editor.apply();
    }

    public static void setPreference(Context context,String preference_name,String details){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(preference_name,details);
        editor.apply();
    }

    public static void setPreferenceBoolean(Context context, String preference_name, boolean details) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(preference_name, details);
        editor.apply();
    }

    public static String getPreference(Context context, String name) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(name, "");
    }

    public static boolean getPreferenceBoolean(Context context, String name) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(name, false);
    }

    //Region helper methods
    private static SharedPreferences.Editor getEditor(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
    }



    // endregion

}
