package com.polsec.pyrky.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.polsec.pyrky.R;
import com.polsec.pyrky.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by thulirsoft on 7/4/18.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private boolean checked;
    private int lastClickedPosition;

    private List<Boolean> setValueForSeletedFilter;
    private String[] keysOfHashmap;
    private int buttonId = 0;
    private static final int CARCATEGORY = 0;
    private static final int SECURITY = 1;
    private static final int FILTER = 2;
    private static final int[] IMAGES = new int[]{R.drawable.fivestar, R.drawable.fourstar, R.drawable.threestar, R.drawable.twostar, R.drawable.onestar};
    String[] mCarCategory;
    android.content.res.Resources res;

    public ExpandableListAdapter(Context context, ArrayList<String> headersList, HashMap<String, List<String>> allChildItems) {



//    public ExpandableListAdapter(Context context, List<String> expandableListTitle,
////                                 HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = headersList;
        this.expandableListDetail = allChildItems;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

//        res = context.getResources();
//        mCarCategory =res.getStringArray(R.array.cartypes);
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(groupPosition, childPosition);

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;

        switch (groupPosition){
            case 0 :
                convertView = layoutInflater.inflate(R.layout.list_item_parking_type,null);
                RadioGroup rgps = (RadioGroup) convertView.findViewById(R.id.radio_group);

                RadioButton rb1 = convertView.findViewById(R.id.rb1);
                RadioButton rb2 = convertView.findViewById(R.id.rb2);
                RadioButton rb3 = convertView.findViewById(R.id.rb3);

                TableRow tablRow1 = convertView.findViewById(R.id.table1);
                TableRow tablRow2 = convertView.findViewById(R.id.table2);
                TableRow tablRow3 = convertView.findViewById(R.id.table3);


                if (Constants.PARKING_TYPES.size() > 0){
                    String checkedItem = Constants.PARKING_TYPES.get(0);

                    switch (checkedItem) {
                        case "Free street parking":
                            rb1.setChecked(true);
                            break;
                        case "Paid street parking":
                            rb2.setChecked(true);
                            break;
                        case "Paid parking":
                            rb3.setChecked(true);
                            break;
                    }
                }

                tablRow1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!rb1.isChecked()){
                            Constants.PARKING_TYPES.clear();
                            Constants.PARKING_TYPES.add("Free street parking");
                            rb1.setChecked(true);
                            rb2.setChecked(false);
                            rb3.setChecked(false);
                        }else {
                            Constants.PARKING_TYPES.remove("Free street parking");
                            rb1.setChecked(false);
                        }
                    }
                });
                tablRow2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!rb2.isChecked()){
                            Constants.PARKING_TYPES.clear();
                            Constants.PARKING_TYPES.add("Paid street parking");
                            rb1.setChecked(false);
                            rb2.setChecked(true);
                            rb3.setChecked(false);
                        }else {
                            Constants.PARKING_TYPES.remove("Paid street parking");
                            rb2.setChecked(false);
                        }
                    }
                });
                tablRow3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!rb3.isChecked()){
                            Constants.PARKING_TYPES.clear();
                            Constants.PARKING_TYPES.add("Paid parking");
                            rb1.setChecked(false);
                            rb2.setChecked(false);
                            rb3.setChecked(true);
                        }else {
                            Constants.PARKING_TYPES.remove("Paid parking");
                            rb3.setChecked(false);
                        }
                    }
                });

                rgps.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        switch (checkedId){
                            case R.id.rb1:
                                Constants.PARKING_TYPES.clear();
                                Constants.PARKING_TYPES.add("Free street parking");
//                                Toast.makeText(context, rb1.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb2:
                                Constants.PARKING_TYPES.clear();
                                Constants.PARKING_TYPES.add("Paid street parking");
//                                Toast.makeText(context, rb2.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb3:
                                Constants.PARKING_TYPES.clear();
                                Constants.PARKING_TYPES.add("Paid parking");
//                                Toast.makeText(context, rb3.getText(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

                break;
            case 1 :
                convertView = layoutInflater.inflate(R.layout.list_item_security_ratings, null);

                CheckBox checkBox1 = convertView.findViewById(R.id.checkbox1);
                CheckBox checkBox2 = convertView.findViewById(R.id.checkbox2);
                CheckBox checkBox3 = convertView.findViewById(R.id.checkbox3);
                CheckBox checkBox4 = convertView.findViewById(R.id.checkbox4);
                CheckBox checkBox5 = convertView.findViewById(R.id.checkbox5);

                TableRow tableRow1 = convertView.findViewById(R.id.table1);
                TableRow tableRow2 = convertView.findViewById(R.id.table2);
                TableRow tableRow3 = convertView.findViewById(R.id.table3);
                TableRow tableRow4 = convertView.findViewById(R.id.table4);
                TableRow tableRow5 = convertView.findViewById(R.id.table5);

                tableRow1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!checkBox1.isChecked()){
                            Constants.SECURITY_RATINGS.add("5 stars");
                            checkBox1.setChecked(true);
                        }else {
                            Constants.SECURITY_RATINGS.remove("5 stars");
                            checkBox1.setChecked(false);
                        }
                    }
                });
                tableRow2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!checkBox2.isChecked()){
                            Constants.SECURITY_RATINGS.add("4 stars");
                            checkBox2.setChecked(true);
                        }else {
                            Constants.SECURITY_RATINGS.remove("4 stars");
                            checkBox2.setChecked(false);
                        }

                    }
                });
                tableRow3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!checkBox3.isChecked()){
                            Constants.SECURITY_RATINGS.add("3 stars");
                            checkBox3.setChecked(true);
                        }else {
                            Constants.SECURITY_RATINGS.remove("3 stars");
                            checkBox3.setChecked(false);
                        }
                    }
                });
                tableRow4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!checkBox4.isChecked()){
                            Constants.SECURITY_RATINGS.add("2 stars");
                            checkBox4.setChecked(true);
                        }else {
                            Constants.SECURITY_RATINGS.remove("2 stars");
                            checkBox4.setChecked(false);
                        }
                    }
                });
                tableRow5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!checkBox5.isChecked()){
                            Constants.SECURITY_RATINGS.add("1 stars");
                            checkBox5.setChecked(true);
                        }else {
                            Constants.SECURITY_RATINGS.remove("1 stars");
                            checkBox5.setChecked(false);
                        }
                    }
                });

                if (Constants.SECURITY_RATINGS.size()>0){
                    for (String checkedbox : Constants.SECURITY_RATINGS){
                        switch (checkedbox){
                            case "5 stars":
                                checkBox1.setChecked(true);
                                break;
                            case "4 stars":
                                checkBox2.setChecked(true);
                                break;
                            case "3 stars":
                                checkBox3.setChecked(true);
                                break;
                            case "2 stars":
                                checkBox4.setChecked(true);
                                break;
                            case "1 stars":
                                checkBox5.setChecked(true);
                                break;
                        }
                    }
                }

