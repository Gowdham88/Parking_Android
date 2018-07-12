package com.pyrky_android.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pyrky_android.R;
import com.pyrky_android.adapter.CurrentBookingRecyclerAdapter;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class CurrentBookingsFragment extends Fragment {
    Context context;
    String mPlace[] = {"Rio de Janeiro","Brasília","Salvador","Fortaleza","Belo Horizonte"};
    String mTimeDate[] = {"05 Jan, 12.30am","15 Feb, 03.10pm","25 Mar, 05.50pm","17 Jun, 10.30am","01 Jan, 12.00am"};
    RecyclerView mRecyclerView;
    View view;
    public CurrentBookingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_booking, null);

        mRecyclerView = view.findViewById(R.id.current_booking_recycler);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CurrentBookingRecyclerAdapter recyclerAdapter = new CurrentBookingRecyclerAdapter(getActivity(),mPlace,mTimeDate);
        mRecyclerView.setAdapter(recyclerAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView = view.findViewById(R.id.current_booking_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CurrentBookingRecyclerAdapter recyclerAdapter = new CurrentBookingRecyclerAdapter(getActivity(),mPlace,mTimeDate);
        mRecyclerView.setAdapter(recyclerAdapter);

    }
}
