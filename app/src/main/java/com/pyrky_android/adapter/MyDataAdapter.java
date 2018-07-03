package com.pyrky_android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pyrky_android.R;


/**
 * Created by thulirsoft on 7/2/18.
 */

public class MyDataAdapter extends RecyclerView.Adapter<MyDataAdapter.ViewHolder> {

    Context context;
    int[] icons;
    LayoutInflater inflater;
    String[] languages;
    public MyDataAdapter(Context context, int[] icons, String[] languages) {
        this.context = context;
        this.icons = icons;
        this.languages = languages;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public MyDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_spinner, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDataAdapter.ViewHolder holder, int position) {

        holder.carIcon.setImageResource(R.mipmap.ic_launcher_round);
        holder.carType.setText(languages[position]);
    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carIcon;
        TextView carType;
        ViewHolder(View itemView) {
            super(itemView);
            carIcon = itemView.findViewById(R.id.car_icon);
            carType = itemView.findViewById(R.id.car_size);

        }
    }
}