//
//                checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                        if (isChecked){
//                            Constants.SECURITY_RATINGS.add("5 stars");\
//                        }else {
//                            Constants.SECURITY_RATINGS.remove("5 stars");
//                        }
//                    }
//                });
//                checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                        if (isChecked){
//                            Constants.SECURITY_RATINGS.add("4 stars");
//                        }else {
//                            Constants.SECURITY_RATINGS.remove("4 stars");
//                        }
//                    }
//                });
//                checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                        if (isChecked){
//                            Constants.SECURITY_RATINGS.add("3 stars");
//                        }else {
//                            Constants.SECURITY_RATINGS.remove("3 stars");
//                        }
//                    }
//                });
//                checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                        if (isChecked){
//                            Constants.SECURITY_RATINGS.add("2 stars");
//                        }else {
//                            Constants.SECURITY_RATINGS.remove("2 stars");
//                        }
//                    }
//                });
//                checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                        if (isChecked){
//                            Constants.SECURITY_RATINGS.add("1 star");
//                        }else {
//                            Constants.SECURITY_RATINGS.remove("1 star");
//                        }
//                    }
//                });
                break;



            case 2 :
                convertView = layoutInflater.inflate(R.layout.list_item_car_category,null);

                RadioGroup rgp = (RadioGroup) convertView.findViewById(R.id.radio_group);

                RadioButton rab1 = convertView.findViewById(R.id.rb1);
                RadioButton rab2 = convertView.findViewById(R.id.rb2);
                RadioButton rab3 = convertView.findViewById(R.id.rb3);
                RadioButton rab4 = convertView.findViewById(R.id.rb4);
                RadioButton rab5 = convertView.findViewById(R.id.rb5);

                TableRow tablesRow1 = convertView.findViewById(R.id.table1);
                TableRow tablesRow2 = convertView.findViewById(R.id.table2);
                TableRow tablesRow3 = convertView.findViewById(R.id.table3);
                TableRow tablesRow4 = convertView.findViewById(R.id.table4);
                TableRow tablesRow5 = convertView.findViewById(R.id.table5);

                tablesRow1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!rab1.isChecked()){
                            Constants.CAR_CATEGORY.clear();
                            Constants.CAR_CATEGORY.add("Compact");
                            rab1.setChecked(true);
                            rab2.setChecked(false);
                            rab3.setChecked(false);
                            rab4.setChecked(false);
                            rab5.setChecked(false);
                        }else {
                            Constants.CAR_CATEGORY.remove("Compact");
                            rab1.setChecked(false);
                        }
                    }
                });

                tablesRow2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!rab1.isChecked()){
                            Constants.CAR_CATEGORY.clear();
                            Constants.CAR_CATEGORY.add("Small");
                            rab1.setChecked(false);
                            rab2.setChecked(true);
                            rab3.setChecked(false);
                            rab4.setChecked(false);
                            rab5.setChecked(false);
                        }else {
                            Constants.CAR_CATEGORY.remove("Small");
                            rab2.setChecked(false);
                        }
                    }
                });
                tablesRow3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!rab3.isChecked()){
                            Constants.CAR_CATEGORY.clear();
                            Constants.CAR_CATEGORY.add("Mid size");
                            rab1.setChecked(false);
                            rab2.setChecked(false);
                            rab3.setChecked(true);
                            rab4.setChecked(false);
                            rab5.setChecked(false);
                        }else {
                            Constants.CAR_CATEGORY.remove("Mid size");
                            rab3.setChecked(false);
                        }
                    }
                });
                tablesRow4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!rab4.isChecked()){
                            Constants.CAR_CATEGORY.clear();
                            Constants.CAR_CATEGORY.add("Full");
                            rab1.setChecked(false);
                            rab2.setChecked(false);
                            rab3.setChecked(false);
                            rab4.setChecked(true);
                            rab5.setChecked(false);
                        }else {
                            Constants.CAR_CATEGORY.remove("Full");
                            rab4.setChecked(false);
                        }
                    }
                });
                tablesRow5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!rab5.isChecked()){
                            Constants.CAR_CATEGORY.clear();
                            Constants.CAR_CATEGORY.add("Van/Pick-up");
                            rab1.setChecked(false);
                            rab2.setChecked(false);
                            rab3.setChecked(false);
                            rab4.setChecked(false);
                            rab5.setChecked(true);
                        }else {
                            Constants.CAR_CATEGORY.remove("Van/Pick-up");
                            rab5.setChecked(false);
                        }
                    }
                });
                if (Constants.CAR_CATEGORY.size() > 0){
                    String checkedItem = Constants.CAR_CATEGORY.get(0);

                    switch (checkedItem) {
                        case "Compact":
                            rab1.setChecked(true);
                            break;
                        case "Small":
                            rab2.setChecked(true);
                            break;
                        case "Mid size":
                            rab3.setChecked(true);
                            break;
                        case "Full":
                            rab4.setChecked(true);
                            break;
                        case "Van/Pick-up":
                            rab5.setChecked(true);
                            break;
                    }
                }

                rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        switch (checkedId){
                            case R.id.rb1:
                                Constants.CAR_CATEGORY.clear();
                                Constants.CAR_CATEGORY.add("Compact");
//                                Toast.makeText(context, rab1.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb2:
                                Constants.CAR_CATEGORY.clear();
                                Constants.CAR_CATEGORY.add("Small");
//                                Toast.makeText(context, rab2.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb3:
                                Constants.CAR_CATEGORY.clear();
                                Constants.CAR_CATEGORY.add("Mid size");
//                                Toast.makeText(context, rab3.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb4:
                                Constants.CAR_CATEGORY.clear();
                                Constants.CAR_CATEGORY.add("Full");
//                                Toast.makeText(context, rab4.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb5:
                                Constants.CAR_CATEGORY.clear();
                                Constants.CAR_CATEGORY.add("Van/Pick-up");
//                                Toast.makeText(context, rab5.getText(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

                break;

        }

        return convertView;
    }

    private int getRandomNumber(int min,int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void setData(){
        notifyDataSetChanged();
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
