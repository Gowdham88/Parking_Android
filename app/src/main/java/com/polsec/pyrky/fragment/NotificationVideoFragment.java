package com.polsec.pyrky.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;

public class NotificationVideoFragment extends Fragment {

    private final int VIDEO_MAXIMUM_DURATION = 50000;
    RelativeLayout BackImg;
    TextView titleText;
    VideoView videoView;
    private android.support.v7.app.AlertDialog dialog;
    private Handler handler;
    private TimeoutRunnable timeoutRunnable = new TimeoutRunnable();


    public static NotificationVideoFragment newInstance() {
        return new NotificationVideoFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).findViewById(R.id.myview).setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notification_video_fragment, null);

        BackImg = view.findViewById(R.id.back_icon);
        titleText = view.findViewById(R.id.extra_title);
        titleText.setText(R.string.title_notifications);
        BackImg.setOnClickListener(view1 -> getActivity().onBackPressed());

        videoView = view.findViewById(R.id.VideoView);

        videoView.setMediaController(new MediaController(getActivity()));
        videoView.setVideoURI(Uri.parse("http://playertest.longtailvideo.com/adaptive/wowzaid3/playlist.m3u8"));

        handler = new Handler();
        showProgressDialog();
        videoView.requestFocus();
        videoView.setOnPreparedListener(mp -> {
            hideProgressDialog();
            mp.start();
        });

        /* starting a timeout runnable */
        handler.postDelayed(timeoutRunnable, VIDEO_MAXIMUM_DURATION);

        return view;
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacks(timeoutRunnable);
        }
        super.onDestroy();
    }

    public void showProgressDialog() {


        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
        //View view = getLayoutInflater().inflate(R.layout.progress);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void hideProgressDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    private class TimeoutRunnable implements Runnable {
        @Override
        public void run() {

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
            transaction.replace(R.id.main_frame_layout, new NotificationFragment());
            transaction.commit();

        }
    }


}
