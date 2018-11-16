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
    String mPlace[] = {"Rio de Janeiro","Brasília","Salvador","Fortaleza","Belo Horizonte"};
    String mTimeDate[] = {"05 Jan, 12.30am","15 Feb, 03.10pm","25 Mar, 05.50pm","17 Jun, 10.30am","01 Jan, 12.00am"};
    RecyclerView mRecyclerView;
    View view;
    String uid;
    CurrentBookingRecyclerAdapter recyclerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private android.support.v7.app.AlertDialog dialog;

    List<Booking> BookingList = new ArrayList<Booking>();
    List<Booking> mFilteredBookingList = new ArrayList<Booking>();
    List<String> BookingListId = new ArrayList<String>();
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";
    public static final String ACTION_SHOW_DEFAULT_ITEM = "action_show_default_item";

    FirebaseFirestore db;

    FirebaseAuth mAuth;
    String mUid;
    Map<String, Object> bookingid = new HashMap<>();

    Map<String, Object> bookingid1 = new HashMap<>();
    public CurrentBookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//        ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_booking, null);
        uid = PreferencesHelper.getPreference(getContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);
//        loadPost(ACTION_SHOW_LOADING_ITEM);
        db = FirebaseFirestore.getInstance();

        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);

        mRecyclerView = view.findViewById(R.id.current_booking_recycler);
        recyclerAdapter= new CurrentBookingRecyclerAdapter(getActivity(),BookingList,bookingid1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);
        mRecyclerView.setHasFixedSize(true);
        recyclerAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        retrieveContents();
                    }
                }
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
//        loadPostval(ACTION_SHOW_LOADING_ITEM);
//        recyclerAdapter.notifyDataSetChanged();
//        swipeRefreshLayout.setRefreshing(false);
    }

    private void setupFeed() {

        recyclerAdapter= new CurrentBookingRecyclerAdapter(getActivity(),mFilteredBookingList,bookingid1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);

        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    public void loadPost(final String type) {

        BookingList.clear();
        BookingListId.clear();
        mFilteredBookingList.clear();
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

                        }
                        loadPostval(ACTION_SHOW_LOADING_ITEM);
                    }

                });
    }

    public void loadPostval(final String type) {

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {


                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.contains("Booking_ID")){
                            showProgressDialog();
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        bookingid = document.getData();


                        bookingid1= (Map<String, Object>) bookingid.get("Booking_ID");


//                        String count = String.valueOf(bookingid1.size());
//                        Log.e("count", count);


//                                    followingcount = 1;
                        for (Map.Entry<String, Object> entry : bookingid1.entrySet()) {
                            System.out.println(entry.getKey() + " = " + entry.getValue());

                            Boolean val = (Boolean) entry.getValue();
                            String values = String.valueOf(val);

                            Log.e("valuesh", values);
//                                Toast.makeText(getActivity(), followcount, Toast.LENGTH_SHORT).show();

                        }

                            for (int i = 0; i < BookingList.size();i++){
                                if(bookingid1.containsKey(BookingList.get(i).getDocumentID())){
                                    Boolean value=(Boolean) bookingid1.get(BookingList.get(i).getDocumentID());

                                    if(value){
                                        mFilteredBookingList.add(BookingList.get(i));

                                    }

                                }
                            }

                            hideProgressDialog();
                        }
                        else{
                            hideProgressDialog();
                        }
//                        recyclerAdapter.notifyDataSetChanged();


                    } else {
//                        Log.d(TAG, "No such document");
                        hideProgressDialog();
                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());

                }

                setupFeed();

            }
        });

    }
    public void showProgressDialog() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void hideProgressDialog(){
        if(dialog!=null)
            dialog.dismiss();
    }
}
