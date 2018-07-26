package com.pyrky_android.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.pyrky_android.activity.SignUpActivity;
import com.pyrky_android.pojo.ProfileImage;
import com.pyrky_android.R;
import com.pyrky_android.adapter.MySpinnerAdapter;
import com.pyrky_android.preferences.PreferencesHelper;
import com.pyrky_android.utils.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class ProfileFragment extends android.support.v4.app.Fragment {

    String[] mLanguages = {"Compact[3.5 - 4.5m]", "Small[2.5 - 3.5m]", "Mid size[4 - 5m]", "Full[5 - 5.5m]", "Van/Pick-up[5.5 - 6.5m]"};
    TextView email;
    CircleImageView mProfileImage;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    ActionBar actionBar;
    ImagePicker mImagePicker;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    UploadTask uploadTask;
    TextView nameEdt;
    String mEmail,mName,mProfilepic;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Image picker
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();

//        mImagePicker = new com.myhexaville.smartimagepicker.ImagePicker(getActivity(),this, imageUri -> {mProfileImage.setImageURI(imageUri);})
//                .setWithImageCrop(1,1);
        mEmail = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_EMAIL);
        mName = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_USER_NAME);
        mProfilepic = PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_PROFILE_PIC);
        email = view.findViewById(R.id.et_email);
        mProfileImage = ( CircleImageView ) view.findViewById(R.id.profile_img);
        nameEdt = ( TextView ) view.findViewById(R.id.name_txt);
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
        email.setText(mEmail);
        nameEdt.setText(mName);
        Log.e("mProfilepic", mProfilepic);
        if (mProfilepic != null && !mProfilepic.isEmpty()) {
            Picasso.with(getActivity())
                    .load(mProfilepic)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(mProfileImage);

        }
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

            // Create file metadata including the content type
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();

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
//                    downloadUrl();
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



/*
    private void downloadImage(){

        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
                storageRef.child("profile_image/"+PreferencesHelper.getPreference(getActivity(),PreferencesHelper.PREFERENCE_PROFILE_PIC))
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getActivity())
                                .load(storageReference)
                                .into(mProfileImage);
                    }
                });
    }
*/
