package com.example.rozgaar.ChatBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.rozgaar.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class chatList extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView backBtn;
    DatabaseReference reference;
    ArrayList<OnScreenModel> list = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    chatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        backBtn = findViewById(R.id.back_from_chatList);
        recyclerView = findViewById(R.id.chatList_recyclerview);

        reference = FirebaseDatabase.getInstance().getReference();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit_chatList);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new chatListAdapter(this, list);
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (list != null) {
            reference.child("OnChatScreen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        progressBar.setVisibility(View.GONE);
                        for (DataSnapshot d : snapshot.getChildren()) {
                            OnScreenModel model = d.getValue(OnScreenModel.class);
                            if (Objects.equals(model.getRoomID(), uid)) {
                                list.add(model);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        reference.child("ChatListStatus").child(uid).child("active").setValue("false");
    }
}