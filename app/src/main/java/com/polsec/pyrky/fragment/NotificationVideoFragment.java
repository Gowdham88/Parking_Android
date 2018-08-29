package com.polsec.pyrky.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.adapter.NotificationAdapter;

public class NotificationVideoFragment extends Fragment{

    ImageView BackImg;
    TextView  TitlaTxt;
    VideoView videoView;
    private String urlStream;
    private android.support.v7.app.AlertDialog dialog;


    public static NotificationVideoFragment newInstance() {
        return new NotificationVideoFragment();
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.GONE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notification_video_fragment, null);

        BackImg=(ImageView) view.findViewById(R.id.back_icon);
        TitlaTxt=(TextView)view.findViewById(R.id.extra_title);
        TitlaTxt.setText("Notifications");
        BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        videoView = (VideoView)view.findViewById(R.id.VideoView);



//        MediaController mc = new MediaController(getActivity());
//        videoView.setMediaController(mc);

//        setupVideoView();
        showProgressDialog();

        videoView.setMediaController(new MediaController(getActivity()));
        videoView.setVideoURI(Uri.parse("http://playertest.longtailvideo.com/adaptive/wowzaid3/playlist.m3u8"));
//        videoView.setVideoURI(Uri.parse("http://dev.stream.polsecled.com:1935/polsec-uai/polsec_camera_2_confins.stream/playlist.m3u8"));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                hideProgressDialog();
                mp.start();

            }
        });

//        urlStream = "http://dev.stream.polsecled.com:1935/polsec-uai/polsec_camera_2_confins.stream/playlist.m3u8";
//
//        videoView.setVideoURI(Uri.parse(urlStream));

//        videoView.setVideoURI(Uri.parse("http://dev.stream.polsecled.com:1935/polsec-uai/polsec_camera_2_confins.stream/playlist.m3u8"));
//

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//
//                Fragment fragment = new NotificationFragment();
//                if (fragment != null) {
//                    FragmentManager fragmentManager = fragment.getSuFragmentManager().beginTransaction();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
//
//                    // Replace whatever is in the fragment_container view with this fragment,
//                    // and add the transaction to the back stack so the user can navigate back
//                    fragmentTransaction.replace(R.id.main_frame_layout, fragment).commit();
////                    fragmentTransaction.addToBackStack(null);
////
////                    // Commit the transaction
////                    fragmentTransaction.commit();
//                }
                FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
                transaction.replace(R.id.main_frame_layout, new NotificationFragment());
                transaction.commit();

            }
        }, 10000);


//
        return view;
    }

    public void showProgressDialog() {


        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
        //View view = getLayoutInflater().inflate(R.layout.progress);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void hideProgressDialog(){
        if(dialog!=null)
            dialog.dismiss();
    }
}
