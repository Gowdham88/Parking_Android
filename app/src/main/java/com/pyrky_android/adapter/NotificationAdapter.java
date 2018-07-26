package com.pyrky_android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pyrky_android.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    Context context;
    String[] mMessage,mTime;
    public NotificationAdapter(String[] mMessage, String[] mTime) {
        this.mMessage = mMessage;
        this.mTime = mTime;
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
      holder.notificationMessage.setText(mMessage[position]);
        holder.notificationTime.setText(mTime[position]);
    }

    @Override
    public int getItemCount() {
        return mTime.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notificationMessage,notificationTime,requestVideo;
        public ViewHolder(View itemView) {
            super(itemView);

            notificationMessage = itemView.findViewById(R.id.notification_message);
            notificationTime = itemView.findViewById(R.id.notification_time);
            requestVideo = itemView.findViewById(R.id.request_video);
        }
    }
}
