package com.pyrky_android.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.pyrky_android.R;
import com.pyrky_android.adapter.CarouselAdapter;
import com.pyrky_android.preferences.PreferencesHelper;

/**
 * Created by thulirsoft on 7/21/18.
 */

public class SettingsFragment extends Fragment{
    Toolbar toolbar;
    TextView toolbarText;
    EditText NameEdt;
    int mCarouselCount = 0;
    String mEmail,mName,mProfilepic;
    String[] mCarCategory = { "Compact", "Small", "Mid size", "Full", "Van/Pick-up" };
    String[] mCarCategoryId = { "1", "2", "3", "4", "5" };
    String[] mCarranze = { "3.5 - 4.5m", "2.5 - 3.5m", "4 -5m", "5 - 5.5m", "5.5 - 6.5m" };
    int mIcons[] = {R.drawable.compactcar_icon,R.drawable.smallcar_icon,R.drawable.midsizecar_icon,R.drawable.fullcar_icon, R.drawable.vanpickupcar_icon};

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mEmail = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_EMAIL);
        mName = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_USER_NAME);
        mProfilepic = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_PROFILE_PIC);

        toolbar = view.findViewById(R.id.toolbar);
        toolbarText.setText("Settings");
        NameEdt=view.findViewById(R.id.et_name);
        NameEdt.setText(mName);

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(1);

        final RecyclerView recyclerView = view.findViewById(R.id.carousel_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new CarouselAdapter(getActivity(), mIcons, mCarCategory,mCarranze));
        recyclerView.addOnScrollListener(new CenterScrollListener());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mCarouselCount = layoutManager.getCenterItemPosition();

                }
            });
        }
        return view;



    }
}
