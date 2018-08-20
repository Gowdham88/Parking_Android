package com.polsec.pyrky.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.polsec.pyrky.R;
import com.polsec.pyrky.adapter.CurrentBookingRecyclerAdapter;
import com.polsec.pyrky.adapter.HistoryRecyclerAdapter;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.preferences.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

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
    SwipeRefreshLayout swipeRefreshLayout;
    String uid;
    List<Booking> BookingList = new ArrayList<Booking>();
    List<String> BookingListId = new ArrayList<String>();
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";
    public static final String ACTION_SHOW_DEFAULT_ITEM = "action_show_default_item";
    public HistoryFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//        ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);
        loadPost(ACTION_SHOW_LOADING_ITEM);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null);

        mRecyclerView = view.findViewById(R.id.history_recycler);


        uid = PreferencesHelper.getPreference(getContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);
//        loadPost(ACTION_SHOW_LOADING_ITEM);



        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
        recyclerAdapter= new HistoryRecyclerAdapter(getActivity(),BookingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);
        mRecyclerView.setHasFixedSize(true);
        recyclerAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
//                        feedAdapter.notifyDataSetChanged();

                        doYourUpdate();
                    }
                }
        );

        return view;
    }

    private void doYourUpdate() {

        recyclerAdapter.notifyDataSetChanged();
        loadPost(ACTION_SHOW_LOADING_ITEM);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setupFeed() {

        recyclerAdapter= new HistoryRecyclerAdapter(getActivity(),BookingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);

//        mAdapter.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
//            }
//        });
//        mAdapter.setItemAnimator(new FeedItemAnimator());

    }
    public void loadPost(final String type) {

        BookingList.clear();
        BookingListId.clear();
//        showProgressDialog();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query first = db.collection("Bookings").whereEqualTo("Current_User_UID",uid);

        first.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        if (documentSnapshots.getDocuments().size() < 1) {

                            return;

                        }

                        for(DocumentSnapshot document : documentSnapshots.getDocuments()) {

                            Booking comment = document.toObject(Booking.class);
                            BookingList.add(comment);
                            BookingListId.add(document.getId());

                            Log.e("dbbd",document.getId());
                            Log.e("dbbd", String.valueOf(document.getData()));

                        }


                        setupFeed();
                    }

                });
    }

}
