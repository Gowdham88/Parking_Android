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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.polsec.pyrky.R;

import java.util.HashMap;
import java.util.List;

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

        LayoutInflater layoutInflater;
        RadioGroup radiogroup;
        View view = convertView;



        if (convertView == null) {


            switch (childType) {
                case CARCATEGORY:
                     layoutInflater = (LayoutInflater) this.context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.list_item, null);

//                   CheckBox checkBox1 = convertView.findViewById(R.id.checkbox);
//                    checkBox1.setText(expandedListText);
//                   TextView expandedListTextView = convertView
//                            .findViewById(R.id.expandedListItem);
//                    expandedListTextView.setText(expandedListText);

//                    radiogroup  = convertView.findViewById(R.id.radioGroup);
//                    radiogroup.setText(expandedListText);
//                   TextView expandedListTextView= convertView
//                            .findViewById(R.id.expandedListItem);
//                    expandedListTextView.setText(expandedListText);
//
//                    //inflate our layout and textview then setTag viewholder if the view is null
//
//
//                    expandedListText = (String) getChild(groupPosition, childPosition - 1);
//                    //inflate our layout and textview then setTag viewholder if the view is null
//                    view = LayoutInflater.from(context).inflate(R.layout.navdrawer_list_item, null);
//                    viewHolder.textView = (TextView) view.findViewById(R.id.expandedListItem);
//                    viewHolder.textView.setText(expandedListText);
                    break;
                case SECURITY:


                      layoutInflater = (LayoutInflater) this.context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.list_item_filter, null);

//                    CheckBox checkBox2 = convertView.findViewById(R.id.checkbox);
//                    checkBox2.setText(expandedListText);
//                    TextView expandedListTextView1 = convertView
//                            .findViewById(R.id.expandedListItem);
//                 expandedListTextView1.setText(expandedListText);
                    break;
                case FILTER:
                     layoutInflater = (LayoutInflater) this.context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.list_item, null);

                    CheckBox checkBox3 = convertView.findViewById(R.id.checkbox);
                    checkBox3.setText(expandedListText);
                   TextView expandedListTextView3 = convertView
                            .findViewById(R.id.expandedListItem);
                    expandedListTextView3.setText(expandedListText);

//                     radiogroup = convertView.findViewById(R.id.radioGroup);
//                    radiogroup.setT(expandedListText);
//                    expandedListTextView = convertView
//                            .findViewById(R.id.expandedListItem);
//                    expandedListTextView.setText(expandedListText);
                    break;

                default:

                    break;
            }
//             layoutInflater = (LayoutInflater) this.context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.list_item, null);
//            CheckBox checkBox = convertView.findViewById(R.id.checkbox);
//                    checkBox.setText(expandedListText);
//                     expandedListTextView = convertView
//                            .findViewById(R.id.expandedListItem);
//                    expandedListTextView.setText(expandedListText);
        }

        return convertView;
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