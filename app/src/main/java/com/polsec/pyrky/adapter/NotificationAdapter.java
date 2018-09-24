package com.polsec.pyrky.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.ViewImage.ViewImageActivity;
import com.polsec.pyrky.activity.arnavigation.ArNavActivity;
import com.polsec.pyrky.fragment.NotificationVideoFragment;
import com.polsec.pyrky.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    Context context;
    String[] mMessage,mTime;

    List<String> items = new ArrayList<>();
    List<String> items1 = new ArrayList<>();
    public NotificationAdapter(Context context, List<String> mMessage, List<String> mTime) {
        this.context=context;
        this.items = mMessage;
        this.items1 = mTime;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);


        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
      holder.notificationMessage.setText(items.get(position));
        holder.notificationTime.setText(items1.get(position));
        holder.requestVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup();

            }
        });
    }

    private void showpopup() {

        final
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        LayoutInflater factory = LayoutInflater.from(context);
        View bottomSheetView = factory.inflate(R.layout.notification_popup, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheetView.getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) bottomSheetView.getParent()).setBackgroundColor(Color.TRANSPARENT);

        TextView map = (TextView) bottomSheetView.findViewById(R.id.maps_title);
        TextView pyrky = (TextView) bottomSheetView.findViewById(R.id.pyrky_title);
//        GalleryIcon = (ImageView) bottomSheetView.findViewById(R.id.gallery_icon);
//        CameraIcon = (ImageView) bottomSheetView.findViewById(R.id.camera_image);
        TextView cancel = (TextView) bottomSheetView.findViewById(R.id.cancel_txt);


        pyrky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 FragmentTransaction transaction =  ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame_layout, NotificationVideoFragment.newInstance());
                transaction.addToBackStack(null).commit();

                bottomSheetDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });


    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeItem(int position) {
        items.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
//        notifyItemRemoved(position);
//        notifyDataSetChanged();
//        notifyItemRangeChanged(position, items.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notificationMessage,notificationTime,requestVideo;
        RelativeLayout Viewbackbaground;
        public View Viewforebaground;

        public ViewHolder(View itemView) {
            super(itemView);

            notificationMessage = itemView.findViewById(R.id.notification_message);
            notificationTime = itemView.findViewById(R.id.notification_time);
            requestVideo = itemView.findViewById(R.id.request_video);
            Viewforebaground= itemView.findViewById(R.id.view_foreground);
            Viewbackbaground= itemView.findViewById(R.id.view_background);
        }
    }
}
