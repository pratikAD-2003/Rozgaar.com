package com.example.rozgaar.NotificationManagement;

import static android.content.Context.MODE_PRIVATE;
import static com.example.rozgaar.NotificationManagement.NotificationModel.NO_REQUEST;
import static com.example.rozgaar.NotificationManagement.NotificationModel.REQUEST_GRANTED;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rozgaar.LoginSignup.ProfileModel;
import com.example.rozgaar.MainScreen.OurPost;
import com.example.rozgaar.MainScreen.TapOnItem;
import com.example.rozgaar.PostManagement.postModel;
import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter {
    Context context;
    Activity activity;
    DatabaseReference reference;
    FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String postKey, pushID, userID, myName;
    boolean isCheck = false;
    boolean reJe = false;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    ArrayList<NotificationModel> list = new ArrayList<>();


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View one = LayoutInflater.from(context).inflate(R.layout.receiver_noti_item, parent, false);
                return new MyViewHolder(one);
            case 1:
                View two = LayoutInflater.from(context).inflate(R.layout.noti_item, parent, false);
                return new GrantedViewHolder(two);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        if (holder.getClass() == MyViewHolder.class) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.username.setText(list.get(position).getUsername());
            viewHolder.title.setText(list.get(position).getPostTitle());
            viewHolder.body.setText(list.get(position).getPostDes());
            Glide.with(context).load(list.get(position).getPostPic()).into(viewHolder.postPic);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClickPost.class);
                    intent.putExtra("CLICK_POST_POSTKEY", list.get(position).getPostKey());
                    intent.putExtra("CLICK_POST_USERNAME", list.get(position).getUsername());
                    context.startActivity(intent);
                }
            });

            viewHolder.username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OurPost.class);
//                    intent.putExtra("GET_USERNAME", list.get(position).getUsername());
                    intent.putExtra("GET_USERNAME", list.get(position).getMyUid());
                    intent.putExtra("GET_USERNAME", list.get(position).getFcm());
                    context.startActivity(intent);
                }
            });

        } else if (holder.getClass() == GrantedViewHolder.class) {
            GrantedViewHolder viewHolder = (GrantedViewHolder) holder;
            viewHolder.userName.setText(list.get(position).getUsername());
            viewHolder.notificationTitle.setText(list.get(position).getPostDes());
            viewHolder.postTitle.setText(list.get(position).getPostTitle());
            Glide.with(context).load(list.get(position).getPostPic()).into(viewHolder.postPic);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClickPost.class);
                    intent.putExtra("CLICK_POST_POSTKEY", list.get(position).getPostKey());
                    intent.putExtra("CLICK_POST_USERNAME", list.get(position).getUsername());
                    context.startActivity(intent);
                }
            });

            viewHolder.userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OurPost.class);
//                    intent.putExtra("GET_USERNAME", list.get(position).getUsername());
                    intent.putExtra("GET_USERNAME", list.get(position).getFcm());
                    context.startActivity(intent);
                }
            });

            viewHolder.sendNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Are your sure ?");
                    builder.setTitle(context.getResources().getString(R.string.sure));
//                    builder.setMessage("Your want to share your personal contact details.");
                    builder.setMessage(context.getResources().getString(R.string.share_msg));
                    builder.setPositiveButton(context.getResources().getString(R.string.share_btn), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!isCheck) {
                                SharedPreferences sp = context.getSharedPreferences("USER_DATA", MODE_PRIVATE);
                                String userName = sp.getString("MyName", "XXXX");
                                String userNumber = sp.getString("MyNumber", "XXXXXX");
                                db.collection("Profile_Data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                ProfileModel model = documentSnapshot.toObject(ProfileModel.class);
                                                if (model.getUser_FCM().equals(list.get(position).getFcm())) {
                                                    userID = documentSnapshot.getString("user_uid");
                                                    myName = documentSnapshot.getString("name");
                                                    sendNotification(list.get(position).getFcm(), context.getResources().getString(R.string.call_request_granted), context.getResources().getString(R.string.see_now));
                                                    String pushId = reference.push().getKey();
                                                    NotificationModel model2 = new NotificationModel(NO_REQUEST, list.get(position).getPostKey(), context.getResources().getString(R.string.call_request_granted), list.get(position).getPostPic(), userName + "'s contact number - " + userNumber + "\n" + "For this post - " + list.get(position).getPostTitle(), myName, list.get(position).getFcm(),uid);
                                                    reference.child("Notification").child(userID).child(pushId).setValue(model2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                reference.child("NotificationStatus").child(userID).child("active").setValue("true");
                                                            }
                                                        }
                                                    });
