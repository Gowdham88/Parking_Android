package com.polsec.pyrky.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.polsec.pyrky.R;


/**
 * Created by thulirsoft on 7/2/18.
 */

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {
    private Context context;
    private int[] icons;
    private LayoutInflater inflater;
    private String[] languages;
    private String[] mCarranze;
    int mCarIcon;
    public CarouselAdapter(Context context, int[] icons, String[] languages, String[] carCategoryMeter, int mCarIcon) {
        this.context = context;
        this.icons = icons;
        this.languages = languages;
        this.mCarranze=carCategoryMeter;
        this.mCarIcon=mCarIcon;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CarouselAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_carousel, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselAdapter.ViewHolder holder, int position) {
        int pos=holder.getAdapterPosition();

//        if(mCarIcon==pos){
//
//        }

        holder.carIcon.setImageResource(icons[position]);
        holder.carType.setText(languages[position]);
        holder.Cardim.setText(mCarranze[position]);

    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carIcon;
        TextView carType,Cardim;
        ViewHolder(View itemView) {
            super(itemView);
            carIcon = itemView.findViewById(R.id.car_icon);
            carType = itemView.findViewById(R.id.car_size);
                    Cardim = itemView.findViewById(R.id.car_dimension);

        }
    }
}
