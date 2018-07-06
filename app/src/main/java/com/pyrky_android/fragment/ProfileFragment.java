package com.pyrky_android.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pyrky_android.R;
import com.pyrky_android.adapter.MySpinnerAdapter;
import com.pyrky_android.preferences.PreferencesHelper;

/**
 * Created by thulirsoft on 7/6/18.
 */

public class ProfileFragment extends android.support.v4.app.Fragment {

    String[] mLanguages = { "Compact[3 - 4.5m]", "Small[3 - 4.5m]", "Mid size[3 - 4.5m]", "Full[3 - 4.5m]", "Van/Pick-up[3 - 4.5m]" };
    TextInputEditText email;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        email = view.findViewById(R.id.et_email);
        final Spinner spinner = view.findViewById(R.id.car_category_spinner);
        Button save = view.findViewById(R.id.save_button);

        email.setText(PreferencesHelper.getPreference(getActivity(),PreferencesHelper.PREFERENCE_EMAIL));
        spinner.setAdapter(new MySpinnerAdapter(getActivity(), R.layout.item_carousel, mLanguages));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData(email.getText().toString().trim(),spinner.getSelectedItem().toString());
            }
        });
        return view;
    }

    private void UpdateData(String email, String carCategory) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference contact = db.collection("users").document(PreferencesHelper.getPreference(getActivity(), PreferencesHelper.PREFERENCE_FIREBASE_UUID));

        contact.update("email",email);
        contact.update("carCategory", carCategory)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

        @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
