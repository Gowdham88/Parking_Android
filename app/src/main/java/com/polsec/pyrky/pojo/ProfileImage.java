package com.polsec.pyrky.pojo;

/**
 * Created by thulirsoft on 7/17/18.
 */

public class ProfileImage {
    public String profileImage;
    public String email;

    public ProfileImage(String profileImage, String email) {
        this.profileImage = profileImage;
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
