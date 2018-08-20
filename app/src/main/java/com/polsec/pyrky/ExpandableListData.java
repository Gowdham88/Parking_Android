package com.polsec.pyrky;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thulirsoft on 7/4/18.
 */

public class ExpandableListData {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> filters = new ArrayList<String>();
        filters.add("Free street parking");
        filters.add("Paid street parking");
        filters.add("Paid parking");

        List<String> securityRatigs = new ArrayList<String>();
        securityRatigs.add("5 Star");
        securityRatigs.add("4 Star");
        securityRatigs.add("3 Star");
        securityRatigs.add("2 Star");
        securityRatigs.add("1 Star");

        List<String> carCategory = new ArrayList<String>();
        carCategory.add("Compact");
        carCategory.add("Small");
        carCategory.add("Mid size");
        carCategory.add("Full");
        carCategory.add("Van/Pick-up");


        expandableListDetail.put("Car Category", carCategory);
        expandableListDetail.put("Security Ratings", securityRatigs);
        expandableListDetail.put("Filter", filters);



        return expandableListDetail;
    }

}
