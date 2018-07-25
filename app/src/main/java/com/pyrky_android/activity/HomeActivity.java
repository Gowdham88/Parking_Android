package com.pyrky_android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pyrky_android.MyApplication;
import com.pyrky_android.fragment.BookingsFragment;
import com.pyrky_android.fragment.HomeFragment;
import com.pyrky_android.fragment.NotificationFragment;
import com.pyrky_android.fragment.ProfileFragment;
import com.pyrky_android.R;
import com.pyrky_android.preferences.PreferencesHelper;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    BottomNavigationView.OnNavigationItemSelectedListener {
    Context context = this;
    @Inject
    Retrofit retrofit;
    Boolean isRunning = true;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ActionBar actionbar;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onStart() {
        super.onStart();
        ((MyApplication )getApplication()).getNetComponent().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();

        /*actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_ham_menu);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);*/
        actionbar.setTitle("Home");


        CoordinatorLayout coordinatorLayout = findViewById(R.id.home_coordinator);
        bottomNavigationView = findViewById(R.id.navigationView);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ham_menu);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.b_nav_home:
                        fragment = new HomeFragment();
                        actionbar.setTitle("Home");
                        break;

                    case R.id.b_nav_notification:
                        fragment = new NotificationFragment();
                        actionbar.setTitle("Notification");
                        break;

                    case R.id.b_nav_profile:
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_frame_layout, ProfileFragment.newInstance());
                        transaction.addToBackStack(null);
                        transaction.commit();


                        actionbar.setTitle("Profile");
                        break;
                }
                return loadFragment(fragment);
            }
        });
        loadFragment(new HomeFragment());
        actionbar.setTitle("Home");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(new HomeFragment());
            actionbar.setTitle("Home");
        } else if (id == R.id.nav_booking) {
            loadFragment(new BookingsFragment());
            actionbar.setTitle("Booking");
        } else if (id == R.id.nav_profile) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame_layout, ProfileFragment.newInstance());
            transaction.addToBackStack(null);
            transaction.commit();
            actionbar.setTitle("Profile");
        } else if (id == R.id.nav_logout) {
            PreferencesHelper.signOut(context);
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this,SignInActivity.class));
            HomeActivity.this.finish();
        }else if (id == R.id.profile_img){
            Toast.makeText(context, "Image selected", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.profile_name){
            Toast.makeText(context, "Image title", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }
}
