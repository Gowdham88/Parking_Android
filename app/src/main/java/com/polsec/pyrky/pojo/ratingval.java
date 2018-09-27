package com.polsec.pyrky.pojo;

/**
 * Created by czsm4 on 29/08/18.
 */

public class ratingval {
    private String User_ID;

    private String ratings;

    public ratingval() {

    }

    public ratingval(String User_ID, String ratings) {
        this.User_ID = User_ID;
        this.ratings = ratings;

    }


    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }


}

