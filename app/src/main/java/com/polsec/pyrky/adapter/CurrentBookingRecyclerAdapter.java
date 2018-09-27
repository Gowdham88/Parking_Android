package com.polsec.pyrky.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.polsec.pyrky.R;
import com.polsec.pyrky.pojo.Booking;
import com.polsec.pyrky.pojo.UsersBooking;
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
    List<UsersBooking> BookinguserList = new ArrayList<UsersBooking>();
    Boolean val;

    List<Booking> bookingList = new ArrayList<Booking>();
    String Datetime,Datemonth,Dateday,Datemothname;
    public CurrentBookingRecyclerAdapter(Context context, List<Booking> bookingList, Map<String, Object> bookingid1) {
        this.context = context;
        this.bookingList = bookingList;
        this.bookingid1=bookingid1;


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
        double time= Double.parseDouble(String.valueOf(bookingList.get(position).getDateTime()));
        int vali= (int) time;

        long dv = Long.valueOf(String.valueOf(vali))*1000;// its need to be in milisecond
        Date df = new Date(Long.valueOf((long) dv));
        Datetime= new SimpleDateFormat("dd MMM,  hh:mma").format(df);
        String str = Datetime.replace("AM", "am").replace("PM","pm");


//        Log.e("vv", String.valueOf(str));
        holder.dateTime.setText(str);


        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();

        if(bookingid1.containsKey(bookingList.get(position).getDocumentID())){
            Boolean value=(Boolean) bookingid1.get(bookingList.get(position).getDocumentID());

            if(value){
                holder.city.setText(bookingList.get(position).getDestName());
            }
            else {
                holder.itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
        }
//
    }

    public static String getMonthShortName(int monthNumber)
    {
        String monthName="";

        if(monthNumber>=0 && monthNumber<12)
            try
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, monthNumber);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                simpleDateFormat.setCalendar(calendar);
                monthName = simpleDateFormat.format(calendar.getTime());
            }
            catch (Exception e)
            {
                if(e!=null)
                    e.printStackTrace();
            }
        return monthName;
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
