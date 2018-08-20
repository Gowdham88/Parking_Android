package com.polsec.pyrky.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.NearestLocMapsActivity;
import com.polsec.pyrky.activity.ViewImage.ViewImageActivity;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.preferences.PreferencesHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thulirsoft on 7/9/18.
 */

public class CurrentBookingRecyclerAdapter extends RecyclerView.Adapter<CurrentBookingRecyclerAdapter.ViewHolder> {
    private Context context;
    FirebaseFirestore db;

    FirebaseAuth mAuth;
    String mUid;
    Map<String, Object> bookingid = new HashMap<>();

    Map<String, Object> bookingid1=new HashMap<>();

    List<Booking> bookingList = new ArrayList<Booking>();
    String Datetime,Datemonth,Dateday,Datemothname;
    public CurrentBookingRecyclerAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_current_booking_list, parent, false);

        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUid = PreferencesHelper.getPreference(context, PreferencesHelper.PREFERENCE_FIREBASE_UUID);
        double time= bookingList.get(position).getDateTime();

        long dv = Long.valueOf(String.valueOf(time))*1000;// its need to be in milisecond
        Date df = new Date(dv);
        Datetime= new SimpleDateFormat("dd MMM,  hh:mma").format(df);
        String str = Datetime.replace("AM", "am").replace("PM","pm");
//        Dateday= new SimpleDateFormat("").format(df);
//        Datetime= new SimpleDateFormat("").format(df);
//        Datemothname=getMonthShortName(Integer.parseInt(Datemonth));

        Log.e("vv",str);

        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
        Boolean status= Boolean.valueOf(bookingList.get(position).getBookingStatus());

//
//        final FirebaseUser user = mAuth.getCurrentUser();
//        DocumentReference docRef = db.collection("users").document(user.getUid());
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//
//                if (documentSnapshot.exists()){
//
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//
//                    DocumentReference docRef = db.collection("users").document(mUid);
//                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
////                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                                    bookingid = document.getData();
//
//
//                                    bookingid1= (Map<String, Object>) bookingid.get("Booking_ID");
//
//
//                                    String count = String.valueOf(bookingid1.size());
//                                    Log.e("count", count);
//
//
////                                    followingcount = 1;
//                                    for (Map.Entry<String, Object> entry : bookingid1.entrySet()) {
//                                        System.out.println(entry.getKey() + " = " + entry.getValue());
//
//                                        Boolean val = (Boolean) entry.getValue();
//                                        String values = String.valueOf(val);
//
//                                        Log.e("values", values);
////
//                                        if (bookingList.get(position).getDocumentID().equals(bookingid1)) {
//                                            if(bookingList.get(position).getDocumentID().equals(true)){
//                                                holder.city.setText(bookingList.get(position).getDestName());
//                                                holder.dateTime.setText(str);
//                                            }
//
//                                        }
//                                        else{
//
//                                            holder.itemView.setVisibility(View.GONE);
//                                            param.height = 0;
//                                            param.width = 0;
//                                        }
//
////                                Toast.makeText(getActivity(), followcount, Toast.LENGTH_SHORT).show();
//
//                                    }
//
//
//
//                                } else {
////                        Log.d(TAG, "No such document");
//
//                                }
//                            } else {
////                    Log.d(TAG, "get failed with ", task.getException());
//
//                            }
//                        }
//                    });
//
////                    Toast.makeText(ViewImageActivity.this, "ok", Toast.LENGTH_SHORT).show();
//
//
//                } else {
//
//
//
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Log.w("Error", "Error adding document", e);
//                Toast.makeText(context,"Login failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//
    }


    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView city,dateTime,viewCar;
        ViewHolder(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.place);
            dateTime = itemView.findViewById(R.id.date_time);
            viewCar = itemView.findViewById(R.id.view_car_text);
        }
    }
}
