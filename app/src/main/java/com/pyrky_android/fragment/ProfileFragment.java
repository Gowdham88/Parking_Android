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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pyrky_android.pojo.ProfileImage;
import com.pyrky_android.R;
import com.pyrky_android.adapter.MySpinnerAdapter;
import com.pyrky_android.preferences.PreferencesHelper;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class ProfileFragment extends android.support.v4.app.Fragment {

    String[] mLanguages = {"Compact[3.5 - 4.5m]", "Small[2.5 - 3.5m]", "Mid size[4 - 5m]", "Full[5 - 5.5m]", "Van/Pick-up[5.5 - 6.5m]"};
    TextInputEditText email;
    ImageView mProfileImage;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();


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
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final ImagePicker imagePicker = ImagePicker.create(this);

        email = view.findViewById(R.id.et_email);
        mProfileImage = view.findViewById(R.id.profile_img);
        final Spinner spinner = view.findViewById(R.id.car_category_spinner);
        Button save = view.findViewById(R.id.save_button);
        String profilePic = PreferencesHelper.getPreference(getActivity(),PreferencesHelper.PREFERENCE_PROFILE_PIC);
       downloadImage();


        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* imagePicker.single();
                imagePicker.showCamera(true);
                imagePicker.start();*/
               chooseImage();
            }
        });
        email.setText(PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_EMAIL));
        spinner.setAdapter(new MySpinnerAdapter(getActivity(), R.layout.item_carousel, mLanguages));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData(email.getText().toString().trim(), spinner.getSelectedItem().toString());
            }
        });
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
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);

            PreferencesHelper.setPreference(getActivity(),PreferencesHelper.PREFERENCE_PROFILE_PIC,image.getPath());
//            Glide.with(getActivity()).load(image.getPath()).into(mProfileImage);


            Toast.makeText(getActivity(), image.getPath(), Toast.LENGTH_SHORT).show();
        }

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                uploadImage();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                mProfileImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void writeToDatabase(String profileImage,String email){

        ProfileImage profileImages = new ProfileImage(profileImage,email);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String profileImageUrl = UUID.randomUUID().toString();
            PreferencesHelper.setPreference(getActivity(),PreferencesHelper.PREFERENCE_PROFILE_PIC,profileImageUrl);
            StorageReference ref = storageReference.child("profile_image/"+ profileImageUrl);
//            StorageReference ref = storageReference.child("profile_image/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

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

}