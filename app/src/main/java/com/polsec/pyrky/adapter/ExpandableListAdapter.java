package com.polsec.pyrky.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
                convertView = layoutInflater.inflate(R.layout.list_item_filter,null);
                RadioGroup rgroupFilter = (RadioGroup) convertView.findViewById(R.id.radio_group);

                        RadioButton rbn = new RadioButton(context);

//                        rbn.setId(1000 + i);
                        rbn.setText(expandedListText);
                        rgroupFilter.addView(rbn);
                        buttonId++;
//                    }
//                }

                //Now lets say we have fixed childViews as Free street parking, Paid street Parking, Paid parking
                //with positions 0,1,2
                switch (childPosition){
                    case 0 :
//                        Toast.makeText(context, "Free street parking", Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
//                        Toast.makeText(context, "Paid street parking", Toast.LENGTH_SHORT).show();
                        break;
                    case 2 :
//                        Toast.makeText(context, "Paid parking", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case 1 :
                convertView = layoutInflater.inflate(R.layout.list_item_filter,null);
                RadioGroup rgps = (RadioGroup) convertView.findViewById(R.id.radio_group);

                    RadioButton rbns = new RadioButton(context);
                    rbns.setText(expandedListText);
                    rbns.setId(1000+childPosition);
                    rgps.addView(rbns);

                int id1 = context.getResources().getIdentifier(String.valueOf(1000), "id", "com.polsec.pyrky");
                RadioButton rb1 = rgps.findViewById(id1);
                int id2 = context.getResources().getIdentifier(String.valueOf(1001), "id", "com.polsec.pyrky");
                RadioButton rb2 = rgps.findViewById(id2);
                int id3 = context.getResources().getIdentifier(String.valueOf(1002), "id", "com.polsec.pyrky");
                RadioButton rb3 = rgps.findViewById(id3);
                int id4 = context.getResources().getIdentifier(String.valueOf(1003), "id", "com.polsec.pyrky");
                RadioButton rb4 = rgps.findViewById(id4);
                int id5 = context.getResources().getIdentifier(String.valueOf(1004), "id", "com.polsec.pyrky");
                RadioButton rb5 = rgps.findViewById(id5);

                rgps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id1 = context.getResources().getIdentifier(String.valueOf(1000), "id", "com.polsec.pyrky");
                        RadioButton rb1 = view.findViewById(id1);
                        rb1.setChecked(true);
                        int id2 = context.getResources().getIdentifier(String.valueOf(1001), "id", "com.polsec.pyrky");
                        RadioButton rb2 = view.findViewById(id2);
                        rb2.setChecked(false);
                    }
                });

                rgps.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                            switch (checkedId){
                                case 1000 :

                                    rb1.setChecked(true);
                                    int id2 = context.getResources().getIdentifier(String.valueOf(1001), "id", "com.polsec.pyrky");
                                    RadioButton rb2 = rgps.findViewById(id2);
                                    rb2.setChecked(false);
                                    int id3 = context.getResources().getIdentifier(String.valueOf(1002), "id", "com.polsec.pyrky");
                                    RadioButton rb3 = rgps.findViewById(id3);
                                    rb3.setChecked(false);
                                    int id4 = context.getResources().getIdentifier(String.valueOf(1003), "id", "com.polsec.pyrky");
                                    RadioButton rb4 = rgps.findViewById(id4);
                                    rb4.setChecked(false);
                                    int id5 = context.getResources().getIdentifier(String.valueOf(1004), "id", "com.polsec.pyrky");
                                    RadioButton rb5 = rgps.findViewById(id5);
                                    rb5.setChecked(false);

                        Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1001 :
                        Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1002 :
                        Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1003 :
                        Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1004 :
                        Toast.makeText(context, checkedId+"", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        }
                    });

                break;

            case 2 :
                convertView = layoutInflater.inflate(R.layout.list_item, null);

                CheckBox checkBox = convertView.findViewById(R.id.checkbox);
                checkBox.setId(1000+childPosition);
                checkBox.setText(expandedListText);

                TextView  expandedListTextView = convertView
                        .findViewById(R.id.expandedListItem1);
                expandedListTextView.setText(expandedListText);


                switch (childPosition){
                    case 0 :
//                        Toast.makeText(context, "5 Star", Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
//                        Toast.makeText(context, "4 Star", Toast.LENGTH_SHORT).show();
                        break;
                    case 2 :
//                        Toast.makeText(context, "3 Star", Toast.LENGTH_SHORT).show();
                        break;
                    case 3 :
//                        Toast.makeText(context, "2 Star", Toast.LENGTH_SHORT).show();
                        break;
                    case 4 :
//                        Toast.makeText(context, "1 Star", Toast.LENGTH_SHORT).show();
                        break;
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Toast.makeText(buttonView.getContext(), "Selected! - Question number: " + groupPosition + "answer: " + childPosition + "ischecked: " + isChecked, Toast.LENGTH_SHORT).show();
//                        Constants.SEARCH_ARRAY.clear();
                        Constants.SEARCH_ARRAY.add((String.valueOf(childPosition+1))+" stars");

                    }
                });

                break;
        }

        //Previous success build code
       /* if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
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
        return false;
    }

    public void setData(){
        notifyDataSetChanged();
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}