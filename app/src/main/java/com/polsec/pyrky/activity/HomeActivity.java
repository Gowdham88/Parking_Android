package com.polsec.pyrky.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.polsec.pyrky.activity.booking.BookingsActivity;
import com.polsec.pyrky.activity.signin.SignInActivity;
import com.polsec.pyrky.adapter.DrawerItemCustomAdapter;
import com.polsec.pyrky.fragment.HomeFragment;
import com.polsec.pyrky.fragment.NotificationFragment;
import com.polsec.pyrky.fragment.ProfileFragment;
import com.polsec.pyrky.R;
import com.polsec.pyrky.pojo.DataModel;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.CircleTransformation;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
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
    TextView textview;
    TextView toolbarText,Username;
    RelativeLayout.LayoutParams layoutparams;
    String UsrName;
    private FirebaseAuth mAuth;
    private int avatarSize;
    View view;


    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
//    @Override
//    protected void onStart() {
//        super.onStart();
//        ((MyApplication )getApplication()).getNetComponent().inject(this);
//    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        actionbar = getSupportActionBar();
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mAuth = FirebaseAuth.getInstance();
//        actionbar.setTitle("Home");

        toolbarText = findViewById(R.id.toolbar_text);
        setSupportActionBar(toolbar);
        view = (View)findViewById(R.id.myview);
        view.setVisibility(View.VISIBLE);
        UsrName=PreferencesHelper.getPreference(HomeActivity.this, PreferencesHelper.PREFERENCE_USER_NAME);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.lst_menu_items);
//
//        Username=findViewById(R.id.);
//        Username.setText(UsrName);


        LinearLayout coordinatorLayout = findViewById(R.id.home_coordinator);
        coordinatorLayout = findViewById(R.id.home_coordinator);
        bottomNavigationView = findViewById(R.id.navigationView);


//        toggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toolbar.inflateMenu(R.menu.activity_main_drawer);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
//        toggle.setDrawerIndicatorEnabled(false);

////        drawer.addDrawerListener(toggle);
//        toggle.syncState();

//        toggle.setToolbarNavigationClickListener(v -> {
////                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            if (drawer.isDrawerOpen(GravityCompat.START)) {
//                drawer.closeDrawer(GravityCompat.START);
//            } else {
//                drawer.openDrawer(GravityCompat.START);
//            }
//        });


        setupToolbar();

        DataModel[] drawerItem = new DataModel[4];

        drawerItem[0] = new DataModel(R.drawable.ic_tab_home, "Home");
        drawerItem[1] = new DataModel(R.drawable.ic_bookings_menu, "Bookings");
        drawerItem[2] = new DataModel(R.drawable.ic_tab_user, "Profile");
        drawerItem[3] = new DataModel(R.drawable.ic_logout, "Logout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(HomeActivity.this,R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        NavigationView navigationView = findViewById(R.id.navigation);
//        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = (TextView)findViewById(R.id.user_name);
        CircleImageView profileImage = (CircleImageView)findViewById(R.id.user_image);
        String profileImageUrl = PreferencesHelper.getPreference(HomeActivity.this,PreferencesHelper.PREFERENCE_PROFILE_PIC);
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            Picasso.with(HomeActivity.this)
                    .load(profileImageUrl)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(profileImage);
        }
        txtProfileName.setText(UsrName);

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment = null;
//
//                switch (item.getItemId()){
//                    case R.id.b_nav_home:
//                        fragment = new HomeFragment();
////                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                        toolbarText.setText("Home");
//                        break;
//
//                    case R.id.b_nav_notification:
//                        fragment = new NotificationFragment();
////                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                        toolbarText.setText("Notification");
//                        break;
//
//                    case R.id.b_nav_profile:
//                        fragment = new ProfileFragment();
////                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                        toolbarText.setText("Profile");
//                        break;
//                }
//                return loadFragment(fragment);
//            }
//        });
////        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//        loadFragment(new HomeFragment());
//        toolbarText.setText("Home");
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                loadFragment(new HomeFragment());
        toolbarText.setText("Home");
                break;
            case 1:
                Intent intent=new Intent(HomeActivity.this, BookingsActivity.class);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                startActivity(intent);
                break;
            case 2:
                loadFragment(new ProfileFragment());
                toolbarText.setText("Profile");
                break;

            case 3:
                Intent intent1 = new Intent(getApplicationContext(), SignInActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("EXIT", true);
//            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                startActivity(intent1);
                PreferencesHelper.signOut(HomeActivity.this);
                mAuth.signOut();
                HomeActivity.this.finish();
                break;

            default:
                break;
        }

//        loadFragment(new HomeFragment());
//        toolbarText.setText("Home");

//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment).commit();
//
//            mDrawerList.setItemChecked(position, true);
//            mDrawerList.setSelection(position);
//            setTitle(mNavigationDrawerItemTitles[position]);
//            mDrawerLayout.closeDrawer(mDrawerList);
//
//        } else {
//            Log.e("MainActivity", "Error in creating fragment");
//        }
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

   /* @Override
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
    }*/

//    @Override
//    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onPostCreate(savedInstanceState, persistentState);
//        toggle.syncState();
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.main_frame_layout, new HomeFragment());
            transaction.addToBackStack(null);
            transaction.commit();
//            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//            loadFragment(new ());
            toolbarText.setText("Home");
        } else if (id == R.id.nav_booking) {
//            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//            loadFragment(new BookingsFragment());
                Intent intent=new Intent(HomeActivity.this, BookingsActivity.class);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                startActivity(intent);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
//            transaction.replace(R.id.main_frame_layout, new BookingsFragment());
//            transaction.addToBackStack(null);
//            transaction.commit();
//            toolbarText.setText("Booking");
        } else if (id == R.id.nav_profile) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.main_frame_layout, new ProfileFragment());
            transaction.addToBackStack(null);
            transaction.commit();
//            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//            loadFragment(new ProfileFragment());
            toolbarText.setText("Profile");
        } else if (id == R.id.nav_logout) {


            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
//            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            startActivity(intent);
            PreferencesHelper.signOut(HomeActivity.this);
            mAuth.signOut();
            HomeActivity.this.finish();

        }else if (id == R.id.profile_img){
            Toast.makeText(context, "Image selected", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.profile_name){
            Toast.makeText(context, "Image title", Toast.LENGTH_SHORT).show();
        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        isRunning = false;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        isRunning = true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }


}
