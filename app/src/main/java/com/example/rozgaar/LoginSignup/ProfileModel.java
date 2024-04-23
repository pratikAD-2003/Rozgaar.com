package com.example.rozgaar.LoginSignup;

public class ProfileModel {
    String profileUri, name, edu_status, email, mobile_number,user_FCM,user_uid;

    public String getProfileUri() {
        return profileUri;
    }

    public ProfileModel() {
    }

    public ProfileModel(String profileUri, String name, String edu_status, String email, String mobile_number, String user_FCM, String user_uid) {
        this.profileUri = profileUri;
        this.name = name;
        this.edu_status = edu_status;
        this.email = email;
        this.mobile_number = mobile_number;
        this.user_FCM = user_FCM;
        this.user_uid = user_uid;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEdu_status() {
        return edu_status;
    }

    public void setEdu_status(String edu_status) {
        this.edu_status = edu_status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getUser_FCM() {
        return user_FCM;
    }

    public void setUser_FCM(String user_FCM) {
        this.user_FCM = user_FCM;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}
