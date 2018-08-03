package com.pyrky_android.activity.booking;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.pyrky_android.R;
import com.pyrky_android.adapter.CustomViewPagerAdapter;
import com.pyrky_android.fragment.CurrentBookingsFragment;
import com.pyrky_android.fragment.HistoryFragment;

public class BookingsActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
Toolbar toolbar;
    ImageView BackImg;
    TextView  TitlaTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        BackImg=(ImageView)findViewById(R.id.back_image);
        TitlaTxt=(TextView)findViewById(R.id.extra_title);
        TitlaTxt.setText("Bookings");
        BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
//                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_righ);
//                startActivity(intent);
//                finish();
            }
        });
//        BookingsActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        viewPager =findViewById(R.id.viewpager_events);
        tabLayout =findViewById(R.id.simpleTabLayout);
//        BookingTabAdapter pagerAdapter = new BookingTabAdapter(getFragmentManager(),getActivity());
//        viewPager.setAdapter(pagerAdapter);
//        setupTabIcons();

        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setupTabLayout();
        viewPager.setCurrentItem(0);
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








    private void setupTabLayout() {

        TextView tabOne = (TextView) LayoutInflater.from(BookingsActivity.this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Current Bookings");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.default_tab, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(BookingsActivity.this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("History");
         tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.default_tab, 0, 0);
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_text_selector, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }
    private void setupViewPager(ViewPager viewPager) {
        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CurrentBookingsFragment(), "Current Bookings");
//        adapter.addFrag(new LikeDataFragment(), "");
        adapter.addFrag(new HistoryFragment(), "History");
        viewPager.setAdapter(adapter);
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

