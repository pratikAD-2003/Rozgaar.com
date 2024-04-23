package com.example.rozgaar.MainScreen;

import static com.example.rozgaar.NotificationManagement.NotificationModel.NO_REQUEST;
import static com.example.rozgaar.NotificationManagement.NotificationModel.REQUEST_GRANTED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rozgaar.ChatBox.chatScreen;
import com.example.rozgaar.NotificationManagement.FcmNotificationsSender;
import com.example.rozgaar.NotificationManagement.NotificationModel;
import com.example.rozgaar.PostManagement.postAdapter;
import com.example.rozgaar.PostManagement.postModel;
import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TapOnItem extends AppCompatActivity {

    String profile, title, des, oppositeFcm, oppositeUid, location;
    RecyclerView recyclerView, recyclerView2;
    TextView titleText, desText, locationText, userName, showMore, postDate;
    CircleImageView profilePic;
    postAdapter adapter;
    List<Uri> temList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager, layoutManager2;
    FirebaseFirestore db;
    ImageView backBtn;
    String name, eduStatus, email, mobileNo, pushId;
    Button chat, contact;
    MainAdapter mainAdapter;
    ArrayList<postModel> list2 = new ArrayList<>();
    DatabaseReference reference;
    String userName_our, getNumberStatus_our, education_our, email_our, number_our, getFcm_our, postKey, postThumbnail, notiTitle, date;
    boolean isSend = false;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_on_item);
        recyclerView = findViewById(R.id.item_click_recyclerview);
        titleText = findViewById(R.id.item_click_title);
        desText = findViewById(R.id.item_click_des);
        locationText = findViewById(R.id.item_click_location);
        userName = findViewById(R.id.item_click_userName);
        profilePic = findViewById(R.id.item_click_user_profile);
        backBtn = findViewById(R.id.item_click_back_btn);
        contact = findViewById(R.id.item_click_contact_btn);
        chat = findViewById(R.id.item_click_chat_btn);
        recyclerView2 = findViewById(R.id.item_click_more_recyclerview);
        showMore = findViewById(R.id.show_more_posts);
        postDate = findViewById(R.id.item_click_date);

        db = FirebaseFirestore.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        List<String> list = intent.getStringArrayListExtra("POST_LIST");
        profile = getIntent().getStringExtra("TAP_PROFILE_PIC");
        title = getIntent().getStringExtra("TAP_TITLE");
        des = getIntent().getStringExtra("TAP_DES");
        oppositeFcm = getIntent().getStringExtra("TAP_FCM");
        oppositeUid = getIntent().getStringExtra("TAP_UID");
        location = getIntent().getStringExtra("TAP_LOCATION");
        postKey = getIntent().getStringExtra("TAP_POSTKEY");
        postThumbnail = getIntent().getStringExtra("TAP_FRONT_PAGE");
        date = getIntent().getStringExtra("TAP_DATE");

        postDate.setText(date);
        SharedPreferences sp = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();


        userName_our = sp.getString("MyName", "not");
        education_our = sp.getString("MyEducation", "not");
        email_our = sp.getString("MyEmail", "not");
        number_our = sp.getString("MyNumber", "not");
        getNumberStatus_our = sp.getString("getType", "false");
        getFcm_our = sp.getString("MyFcm", "not");


        try {
            db.collection("Profile_Data").document(oppositeUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            name = documentSnapshot.getString("name");
                            eduStatus = documentSnapshot.getString("edu_status");
                            email = documentSnapshot.getString("email");
                            mobileNo = documentSnapshot.getString("mobile_number");
//                            Log.d("DJIOSD", name + "\n" + eduStatus + "\n" + email + "\n" + mobileNo);
                            userName.setText(name);
                        }
                    }
                }
            });
        } catch (Exception e) {

        }


        for (int i = 0; i < list.size(); i++) {
            temList.add(Uri.parse(list.get(i)));
        }

        titleText.setText(title);
        desText.setText(des);
        locationText.setText(location);
        Glide.with(this).load(profile).into(profilePic);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TapOnItem.this, chatScreen.class);
                intent1.putExtra("User_Uid", oppositeUid);
                intent1.putExtra("User_fcm", oppositeFcm);
                startActivity(intent1);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSend) {
                    sendNotification(title);
                    isSend = true;
                    String pushId = reference.push().getKey();
                    NotificationModel model = new NotificationModel(REQUEST_GRANTED, postKey, title, postThumbnail, notiTitle, userName_our, getFcm_our, uid);
                    reference.child("Notification").child(oppositeUid).child(pushId).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                reference.child("NotificationStatus").child(oppositeUid).child("active").setValue("true");
                            }
                        }
                    });
//                    Toast.makeText(TapOnItem.this, "Your request has been sent!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(TapOnItem.this, getResources().getString(R.string.call_request_btn), Toast.LENGTH_SHORT).show();
                }
            }
        });


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new postAdapter(this, (ArrayList<Uri>) temList);
        recyclerView.setAdapter(adapter);

        layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);
        mainAdapter = new MainAdapter(this, list2);
        recyclerView2.setAdapter(mainAdapter);


        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
                db.collection("Users_work").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                postModel model = documentSnapshot.toObject(postModel.class);
                                list2.add(model);
                            }
                            mainAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    public void sendNotification(String title) {
//        notiTitle = " is sent a contact number request!";
        notiTitle = getResources().getString(R.string.call_request);
        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(oppositeFcm, userName_our + notiTitle, title, this, TapOnItem.this);
        notificationsSender.SendNotifications();
    }
}