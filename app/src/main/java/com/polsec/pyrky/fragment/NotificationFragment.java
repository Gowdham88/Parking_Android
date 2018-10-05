package com.polsec.pyrky.fragment;

import android.content.ClipData;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.adapter.NotificationAdapter;
import com.polsec.pyrky.helper.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class NotificationFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    RecyclerView mRecyclerView;
    NotificationAdapter mRecyclerAdapter;
    List<String> items = new ArrayList<>();
    List<String> items1 = new ArrayList<>();


    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_notification, null);

        items.clear();
        items1.clear();

        items.add("Hey buddy, something happened to your car");
        items.add("Hey buddy, something happened to your car");
        items.add("something happened to your car");


        items1.add("2 minutes");
        items1.add("5 days ago");
        items1.add("2 months ago");


        mRecyclerView = view.findViewById(R.id.notification_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerAdapter = new NotificationAdapter(getActivity(),items,items1);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationAdapter.ViewHolder) {


            // get the removed item name to display it in snack bar
//            String name = items.get(position);

            // backup of removed item for undo purpose
//             String deletedItem = items.get(viewHolder.getAdapterPosition());
//            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mRecyclerAdapter.removeItem(viewHolder.getAdapterPosition());
            mRecyclerAdapter.notifyDataSetChanged();


            // showing snack bar with Undo option
//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // undo is selected, restore the deleted item
//                    mAdapter.restoreItem(deletedItem, deletedIndex);
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
        }
    }
}
