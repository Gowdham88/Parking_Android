package com.polsec.pyrky.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.polsec.pyrky.R;
import com.polsec.pyrky.utils.Constants;

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



    public ExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition))
                .size();
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
//    @Override
//    public int getChildType (int groupPosition, int childPosition) {
//        switch(childPosition) {
//            case 0:
//                return CARCATEGORY;
//            case 1:
//                return SECURITY;
//            default:
//                return FILTER;
//        }
//    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
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

        int childType = getChildType(groupPosition, childPosition);


        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        RadioGroup radiogroup;
        View view = convertView;



        switch (groupPosition){
            case 0 :
                convertView = layoutInflater.inflate(R.layout.list_item_parking_type,null);
                RadioGroup rgps = (RadioGroup) convertView.findViewById(R.id.radio_group);

                RadioButton rb1 = convertView.findViewById(R.id.rb1);
                RadioButton rb2 = convertView.findViewById(R.id.rb2);
                RadioButton rb3 = convertView.findViewById(R.id.rb3);

                rgps.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        switch (checkedId){
                            case R.id.rb1:
                                Constants.PARKING_TYPES.clear();
                                Constants.PARKING_TYPES.add(String.valueOf(rb1.getText()));
                                Toast.makeText(context, rb1.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb2:
                                Constants.PARKING_TYPES.clear();
                                Constants.PARKING_TYPES.add(String.valueOf(rb2.getText()));
                                Toast.makeText(context, rb2.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb3:
                                Constants.PARKING_TYPES.clear();
                                Constants.PARKING_TYPES.add(String.valueOf(rb3.getText()));
                                Toast.makeText(context, rb3.getText(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
//                RadioButton rbns = new RadioButton(context);
//                rbns.setText(expandedListText);
//                rbns.setId(1000+childPosition);
//                rgps.addView(rbns);

//                rgps.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//
//                        switch (checkedId){
//                            case 1000 :
//                                Constants.PARKING_TYPES.add("Free street parking");
//                                Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
//                                break;
//                            case 1001 :
//                                Constants.PARKING_TYPES.add("Paid street parking");
//                                Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
//                                break;
//                            case 1002 :
//                                Constants.PARKING_TYPES.add("Paid parking");
//                                Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//
//                    }
//                });

                break;


            case 1 :
                convertView = layoutInflater.inflate(R.layout.list_item_car_category,null);

                RadioGroup rgp = (RadioGroup) convertView.findViewById(R.id.radio_group);


                RadioButton rab1 = convertView.findViewById(R.id.rb1);
                RadioButton rab2 = convertView.findViewById(R.id.rb2);
                RadioButton rab3 = convertView.findViewById(R.id.rb3);
                RadioButton rab4 = convertView.findViewById(R.id.rb3);
                RadioButton rab5 = convertView.findViewById(R.id.rb3);

                rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        switch (checkedId){
                            case R.id.rb1:
                                Constants.CAR_CATEGORY.clear();
                                Constants.CAR_CATEGORY.add(String.valueOf(rab1.getText()));
                                Toast.makeText(context, rab1.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb2:
                                Constants.CAR_CATEGORY.clear();
                                Constants.CAR_CATEGORY.add(String.valueOf(rab2.getText()));
                                Toast.makeText(context, rab2.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb3:
                                Constants.CAR_CATEGORY.clear();
                                Constants.CAR_CATEGORY.add(String.valueOf(rab3.getText()));
                                Toast.makeText(context, rab3.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb4:
                                Constants.CAR_CATEGORY.clear();
                                Constants.CAR_CATEGORY.add(String.valueOf(rab4.getText()));
                                Toast.makeText(context, rab4.getText(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.rb5:
                                Constants.CAR_CATEGORY.clear();
                                Constants.CAR_CATEGORY.add(String.valueOf(rab5.getText()));
                                Toast.makeText(context, rab5.getText(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
//                    RadioButton rbn = new RadioButton(context);
//                    rbn.setText(expandedListText);
//                    rbn.setId(1000+childPosition);
//                    rgp.addView(rbn);
//
//                int id11 = context.getResources().getIdentifier(String.valueOf(1000), "id", "com.polsec.pyrky");
//                RadioButton rb11 = rgp.findViewById(id11);
//                int id22 = context.getResources().getIdentifier(String.valueOf(1001), "id", "com.polsec.pyrky");
//                RadioButton rb22 = rgp.findViewById(id22);
//                int id33 = context.getResources().getIdentifier(String.valueOf(1002), "id", "com.polsec.pyrky");
//                RadioButton rb33 = rgp.findViewById(id33);
//                int id44 = context.getResources().getIdentifier(String.valueOf(1003), "id", "com.polsec.pyrky");
//                RadioButton rb44 = rgp.findViewById(id44);
//                int id55 = context.getResources().getIdentifier(String.valueOf(1004), "id", "com.polsec.pyrky");
//                RadioButton rb55 = rgp.findViewById(id55);
//
//                rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//                        @Override
//                        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//
//                            switch (checkedId){
//                                case 1000 :
//                                    Constants.CAR_CATEGORY.add("Compact");
//
////                                    Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 1001 :
//                                    Constants.CAR_CATEGORY.add("Small");
////                                    Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 1002 :
//                                    Constants.CAR_CATEGORY.add("Mid size");
////                                    Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 1003 :
//                                    Constants.CAR_CATEGORY.add("Full");
////                                    Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
//                                    break;
//                                case 1004 :
//                                    Constants.CAR_CATEGORY.add("Van/Pick-up");
////                                    Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//
//                        }
//                    });

                break;

            case 2 :
                convertView = layoutInflater.inflate(R.layout.list_item_security_ratings, null);

                CheckBox checkBox = convertView.findViewById(R.id.checkbox);
                checkBox.setId(1000+childPosition);
                checkBox.setText(expandedListText);

                TextView  expandedListTextView = convertView
                        .findViewById(R.id.expandedListItem1);
                expandedListTextView.setText(expandedListText);


                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Toast.makeText(buttonView.getContext(), "Selected! - Question number: " + groupPosition + "answer: " + childPosition + "ischecked: " + isChecked, Toast.LENGTH_SHORT).show();

                        Constants.SECURITY_RATINGS.add((String.valueOf(childPosition+1))+" stars");

                    }
                });

                break;
        }

        //Previous success build code
       /* if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_security_ratings, null);
        }

        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        checkBox.setText(expandedListText);
        TextView  expandedListTextView = convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);*/

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
