package com.pyrky_android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pyrky_android.fragment.CurrentBookingsFragment;
import com.pyrky_android.fragment.HistoryFragment;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class BookingTabAdapter extends FragmentStatePagerAdapter {

    String tabTitles[] = new String[]{"Current Bookings", "History"};
    Context context;
    FragmentManager fragmentManager;
    int mNumofTab;

    public BookingTabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CurrentBookingsFragment tab1 = new CurrentBookingsFragment();
                return tab1;
            case 1:
                HistoryFragment tab2 = new HistoryFragment();
                return tab2;
            default:

                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

//    public View getTabView(int position) {
//        View tab = LayoutInflater.from(context).inflate(R.layout.fragment_upcoming_events, null);
//        TextView tv = ( TextView ) tab.findViewById(R.id.event_title);
//        tv.setText(tabTitles[position]);
//        return tab;
//    }
}