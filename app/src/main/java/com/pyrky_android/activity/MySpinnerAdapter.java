package com.pyrky_android.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pyrky_android.R;


/**
 * Created by thulirsoft on 7/2/18.
 */

public class MySpinnerAdapter extends ArrayAdapter {

    String[] languages;
    Context context;
    public MySpinnerAdapter(@NonNull Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.languages = objects;
    }


    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        LayoutInflater inflater = ((Activity )context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.item_spinner, parent, false);

        TextView tvLanguage = layout.findViewById(R.id.car_size);
        ImageView img = layout.findViewById(R.id.car_icon);

        tvLanguage.setText(languages[position]);

//        tvLanguage.setTextColor(Color.rgb(75, 180, 225));


// Setting an image using the id's in the array
//        img.setImageResource(images[position]);

// Setting Special atrributes for 1st element
        if (position == 0) {
// Removing the image view
//            img.setVisibility(View.GONE);
// Setting the size of the text
            tvLanguage.setTextSize(20f);
// Setting the text Color
//            tvLanguage.setTextColor(Color.BLACK);
        }
        return layout;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}