package com.polsec.pyrky.fragment;

import android.graphics.drawable.ColorDrawable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.polsec.pyrky.R;
import com.polsec.pyrky.adapter.CurrentBookingRecyclerAdapter;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.preferences.PreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class CurrentBookingsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private String uid;
    private CurrentBookingRecyclerAdapter recyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private android.support.v7.app.AlertDialog dialog;
    private List<Booking> BookingList = new ArrayList<>();
    private List<Booking> mFilteredBookingList = new ArrayList<>();
    private List<String> BookingListId = new ArrayList<>();
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";
    private FirebaseFirestore db;
    private Map<String, Object> bookingid = new HashMap<>();
    private Map<String, Object> bookingid1 = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_booking, null);
        uid = PreferencesHelper.getPreference(getContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        db = FirebaseFirestore.getInstance();
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mRecyclerView = view.findViewById(R.id.current_booking_recycler);
        recyclerAdapter = new CurrentBookingRecyclerAdapter(getActivity(), BookingList, bookingid1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);
        mRecyclerView.setHasFixedSize(true);
        recyclerAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setOnRefreshListener(
                () -> retrieveContents()
        );


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrieveContents();
    }


    private void retrieveContents() {
        loadPost(ACTION_SHOW_LOADING_ITEM);
    }

    private void setupFeed() {

        recyclerAdapter = new CurrentBookingRecyclerAdapter(getActivity(), mFilteredBookingList, bookingid1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    public void loadPost(final String type) {

        BookingList.clear();
        BookingListId.clear();
        mFilteredBookingList.clear();
        showProgressDialog();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query first = db.collection("Bookings").whereEqualTo("Current_User_UID", uid);

        first.get()
                .addOnSuccessListener(documentSnapshots -> {

                    if (documentSnapshots.getDocuments().size() < 1) {
                        return;
                    }

                    for (DocumentSnapshot document : documentSnapshots.getDocuments()) {

                        double latitude = 0.0, longitude = 0.0;

                        if (document.get("DestLat") instanceof String)
                            latitude = Double.parseDouble(document.getString("DestLat"));
                        else
                            latitude = document.getDouble("DestLat");

                        if (document.get("DestLong") instanceof String)
                            longitude = Double.parseDouble(document.getString("DestLat"));
                        else
                            longitude = document.getDouble("DestLong");


                        Booking comment = new Booking(document.getString("Current_User_UID"), latitude, longitude, document.getString("DestName"), document.getDouble("DateTime"),
                                document.getBoolean("bookingStatus"), document.getString("cameraId"), document.getString("documentID"),
                                document.getDouble("parkingSpaceRating"), document.getBoolean("protectCar"));
                        BookingList.add(comment);
                        BookingListId.add(document.getId());

                    }
                    loadPostval(ACTION_SHOW_LOADING_ITEM);
                });
    }

    public void loadPostval(final String type) {

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    if (document.contains("Booking_ID")) {
                        hideProgressDialog();
                        bookingid = document.getData();

                        bookingid1 = (Map<String, Object>) bookingid.get("Booking_ID");

                        for (Booking booking : BookingList) {

                            if (bookingid1.containsKey(booking.getDocumentID())) {
                                Boolean value = (Boolean) bookingid1.get(booking.getDocumentID());
                                if (value) {
                                    mFilteredBookingList.add(booking);
                                    hideProgressDialog();
                                }

                            }
                        }

                    } else {
                        hideProgressDialog();
                    }

                } else {
                    hideProgressDialog();
                }
            }

            setupFeed();
        });

    }

    public void showProgressDialog() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void hideProgressDialog() {
        if (dialog != null)
            dialog.dismiss();
    }
}
