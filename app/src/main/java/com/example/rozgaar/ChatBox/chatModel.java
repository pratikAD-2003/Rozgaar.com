package com.example.rozgaar.ChatBox;

public class chatModel {
    final public static  int MESSAGE = 0;
    final public static  int AUDIO_MSG = 1;
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    String message,time,pushId,uid,audioUri;

    public chatModel(int type, String time, String uid, String audioUri) {
        this.type = type;
        this.time = time;
        this.uid = uid;
        this.audioUri = audioUri;
    }

    public String getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(String audioUri) {
        this.audioUri = audioUri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public chatModel() {
    }

    public chatModel(int type,String message, String time, String pushId,String uid) {
        this.type = type;
        this.message = message;
        this.time = time;
        this.pushId = pushId;
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
