package com.pyrky_android.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.pyrky_android.R;
import com.pyrky_android.activity.HomeActivity;
import com.pyrky_android.adapter.CustomViewPagerAdapter;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class BookingsFragment extends Fragment implements  TabHost.OnTabChangeListener {

    public static BookingsFragment newInstance() {
        return new BookingsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings,container,false);


        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        viewPager = view.findViewById(R.id.viewpager_events);
        tabLayout = view.findViewById(R.id.simpleTabLayout);

        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        setupTabLayout();
        viewPager.setCurrentItem(0);
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new CurrentBookingsFragment(), "Current Bookings");
        adapter.addFrag(new HistoryFragment(), "History");
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {

        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Current Bookings");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("History");
        tabLayout.getTabAt(1).setCustomView(tabTwo);


    }

    @Override
    public void onTabChanged(String s) {

    }
}
