package com.polsec.pyrky.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.polsec.pyrky.ExpandableListData;
import com.polsec.pyrky.R;
import com.polsec.pyrky.adapter.ExpandableListAdapter;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by thulirsoft on 7/19/18.
 */

public class FiltersFragment extends Fragment {

    //Expandable List View
    ExpandableListView mExpandableListView;
    ExpandableListAdapter mExpandableListAdapter;
    Boolean isExpandableListEnabled = false;
    RelativeLayout mHomeRelaLay;
    Button mEnableButton;
    String[] parentHeaders;
    String[] parent1;
    ArrayList<String> headersList;
    List<String> list;

    public FiltersFragment() {

    }
    public static FiltersFragment newInstance() {
        return new FiltersFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, null);
        mEnableButton = view.findViewById(R.id.enable_button);
        mEnableButton.setVisibility(View.GONE);
        mExpandableListView = view.findViewById(R.id.expandableListView);

        mHomeRelaLay=view.findViewById(R.id.parent_filter);
        mHomeRelaLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeRelaLay.setVisibility(View.GONE);
                getFragmentManager().popBackStack();
            }
        });

        parentHeaders = getResources().getStringArray(R.array.filtertypes);
//       mExpandableListDetail = ExpandableListData.getData();
       parent1 = getResources().getStringArray(R.array.filtertypes);

        list = new ArrayList<String>(Arrays.asList(parent1));

        headersList = new ArrayList<>();
        String[] parentHeaders = getResources().getStringArray(R.array.filtertypes);
        List<String> parentHeaderss = new ArrayList<String>(Arrays.asList(parentHeaders));
        headersList.addAll(parentHeaderss);

        HashMap<String, List<String>> allChildItems = returnGroupedChildItems();
        Log.e("mExpandableListTitle", String.valueOf(allChildItems));
        mExpandableListAdapter = new ExpandableListAdapter(getActivity(), headersList, allChildItems);
        mExpandableListView.setAdapter(mExpandableListAdapter);


        mEnableButton.setOnClickListener(v -> {
            if (!isExpandableListEnabled) {
                isExpandableListEnabled = true;
                mExpandableListView.setVisibility(View.VISIBLE);
                mExpandableListAdapter = new ExpandableListAdapter(getActivity(), headersList, allChildItems);

                mExpandableListView.setAdapter(mExpandableListAdapter);
            } else {
                mExpandableListView.setVisibility(View.GONE);
                isExpandableListEnabled = false;
            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {

                if(groupPosition != previousGroup){
                    mExpandableListView.collapseGroup(previousGroup);

                previousGroup = groupPosition;
            }
            }

        });

        mExpandableListView.setOnGroupCollapseListener(groupPosition -> {

        });

        mExpandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
//
            return false;
        });

        return view;
    }

    private HashMap<String,List<String>> returnGroupedChildItems() {

        {
            HashMap<String, List<String>> childList = new HashMap<String, List<String>>();

            List<String> parent1 = new ArrayList<String>();
            String[] child1 = getResources().getStringArray(R.array.parent1);
            List<String> childHeaders1 = new ArrayList<String>(Arrays.asList(child1));
            parent1.addAll(childHeaders1);

            List<String> parent2 = new ArrayList<String>();
            String[] child2 = getResources().getStringArray(R.array.parent2);
            List<String> childHeaders2 = new ArrayList<String>(Arrays.asList(child2));
            parent2.addAll(childHeaders2);

            List<String> parent3 = new ArrayList<String>();
            String[] child3 = getResources().getStringArray(R.array.parent3);
            List<String> childHeaders3 = new ArrayList<String>(Arrays.asList(child3));
            parent3.addAll(childHeaders3);
//
            childList.put(headersList.get(0), parent1);
            childList.put(headersList.get(1), parent2);
            childList.put(headersList.get(2), parent3);

            return childList;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Constants.PARKING_TYPES.size()>0){
            String parkingTypeData = new Gson().toJson(Constants.PARKING_TYPES);
            PreferencesHelper.setPreference(getActivity(),PreferencesHelper.PREFERENCE_PARKING_TYPES,parkingTypeData);

        }
        if (Constants.CAR_CATEGORY.size()>0){
            String carCategoryData = new Gson().toJson(Constants.CAR_CATEGORY);
            PreferencesHelper.setPreference(getActivity(),PreferencesHelper.PREFERENCE_CAR_CATEGORY,carCategoryData);
        }

        if (Constants.SECURITY_RATINGS.size()>0){
            String securityRatingsData = new Gson().toJson(Constants.SECURITY_RATINGS);
            PreferencesHelper.setPreference(getActivity(),PreferencesHelper.PREFERENCE_SECURITY_RATINGS,securityRatingsData);
        }


    }
}
