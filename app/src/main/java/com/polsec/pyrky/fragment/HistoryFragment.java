package com.polsec.pyrky.fragment;

import android.content.Context;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.polsec.pyrky.R;
import com.polsec.pyrky.adapter.HistoryRecyclerAdapter;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.pojo.Camera;
import com.polsec.pyrky.preferences.PreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class HistoryFragment extends Fragment {

    RecyclerView mRecyclerView;
    HistoryRecyclerAdapter recyclerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String uid;
    FirebaseFirestore db;
    List<Booking> BookingList = new ArrayList<Booking>();
    List<Booking> mFilteredBookingList = new ArrayList<Booking>();
    List<String> BookingListId = new ArrayList<String>();
    List<Camera>CameraList = new ArrayList<Camera>();
    Map<String, Object> bookingid = new HashMap<>();
    private android.support.v7.app.AlertDialog dialog;

    Map<String, Object> bookingid1=new HashMap<>();
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";
    public static final String ACTION_SHOW_DEFAULT_ITEM = "action_show_default_item";

    public HistoryFragment() {

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
        View view = inflater.inflate(R.layout.fragment_history, null);

        mRecyclerView = view.findViewById(R.id.history_recycler);
        db = FirebaseFirestore.getInstance();

        uid = PreferencesHelper.getPreference(getContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);



        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    retrieveContents();
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


    }

    private void setupFeed() {

        recyclerAdapter= new HistoryRecyclerAdapter(getActivity(),mFilteredBookingList,bookingid1,CameraList,recyclerAdapter,mRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);

        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    //Retrieve Booking records using Current_User_UID field.
    public void loadPost(final String type) {

        BookingList.clear();
        BookingListId.clear();
        mFilteredBookingList.clear();
        showProgressDialog();
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

    //Retrieve Booking records using Current_User_UID field.
    public void loadPostval(final String type) {

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if(document.contains("Booking_ID")){

                            hideProgressDialog();
                            bookingid = document.getData();


                            bookingid1= (Map<String, Object>) bookingid.get("Booking_ID");


                            String count = String.valueOf(bookingid1.size());
                            Log.e("count", count);


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

                                    if(!value){
                                        mFilteredBookingList.add(BookingList.get(i));
                                    }

                                }
                            }




                        }
                        else{

                        }

                    } else {

                    }
                } else {

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
