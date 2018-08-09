package com.pyrky_android.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.pyrky_android.R;
import com.pyrky_android.activity.HomeActivity;
import com.pyrky_android.adapter.HistoryRecyclerAdapter;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class HistoryFragment extends Fragment {
    Context context;
    RecyclerView mRecyclerView;
    String mPlace[] = {"Rio de Janeiro","Brasília","Salvador","Fortaleza","Belo Horizonte"};
    String mTimeDate[] = {"05 Jan, 12.30am","15 Feb, 03.10pm","25 Mar, 05.50pm","17 Jun, 10.30am","01 Jan, 12.00am"};
    int[] mCurrentRating = {5,5,5,5,5};
    HistoryRecyclerAdapter recyclerAdapter;
    public HistoryFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
//        ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null);

        mRecyclerView = view.findViewById(R.id.history_recycler);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new HistoryRecyclerAdapter(getActivity(),mPlace,mTimeDate,mCurrentRating);
        mRecyclerView.setAdapter(recyclerAdapter);

        return view;
    }


}
