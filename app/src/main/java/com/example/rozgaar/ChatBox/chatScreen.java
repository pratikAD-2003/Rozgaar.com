package com.example.rozgaar.ChatBox;

import static com.example.rozgaar.ChatBox.chatModel.AUDIO_MSG;
import static com.example.rozgaar.ChatBox.chatModel.MESSAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.rozgaar.MainScreen.OurPost;
import com.example.rozgaar.MainScreen.TapOnItem;
import com.example.rozgaar.NotificationManagement.FcmNotificationsSender;
import com.example.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatScreen extends AppCompatActivity {

    ImageView backBtn, audioBtn, messageSent;
    CircleImageView profile;
    TextView username, userStatus, recordingText;
    RecyclerView recyclerView;
    EditText message;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String ourUid = user.getUid();
    DatabaseReference reference;
    FirebaseFirestore db;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("ChatAudio");
    String senderRoomId, receiverRoomId, oppositeUid, profileUri, name, myProfile, myName, pushId = "", oppositeFcm;
    ChatAdapter adapter;
    ArrayList<chatModel> list = new ArrayList<>();
    LinearLayoutManager manager;
    boolean isRecording = false;
    int seconds = 0;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler;
    MediaRecorder mediaRecorder;
    LottieAnimationView recording_anim;

    @Override
    protected void onStart() {
        super.onStart();
        reference.child("SeenStatus").child(ourUid).child("active").setValue("true");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        backBtn = findViewById(R.id.back_from_chatScreen);
        profile = findViewById(R.id.chatScreen_profilePic);
        username = findViewById(R.id.chatScreen_username);
        userStatus = findViewById(R.id.chatScreen_userStatus);
        recyclerView = findViewById(R.id.chatScreen_recyclerview);
        message = findViewById(R.id.chatScreen_msg_box);
        audioBtn = findViewById(R.id.chatScreen_audioBtn);
        messageSent = findViewById(R.id.chatScreen_msg_sentBtn);
        recordingText = findViewById(R.id.audio_recording_text);
        recording_anim = findViewById(R.id.recording_anim);

        reference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        SharedPreferences sp = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        myProfile = sp.getString("MyProfileUri", "not");
        myName = sp.getString("MyName", "not");

        Date d = new Date();

        oppositeUid = getIntent().getStringExtra("User_Uid");
        oppositeFcm = getIntent().getStringExtra("User_fcm");
        db.collection("Profile_Data").document(oppositeUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    profileUri = documentSnapshot.getString("profileUri");
                    name = documentSnapshot.getString("name");
                    username.setText(name);
                    Glide.with(chatScreen.this).load(profileUri).into(profile);
                }
            }
        });

        reference.child("OnAppStatus").child(oppositeUid).child("active").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String check = snapshot.getValue(String.class);
                    if (Objects.equals(check, "true")) {
                        userStatus.setText("online");
                    } else {
                        userStatus.setText("offline");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        senderRoomId = oppositeUid + ourUid;
        receiverRoomId = ourUid + oppositeUid;

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        manager = new LinearLayoutManager(this);
        manager.setReverseLayout(false);
        manager.setStackFromEnd(true);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new ChatAdapter(this, list);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    scrollToBottom();
                }
            }
        });

        if (list != null) {
            reference.child("Chats").child(senderRoomId).child("Message").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            chatModel model = d.getValue(chatModel.class);
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        messageSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String times = d.getHours() + ":" + d.getMinutes();
                String msg = message.getText().toString();
                pushId = reference.push().getKey();
                chatModel model = new chatModel(MESSAGE, msg, times, pushId, ourUid);
                reference.child("Chats").child(senderRoomId).child("Message").child(pushId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pushId = reference.push().getKey();
                        chatModel model = new chatModel(MESSAGE, msg, times, pushId, ourUid);
                        reference.child("Chats").child(receiverRoomId).child("Message").child(pushId).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                OnScreenModel model1 = new OnScreenModel(oppositeUid, myName, myProfile, msg, ourUid);
                                reference.child("OnChatScreen").child(senderRoomId).setValue(model1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        OnScreenModel model2 = new OnScreenModel(ourUid, name, profileUri, msg, oppositeUid);
                                        reference.child("OnChatScreen").child(receiverRoomId).setValue(model2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                reference.child("ChatListStatus").child(oppositeUid).child("active").setValue("true");
                                                reference.child("SeenStatus").child(oppositeUid).child("active").setValue("false");
                                                sendNotification(oppositeFcm, myName, msg);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
                message.setText("");
            }
        });

        audioBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // start recording
                    recordingText.setVisibility(View.VISIBLE);
                    recording_anim.setVisibility(View.VISIBLE);
                    startRecordingAudio();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    // stop recording
                    recordingText.setVisibility(View.GONE);
                    recording_anim.setVisibility(View.GONE);
                    stopRecordingAudio();
                }
                return true;
            }
        });
    }


    private void scrollToBottom() {
        manager.smoothScrollToPosition(recyclerView, null, adapter.getItemCount());
    }

    private void startRecordingAudio() {
        isRecording = true;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setOutputFile(getRecordFilePath());
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.prepare();
                    Toast.makeText(chatScreen.this, "Recording Started!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaRecorder.start();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        seconds = 0;
                        runTimer();
                    }
                });
            }
        });
    }

    private void stopRecordingAudio() {
        audioSent();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        isRecording = false;
        Toast.makeText(chatScreen.this, "Stopped", Toast.LENGTH_SHORT).show();
    }

    public void runTimer() {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                recordingText.setText(time);
                if (isRecording) {
                    seconds++;
                }
                if (!isRecording) {
                    seconds = 0;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private String getRecordFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.getExternalStorageState());
        File file = new File(musicDirectory, "/audioRecorder" + "." + "mp3");
        return file.getPath();
    }

    public void audioSent() {
        senderRoomId = oppositeUid + ourUid;
        receiverRoomId = ourUid + oppositeUid;
        Date d = new Date();
        String times = d.getHours() + ":" + d.getMinutes();
        Uri uri = Uri.fromFile(new File(getRecordFilePath()));
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + ".mp3");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri myUri = uri;
                        pushId = reference.push().getKey();
                        chatModel model = new chatModel(AUDIO_MSG, times, ourUid, String.valueOf(myUri));
                        reference.child("Chats").child(senderRoomId).child("Message").child(pushId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                pushId = reference.push().getKey();
                                chatModel model = new chatModel(AUDIO_MSG, times, ourUid, String.valueOf(myUri));
                                reference.child("Chats").child(receiverRoomId).child("Message").child(pushId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        OnScreenModel model1 = new OnScreenModel(oppositeUid, myName, myProfile, myName + " sent a audio.", ourUid);
                                        reference.child("OnChatScreen").child(senderRoomId).setValue(model1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                OnScreenModel model2 = new OnScreenModel(ourUid, name, profileUri, name + " sent a audio.", oppositeUid);
                                                reference.child("OnChatScreen").child(receiverRoomId).setValue(model2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        reference.child("ChatListStatus").child(oppositeUid).child("active").setValue("true");
                                                        reference.child("SeenStatus").child(oppositeUid).child("active").setValue("false");
                                                        sendNotification(oppositeFcm, myName, myName + " sent a audio.");
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public void sendNotification(String fcm, String title, String body) {
        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(fcm, title, body, this, chatScreen.this);
        notificationsSender.SendNotifications();
    }
}