//                                                    Toast.makeText(context, "Your number has been shared.", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(context, context.getResources().getString(R.string.granted_call_toast), Toast.LENGTH_SHORT).show();
                                                    isCheck = true;
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }).setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

                }
            });

            viewHolder.rejectRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!reJe) {
                        SharedPreferences sp = context.getSharedPreferences("USER_DATA", MODE_PRIVATE);
                        String userName = sp.getString("MyName", "XXXX");
                        String userNumber = sp.getString("MyNumber", "XXXXXX");
                        db.collection("Profile_Data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        ProfileModel model = documentSnapshot.toObject(ProfileModel.class);
                                        if (model.getUser_FCM().equals(list.get(position).getFcm())) {
                                            userID = documentSnapshot.getString("user_uid");
                                            myName = documentSnapshot.getString("name");
                                            sendNotification(list.get(position).getFcm(), context.getResources().getString(R.string.call_request_denied), context.getResources().getString(R.string.see_now));
                                            String pushId = reference.push().getKey();
                                            NotificationModel model2 = new NotificationModel(NO_REQUEST, list.get(position).getPostKey(), context.getResources().getString(R.string.call_request_denied), list.get(position).getPostPic(), userName + " don't want share his contact number. " + "\n" + "For this post - " + list.get(position).getPostTitle(), myName, list.get(position).getFcm(),uid);
                                            reference.child("Notification").child(userID).child(pushId).setValue(model2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        reference.child("NotificationStatus").child(userID).child("active").setValue("true");
                                                    }
                                                }
                                            });
//                                            Toast.makeText(context, "Your rejection has been sent..", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(context, context.getResources().getString(R.string.denied_call_toast), Toast.LENGTH_SHORT).show();
                                            reJe = true;
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        NotificationModel model = list.get(position);
        switch (model.getType()) {
            case 0:
                return NO_REQUEST;
            case 1:
                return REQUEST_GRANTED;
            default:
                return -1;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView postPic;
        TextView username, title, body;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postPic = itemView.findViewById(R.id.receiver_noti_pic);
            username = itemView.findViewById(R.id.receiver_noti_username);
            title = itemView.findViewById(R.id.receiver_noti_title);
            body = itemView.findViewById(R.id.receiver_noti_body);
        }
    }

    public class GrantedViewHolder extends RecyclerView.ViewHolder {
        ImageView postPic;
        TextView postTitle, notificationTitle, userName;
        Button sendNumber, rejectRequest;

        public GrantedViewHolder(@NonNull View itemView) {
            super(itemView);
            postPic = itemView.findViewById(R.id.noti_item_pic);
            postTitle = itemView.findViewById(R.id.noti_item_post_title);
            notificationTitle = itemView.findViewById(R.id.noti_item_notiTitle);
            userName = itemView.findViewById(R.id.noti_item_Username);
            sendNumber = itemView.findViewById(R.id.noti_send_number);
            rejectRequest = itemView.findViewById(R.id.noti_reject_request);
        }
    }

    public void sendNotification(String fcm, String title, String body) {
        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(fcm, title, body, context, activity);
        notificationsSender.SendNotifications();
    }
}
