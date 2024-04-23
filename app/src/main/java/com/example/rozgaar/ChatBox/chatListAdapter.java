package com.example.rozgaar.ChatBox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rozgaar.MainScreen.TapOnItem;
import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatListAdapter extends RecyclerView.Adapter<chatListAdapter.MyViewHolder> {
    Context context;
    ArrayList<OnScreenModel> list;
    DatabaseReference reference;
    FirebaseFirestore db;
    String oppositeFcm;

    public chatListAdapter(Context context, ArrayList<OnScreenModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        reference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        Glide.with(context).load(list.get(position).getProfileUri()).into(holder.circleImageView);
        if (list.get(position).getLastMsg().length() > 100) {
            holder.lastMsg.setText(list.get(position).getLastMsg().substring(0, 100 - 3) + "...");
        } else {
            holder.lastMsg.setText(list.get(position).getLastMsg());
        }

        if (list.get(position).getName().length() > 100) {
            holder.name.setText(list.get(position).getName().substring(0, 100 - 3) + "...");
        } else {
            holder.name.setText(list.get(position).getName());
        }

        reference.child("SeenStatus").child(list.get(position).getRoomID()).child("active").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String check = snapshot.getValue(String.class);
                    if (Objects.equals(check, "false")) {
                        holder.unread.setVisibility(View.VISIBLE);
                    } else {
                        holder.unread.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try {
            db.collection("Profile_Data").document(list.get(position).getSenderId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        oppositeFcm = documentSnapshot.getString("user_FCM");
                    }
                }
            });
        } catch (Exception e) {

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, chatScreen.class);
                intent1.putExtra("User_Uid", list.get(position).getSenderId());
                intent1.putExtra("User_fcm", oppositeFcm);
                context.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView, unread;
        TextView name, lastMsg;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.chatList_profile);
            name = itemView.findViewById(R.id.chatList_username);
            lastMsg = itemView.findViewById(R.id.chatList_last_msg);
            unread = itemView.findViewById(R.id.chatList_unread_msg_signal);
        }
    }
}
