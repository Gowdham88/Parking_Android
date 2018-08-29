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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.polsec.pyrky.R;
import com.polsec.pyrky.adapter.CurrentBookingRecyclerAdapter;
import com.polsec.pyrky.adapter.HistoryRecyclerAdapter;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.pojo.Camera;
import com.polsec.pyrky.preferences.PreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.crashlytics.android.answers.Answers.TAG;

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
    FirebaseFirestore db;
    List<Booking> BookingList = new ArrayList<Booking>();
    List<String> BookingListId = new ArrayList<String>();
    List<Camera>CameraList = new ArrayList<Camera>();
    List<String> CameraListId = new ArrayList<String>();
    List<Booking> Booklist = new ArrayList<Booking>();
    int i;



    String mUid;
    Map<String, Object> bookingid = new HashMap<>();

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
        loadPost(ACTION_SHOW_LOADING_ITEM);
        loadPostval(ACTION_SHOW_LOADING_ITEM);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null);

        mRecyclerView = view.findViewById(R.id.history_recycler);
        db = FirebaseFirestore.getInstance();

        uid = PreferencesHelper.getPreference(getContext(), PreferencesHelper.PREFERENCE_FIREBASE_UUID);
//        loadPost(ACTION_SHOW_LOADING_ITEM);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
//                        feedAdapter.notifyDataSetChanged();

                        doYourUpdate();
                    }
                }
        );
        recyclerAdapter= new HistoryRecyclerAdapter(getActivity(),BookingList,bookingid1,CameraList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);
        mRecyclerView.setHasFixedSize(true);
        recyclerAdapter.notifyDataSetChanged();

        return view;
    }

    private void doYourUpdate() {

        recyclerAdapter.notifyDataSetChanged();
        loadPost(ACTION_SHOW_LOADING_ITEM);
        loadPostval(ACTION_SHOW_LOADING_ITEM);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setupFeed() {

        recyclerAdapter= new HistoryRecyclerAdapter(getActivity(),BookingList,bookingid1,CameraList);
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

//                            Log.e("dbbd",document.getId());
//                            Log.e("dbbd", String.valueOf(document.getData()));

                        }
//                        loadPostval(ACTION_SHOW_LOADING_ITEM);


                        setupFeed();
                        recyclerAdapter.notifyDataSetChanged();
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
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
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
                            setupFeed();
//                                Toast.makeText(getActivity(), followcount, Toast.LENGTH_SHORT).show();

                        }


//                        recyclerAdapter.notifyDataSetChanged();


                    } else {
//                        Log.d(TAG, "No such document");

                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());

                }
            }
        });

    }

}
