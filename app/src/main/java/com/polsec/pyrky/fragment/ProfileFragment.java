package com.polsec.pyrky.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.pojo.ProfileImage;
import com.polsec.pyrky.R;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class ProfileFragment extends android.support.v4.app.Fragment {

    String[] mCarCategoryId = {"0", "1", "2", "3", "4" };
    String[] mCarCategory = {"Compact", "Small", "Mid size", "Full", "Van/Pick-up"};
    String[] mCarRange = {"[3.5 - 4.5m]", "[2.5 - 3.5m]", "[4 - 5m]", "[5 - 5.5m]", "[5.5 - 6.5m]"};
    int mIcons[] = {R.drawable.compactcar_icon,R.drawable.smallcar_icon,R.drawable.midsizecar_icon,R.drawable.fullcar_icon, R.drawable.vanpickupcar_icon};
    TextView email,carSize,carDimension;
    CircleImageView mProfileImage;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    ActionBar actionBar;
    ImagePicker mImagePicker;
    ImageView carIcon;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    UploadTask uploadTask;
    TextView nameEdt,emailTxt;
    String mEmail,mName,mProfilepic;
    int mCarIcon;
    ImageView SettingsImg;
    private int avatarSize;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.setIcon(R.drawable.ic_settings_new);

    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).findViewById(R.id.myview).setVisibility(View.VISIBLE);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mEmail = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_EMAIL);
        mName = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_USER_NAME);
        mProfilepic = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_PROFILE_PIC);
        mCarIcon = Integer.parseInt(PreferencesHelper.getPreference(getActivity(),PreferencesHelper.PREFERENCE_PROFILE_CAR));

        email = view.findViewById(R.id.et_email);
        mProfileImage = view.findViewById(R.id.profile_img);
        nameEdt = view.findViewById(R.id.name_txt);
        carIcon = view.findViewById(R.id.car_icon);
        carSize = view.findViewById(R.id.car_size);
        carDimension = view.findViewById(R.id.car_dimension);

        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        email.setText(mEmail);
        nameEdt.setText(mName);
        carIcon.setImageResource(mIcons[mCarIcon]);
        carSize.setText(mCarCategory[mCarIcon]);
        carDimension.setText(mCarRange[mCarIcon]);

        Log.e("mProfilepic", mProfilepic);
        if (mProfilepic != null && !mProfilepic.isEmpty()) {
            Picasso.with(getActivity())
                    .load(mProfilepic)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(mProfileImage);
        }


       if(mCarIcon==0){
           carIcon.setImageResource(R.drawable.compactcar_icon);
           carSize.setText("Compact");
           carDimension.setText("[3.5 - 4.5m]");
       }
       else if(mCarIcon==1){
           carIcon.setImageResource(R.drawable.smallcar_icon);
           carSize.setText("Small");
           carDimension.setText("[2.5 - 3.5m]");
       }
       else if(mCarIcon==2){
           carIcon.setImageResource(R.drawable.midsizecar_icon);
           carSize.setText("Mid size");
           carDimension.setText("[4 - 5m]");
        }
       else if(mCarIcon==3){
           carIcon.setImageResource(R.drawable.fullcar_icon);
           carSize.setText("Full");
           carDimension.setText("[5 - 5.5m]");
       }
       else {
           carIcon.setImageResource(R.drawable.vanpickupcar_icon);
           carSize.setText("Van/Pick-up");
           carDimension.setText("[5.5 - 6.5m]");
       }

//        SettingsImg=view.findViewById(R.id.action_settings);
//        SettingsImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.main_frame_layout, SettingsFragment.newInstance());
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });
        return view;
    }
    private void UpdateData(final String email, String carCategory) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference contact = db.collection("users").document(PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_FIREBASE_UUID));

        contact.update("email", email);
        contact.update("carCategory", carCategory)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        PreferencesHelper.setPreference(getActivity(),PreferencesHelper.PREFERENCE_EMAIL,email);
                        Toast.makeText(getActivity(), "Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mImagePicker.handleActivityResult(resultCode,requestCode,data);
        File file = mImagePicker.getImageFile();
        if (file != null){
            filePath = Uri.fromFile(file);
            uploadImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame_layout, SettingsFragment.newInstance());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void writeToDatabase(String profileImage, String email){

        ProfileImage profileImages = new ProfileImage(profileImage,email);
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String profileImageUrl = UUID.randomUUID().toString();
            Uri file = Uri.fromFile(new File(String.valueOf(filePath)));
            PreferencesHelper.setPreference(getActivity(),PreferencesHelper.PREFERENCE_PROFILE_PIC,profileImageUrl);

            StorageReference ref = storageReference.child("profile_image/"+ filePath.getLastPathSegment());
            uploadTask = ref.putFile(filePath);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), taskSnapshot.getUploadSessionUri().toString(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploading "+(int)progress+"%");
                }
            });

        }
    }


}