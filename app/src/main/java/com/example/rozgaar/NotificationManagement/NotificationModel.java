package com.example.rozgaar.NotificationManagement;

public class NotificationModel {

    public static final String NOTI_SENDER_TOKEN = "AAAABK9CIKQ:APA91bFvubG_Qb68HDaProXr_S-ZNjSdajNPcULPTjJZh-_0MK4OTPLvmJz7Po1pPI6PzojArNMt2TCSTEYatJr7d6ldPvlgQQsdvx2xkOphREBJaAXl2w4jNLULVp0VkOnjjVjj1_Ra";
    public NotificationModel() {
    }

    public NotificationModel(int type, String postKey, String postTitle, String postPic, String postDes, String username, String fcm,String myUid) {
        this.type = type;
        this.postKey = postKey;
        this.postTitle = postTitle;
        this.postPic = postPic;
        this.postDes = postDes;
        this.username = username;
        this.fcm = fcm;
        this.myUid  = myUid;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getMyUid() {
        return myUid;
    }

    public void setMyUid(String myUid) {
        this.myUid = myUid;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostPic() {
        return postPic;
    }

    public void setPostPic(String postPic) {
        this.postPic = postPic;
    }

    public String getPostDes() {
        return postDes;
    }

    public void setPostDes(String postDes) {
        this.postDes = postDes;
    }

    public String getFcm() {
        return fcm;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    final public static int NO_REQUEST = 0;
    final public static int REQUEST_GRANTED = 1;
    int type;
    String postKey, postTitle, postPic, postDes, username, fcm,myUid;
}
