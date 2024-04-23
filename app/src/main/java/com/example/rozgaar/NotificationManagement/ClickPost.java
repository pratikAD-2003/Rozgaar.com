package com.example.rozgaar.NotificationManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rozgaar.MainScreen.MainAdapter;
import com.example.rozgaar.PostManagement.postAdapter;
import com.example.rozgaar.PostManagement.postModel;
import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClickPost extends AppCompatActivity {

    TextView title, des, location, username, showMore;
    ImageView backBtn;
    RecyclerView recyclerView, recyclerView2;
    Button contact, chat;
    FirebaseFirestore db;
    DatabaseReference reference;
    String postKey;
    CircleImageView profilePic;
    String otherFcm, otherUid, otherUsername;
    List<String> list = new ArrayList<>();
    List<Uri> listPost = new ArrayList<>();

    ArrayList<postModel> modelArrayList = new ArrayList<>();

    postAdapter adapter;
    MainAdapter mainAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);
        title = findViewById(R.id.click_post_title);
        des = findViewById(R.id.click_post_des);
        location = findViewById(R.id.click_post_location);
        username = findViewById(R.id.click_post_username);
        showMore = findViewById(R.id.click_post_more_posts);
        backBtn = findViewById(R.id.click_post_back_btn);
        recyclerView = findViewById(R.id.click_post_recyclerview);
        recyclerView2 = findViewById(R.id.click_post_more_recyclerview);
        contact = findViewById(R.id.click_post_contact_btn);
        chat = findViewById(R.id.click_post_chat_btn);
        profilePic = findViewById(R.id.click_post_userProfile);

        db = FirebaseFirestore.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        postKey = getIntent().getStringExtra("CLICK_POST_POSTKEY");
        otherUsername = getIntent().getStringExtra("CLICK_POST_USERNAME");

//        username.setText(otherUsername);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db.collection("Users_work").document(postKey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        postModel model = documentSnapshot.toObject(postModel.class);
                        Glide.with(ClickPost.this).load(model.getProfile_uri()).into(profilePic);
                        title.setText(model.getPost_title());
                        des.setText(model.getPost_description());
                        location.setText(model.getPost_location());
                        otherFcm = model.getUserFcm();
                        otherUid = model.getUser_uid();
                        if (model.getList() != null) {
                            ArrayList<String> arrayList = (ArrayList<String>) model.getList();
                            for (String item : arrayList) {
                                listPost.add(Uri.parse(item));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        db.collection("Profile_Data").document(otherUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                        username.setText(documentSnapshot.getString("name"));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new postAdapter(this, (ArrayList<Uri>) listPost);
        recyclerView.setAdapter(adapter);

        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setHasFixedSize(true);
        mainAdapter = new MainAdapter(this, modelArrayList);
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
                                modelArrayList.add(model);
                            }
                            mainAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }
}