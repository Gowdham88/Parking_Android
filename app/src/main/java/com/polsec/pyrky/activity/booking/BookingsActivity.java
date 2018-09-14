package com.polsec.pyrky.activity.booking;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toolbar;

import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.adapter.CustomViewPagerAdapter;
import com.polsec.pyrky.fragment.CurrentBookingsFragment;
import com.polsec.pyrky.fragment.HistoryFragment;
import com.polsec.pyrky.utils.CustomViewPager;

public class BookingsActivity extends AppCompatActivity implements  TabHost.OnTabChangeListener,TabLayout.OnTabSelectedListener{
    CustomViewPager viewPager;
    TabLayout tabLayout;
Toolbar toolbar;
    ImageView mBackIcon;
    TextView  TitlaTxt;
    RelativeLayout BackImgRelay;
    TextView tabOne,tabTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bookings);

//        Toast.makeText(this, Constants.SEARCH_ARRAY.get(0), Toast.LENGTH_SHORT).show();

        RelativeLayout parentLayout = findViewById(R.id.parent_layout);
        mBackIcon =(ImageView)findViewById(R.id.back_icon);
        TitlaTxt=(TextView)findViewById(R.id.extra_title);
        TitlaTxt.setText("Bookings");



        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_righ);
                finish();
            }
        });
//        BookingsActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        viewPager =findViewById(R.id.viewpager_events);
        tabLayout =findViewById(R.id.simpleTabLayout);

        tabLayout.setFocusableInTouchMode(false); //Not Working
        tabLayout.setFocusable(false);
        viewPager.setSwipeable(false);
//        viewPager.setPagingEnabled(false);

//        BookingTabAdapter pagerAdapter = new BookingTabAdapter(getFragmentManager(),getActivity());
//        viewPager.setAdapter(pagerAdapter);
//        setupTabIcons();
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setFocusableInTouchMode(false); //Not Working
        tabLayout.setFocusable(false);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setOnTabSelectedListener(this);

        setupTabLayout();
        viewPager.setCurrentItem(0);
//        viewPager.endFakeDrag();
    }



//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                for (int i = 0; i < tabLayout.getTabCount(); i++) {
//                    if (i == position) {
//                        tabLayout.getTabAt(i).getCustomView().setBackgroundColor(Color.parseColor("#ffffff"));
//                    } else {
//                        tabLayout.getTabAt(i).getCustomView().setBackgroundColor(Color.parseColor("#fEAEAEA"));
//                    }
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//
//

    private void setupViewPager(ViewPager viewPager) {
        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CurrentBookingsFragment(), "Current Bookings");
        adapter.addFrag(new HistoryFragment(), "History");
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {
         tabOne = (TextView) LayoutInflater.from(BookingsActivity.this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Current Bookings");
        tabLayout.getTabAt(0).setCustomView(tabOne);

         tabTwo = (TextView) LayoutInflater.from(BookingsActivity.this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("History");
        tabLayout.getTabAt(1).setCustomView(tabTwo);


    }

    @Override
    public void onTabChanged(String s) {
//        viewPager.endFakeDrag();

    }

    /**
     * Called when a tab enters the selected state.
     *
     * @param tab The tab that was selected
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

        int selectedTabPosition = tab.getPosition();

//        if (selectedTabPosition == 0)
//        { // that means first tab
//            tabOne.setBackground(getResources().getDrawable(R.drawable.tabselectionleft));
//            tabTwo.setBackground(getResources().getDrawable(R.drawable.notabselectionleft));
//
//
//        } else if (selectedTabPosition == 1)
//        { // that means it's a last tab
//
//            tabOne.setBackground(getResources().getDrawable(R.drawable.notabselectionright));
//            tabTwo.setBackground(getResources().getDrawable(R.drawable.tabselectionright));
//
//
//        }
    }

    /**
     * Called when a tab exits the selected state.
     *
     * @param tab The tab that was unselected
     */
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    /**
     * Called when a tab that is already selected is chosen again by the user. Some applications
     * may use this action to return to the top level of a category.
     *
     * @param tab The tab that was reselected.
     */
    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

//    private void setupTabLayout() {
//
//                TextView customTab1 = (TextView) LayoutInflater.from(getActivity())
//                .inflate(R.layout.custom_tab, null);
//                TextView customTab2 = (TextView) LayoutInflater.from(getActivity())
//                .inflate(R.layout.custom_tab, null);
//                customTab1.setText("");
//                tabLayout.getTabAt(0).setCustomView(customTab1);
//                customTab2.setText("");
//                tabLayout.getTabAt(1).setCustomView(customTab2);
//                }


}

