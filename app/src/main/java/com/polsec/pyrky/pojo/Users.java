package com.polsec.pyrky.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by thulirsoft on 7/3/18.
 */

public class Users implements Serializable {


    private String username;


    private String email;
    private String password;



    private String carCategory;
    private String profileImageURL;

    public Users() {

    }

    public Users(String username, String email,String profileImageUrl, String carCategory) {
        this.username = username;
        this.email = email;
        this.profileImageURL = profileImageUrl;
        this.carCategory = carCategory;

    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(String carCategory) {
        this.carCategory = carCategory;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }


}
