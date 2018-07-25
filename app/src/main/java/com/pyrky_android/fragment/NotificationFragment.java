package com.pyrky_android.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pyrky_android.R;
import com.pyrky_android.activity.NearestLocMapsActivity;
import com.pyrky_android.adapter.NotificationAdapter;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class NotificationFragment extends Fragment {

    RecyclerView mRecyclerView;
    NotificationAdapter mRecyclerAdapter;

    String[] mMessage = {"Hey buddy, something happened to your car","Hey buddy, something happened to your car","Hey buddy, something happened to your car"};
    String[] mTime = {"2 minutes","5 days ago","2 months ago"};

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

    public void showDialog(){
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());


        popDialog.setTitle("Do you want to request a camera view for three seconds");

        popDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().startActivity(new Intent(getActivity(), NearestLocMapsActivity.class));
            }
        });
        popDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        popDialog.create();
        popDialog.show();

    }
}
