package com.polsec.pyrky.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.adapter.NotificationAdapter;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class NotificationFragment extends Fragment {

    RecyclerView mRecyclerView;
    NotificationAdapter mRecyclerAdapter;

    String[] mMessage = {"Hey buddy, something happened to your car","Hey buddy, something happened to your car","Hey buddy, something happened to your car"};
    String[] mTime = {"2 minutes","5 days ago","2 months ago"};
    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_notification, null);
        mRecyclerView = view.findViewById(R.id.notification_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerAdapter = new NotificationAdapter(mMessage,mTime);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        return view;
    }
}