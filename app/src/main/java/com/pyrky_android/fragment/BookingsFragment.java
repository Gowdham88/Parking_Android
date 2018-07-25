package com.pyrky_android.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.pyrky_android.R;
import com.pyrky_android.adapter.CustomViewPagerAdapter;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class BookingsFragment extends Fragment implements  TabHost.OnTabChangeListener {

    public static BookingsFragment newInstance() {
        return new BookingsFragment();
    }

    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings,container,false);

        viewPager = view.findViewById(R.id.viewpager_events);
        tabLayout = view.findViewById(R.id.simpleTabLayout);
//        BookingTabAdapter pagerAdapter = new BookingTabAdapter(getFragmentManager(),getActivity());
//        viewPager.setAdapter(pagerAdapter);
//        setupTabIcons();

        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setupTabLayout();
        viewPager.setCurrentItem(0);
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



        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new CurrentBookingsFragment(), "Current Bookings");
//        adapter.addFrag(new LikeDataFragment(), "");
        adapter.addFrag(new HistoryFragment(), "History");
        viewPager.setAdapter(adapter);
    }
    private void setupTabLayout() {

        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Current Bookings");
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.barcode_img, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("History");
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.review, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);


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

    @Override
    public void onTabChanged(String s) {

    }
}
