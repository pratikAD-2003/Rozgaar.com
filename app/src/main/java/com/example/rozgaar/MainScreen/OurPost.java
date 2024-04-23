package com.example.rozgaar.MainScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rozgaar.LoginSignup.ProfileModel;
import com.example.rozgaar.PostManagement.postModel;
import com.example.rozgaar.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class OurPost extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView userName, postCount,notFound;
    RecyclerView recyclerView;
    ArrayList<postModel> list = new ArrayList<postModel>();
    OurPostAdapter adapter;
    FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String profileUri, username;
    ImageView backBtn;
    int count = 0;

    String getByUsername, getUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_post);
        circleImageView = findViewById(R.id.our_post_profilePic);
        userName = findViewById(R.id.our_post_username);
        postCount = findViewById(R.id.our_post_postCount);
        recyclerView = findViewById(R.id.our_post_recyclerview);
        backBtn = findViewById(R.id.our_post_backBtn);
        notFound = findViewById(R.id.our_post_text);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        adapter = new OurPostAdapter(this, list);
        recyclerView.setAdapter(adapter);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        db = FirebaseFirestore.getInstance();

        getByUsername = getIntent().getStringExtra("GET_USERNAME");

        db.collection("Profile_Data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        ProfileModel profileModel = documentSnapshot.toObject(ProfileModel.class);
                        if (profileModel.getUser_FCM().equals(getByUsername)) {
                            profileUri = documentSnapshot.getString("profileUri");
                            username = documentSnapshot.getString("name");
                            getUid = documentSnapshot.getString("user_uid");
                            userName.setText(username);
                            Glide.with(OurPost.this).load(profileUri).into(circleImageView);
                            db.collection("Users_work").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        notFound.setVisibility(View.GONE);
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            postModel model = documentSnapshot.toObject(postModel.class);
                                            if (model.getUser_uid().equals(getUid)) {
                                                list.add(model);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                        postCount.setText(String.valueOf(list.size()) + " posts");
                                    } else {
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }
                } else {

                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}