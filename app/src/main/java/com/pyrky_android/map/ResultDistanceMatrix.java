package com.pyrky_android.map;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultDistanceMatrix {

    @SerializedName("status")
    public String status;

    @SerializedName("rows")
    public List rows;

    public class InfoDistanceMatrix {
        @SerializedName("elements")
        public List elements;

        public class DistanceElement {
            @SerializedName("status")
            public String status;
            @SerializedName("duration")
            public ValueItem duration;
            @SerializedName("distance")
            public ValueItem distance;


        }

        public class ValueItem {
            @SerializedName("value")
            public long value;
            @SerializedName("text")
            public String text;

        }
    }
}
