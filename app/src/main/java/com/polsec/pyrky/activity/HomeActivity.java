package com.polsec.pyrky.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.ar.core.ArCoreApk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.polsec.pyrky.activity.booking.BookingsActivity;
import com.polsec.pyrky.activity.signin.SignInActivity;
import com.polsec.pyrky.fragment.HomeFragment;
import com.polsec.pyrky.fragment.NotificationFragment;
import com.polsec.pyrky.fragment.ProfileFragment;
import com.polsec.pyrky.R;
import com.polsec.pyrky.fragment.SettingsFragment;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.CircleTransformation;
import com.polsec.pyrky.utils.Constants;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {
    private Context context = this;
    private BottomNavigationView bottomNavigationView;
    private ActionBarDrawerToggle toggle;
    private TextView toolbarText;
    private int avatarSize;
    private View holderView;
    private String profileImageUrl;
    private CircleImageView profileImage;
    private NavigationView navigationView;
    public static boolean IS_LOCATION_LOADING_REQUIRED = false;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checkWhetherArEnabled();
        IS_LOCATION_LOADING_REQUIRED = true;
        Log.d("parkType---", "fdjkfjfh");
        String parkType = PreferencesHelper.getPreference(HomeActivity.this, PreferencesHelper.PREFERENCE_PARKING_TYPES);
        String secRatings = PreferencesHelper.getPreference(HomeActivity.this, PreferencesHelper.PREFERENCE_SECURITY_RATINGS);
        String carCategory = PreferencesHelper.getPreference(HomeActivity.this, PreferencesHelper.PREFERENCE_CAR_CATEGORY);

        if (!parkType.equals("") && !secRatings.equals("") && !carCategory.equals("")) {

            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> restoreData1 = new Gson().fromJson(parkType, type);
            List<String> restoreData2 = new Gson().fromJson(secRatings, type);
            List<String> restoreData3 = new Gson().fromJson(carCategory, type);

            Constants.PARKING_TYPES = restoreData1;
            Constants.SECURITY_RATINGS = restoreData2;
            Constants.CAR_CATEGORY = restoreData3;
        }
        Log.d("toolbar---", "fdjkfjfh");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarText = findViewById(R.id.toolbar_text);
        View view = (View) findViewById(R.id.myview);
        view.setVisibility(View.VISIBLE);
        String usrName = PreferencesHelper.getPreference(HomeActivity.this, PreferencesHelper.PREFERENCE_USER_NAME);

        // checkWhetherArEnabled();
        holderView = findViewById(R.id.holder);

        View contentView = findViewById(R.id.home_coordinator);
        bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.offsetTopAndBottom(0);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toolbar.inflateMenu(R.menu.activity_main_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ham_menu);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        profileImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        profileImageUrl = PreferencesHelper.getPreference(HomeActivity.this, PreferencesHelper.PREFERENCE_PROFILE_PIC);
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            Picasso.with(HomeActivity.this)
                    .load(profileImageUrl)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(profileImage);
        }
        txtProfileName.setText(usrName);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                holderView.setTranslationX(slideX);
            }
        };

        drawer.addDrawerListener(actionBarDrawerToggle);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.b_nav_home:
                    fragment = new HomeFragment();
                    navigationView.setCheckedItem(R.id.b_nav_home);
                    toolbarText.setText("Home");
                    break;

                case R.id.b_nav_notification:
                    fragment = new NotificationFragment();
                    toolbarText.setText("Notification");

                    break;

                case R.id.b_nav_profile:
                    fragment = new ProfileFragment();
                    navigationView.setCheckedItem(R.id.b_nav_profile);
                    toolbarText.setText("Profile");
                    break;
            }
            return loadFragment(fragment);
        });
        loadFragment(new HomeFragment());
        toolbarText.setText("Home");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            bottomNavigationView.setSelectedItemId(R.id.b_nav_home);
            navigationView.setCheckedItem(R.id.nav_home);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
            transaction.replace(R.id.main_frame_layout, new HomeFragment()).commit();
            toolbarText.setText("Home");
        } else if (id == R.id.nav_booking) {
            navigationView.setCheckedItem(R.id.nav_booking);
            Intent intent = new Intent(HomeActivity.this, BookingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);


        } else if (id == R.id.nav_profile) {

            bottomNavigationView.setSelectedItemId(R.id.b_nav_profile);
            navigationView.setCheckedItem(R.id.nav_profile);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.main_frame_layout, new ProfileFragment()).commit();
            toolbarText.setText("Profile");

//
        } else if (id == R.id.nav_logout) {


            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            PreferencesHelper.signOut(HomeActivity.this);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            HomeActivity.this.finish();

        } else if (id == R.id.profile_img) {
            Toast.makeText(context, "Image selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.profile_name) {
            Toast.makeText(context, "Image title", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            Picasso.with(HomeActivity.this)
                    .load(profileImageUrl)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(profileImage);
        }
    }

    void checkWhetherArEnabled() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkWhetherArEnabled();
                }
            }, 300);
        }
        if (availability.isSupported()) {
            Constants.IS_AR_ENABLED = true;
            // indicator on the button.
        } else { // Unsupported or unknown.
            Constants.IS_AR_ENABLED = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame_layout);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        IS_LOCATION_LOADING_REQUIRED = false;
        super.onDestroy();
    }
}
