package com.pyrky_android.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pyrky_android.R;
import com.pyrky_android.adapter.BookingTabAdapter;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class BookingsFragment extends Fragment {

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
        BookingTabAdapter pagerAdapter = new BookingTabAdapter(getFragmentManager(),getActivity());
        viewPager.setAdapter(pagerAdapter);

        tabLayout = view.findViewById(R.id.simpleTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
