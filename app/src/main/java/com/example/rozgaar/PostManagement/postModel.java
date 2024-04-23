package com.example.rozgaar.PostManagement;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class postModel {
    String user_uid, post_title, post_description, post_date,profile_uri,post_location,pushId,pageImage,userFcm;
    List<String>list;

    public String getPost_location() {
        return post_location;
    }

    public void setPost_location(String post_location) {
        this.post_location = post_location;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public String getPageImage() {
        return pageImage;
    }

    public String getUserFcm() {
        return userFcm;
    }

    public void setUserFcm(String userFcm) {
        this.userFcm = userFcm;
    }

    public void setPageImage(String pageImage) {
        this.pageImage = pageImage;
    }

    public postModel() {
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public postModel(String user_uid, String post_title, String post_description, String post_date, String profile_uri, List<String> list, String post_location, String pushId,String pageImage,String userFcm) {
        this.user_uid = user_uid;
        this.userFcm = userFcm;
        this.post_title = post_title;
        this.post_description = post_description;
        this.post_date = post_date;
        this.profile_uri = profile_uri;
        this.list = list;
        this.post_location = post_location;
        this.pushId = pushId;
        this.pageImage = pageImage;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getProfile_uri() {
        return profile_uri;
    }

    public void setProfile_uri(String profile_uri) {
        this.profile_uri = profile_uri;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
