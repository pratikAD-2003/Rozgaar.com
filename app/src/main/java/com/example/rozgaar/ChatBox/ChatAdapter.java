package com.example.rozgaar.ChatBox;

import static com.example.rozgaar.ChatBox.chatModel.MESSAGE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.rozgaar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.logging.Handler;

public class ChatAdapter extends RecyclerView.Adapter {
    Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    public ChatAdapter(Context context, ArrayList<chatModel> list) {
        this.context = context;
        this.list = list;
    }

    ArrayList<chatModel> list;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(context).inflate(R.layout.message_layout, parent, false);
                return new MsgHolder(view);
            case 1:
                View view2 = LayoutInflater.from(context).inflate(R.layout.chat_audio_item, parent, false);
                return new AudioHolder(view2);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        chatModel model = list.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getUid())) {
            if (holder.getClass() == MsgHolder.class) {
                MsgHolder msgHolder = (MsgHolder) holder;
                msgHolder.senderMessage.setVisibility(View.VISIBLE);
                msgHolder.senderMTime.setVisibility(View.VISIBLE);
                msgHolder.senderMessage.setText(model.getMessage());
                msgHolder.senderMTime.setText(model.getTime());
            } else if (holder.getClass() == AudioHolder.class) {
                AudioHolder audioHolder = (AudioHolder) holder;
                audioHolder.sender.setVisibility(View.VISIBLE);
                audioHolder.senderTime.setVisibility(View.VISIBLE);
                audioHolder.senderTime.setText(model.getTime());
                audioHolder.senderPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AudioPlayer.class);
                        intent.putExtra("AudioUri", model.getAudioUri());
                        context.startActivity(intent);
                    }
                });
            }
        } else {
            if (holder.getClass() == MsgHolder.class) {
                MsgHolder msgHolder = (MsgHolder) holder;
                msgHolder.receiverMessage.setVisibility(View.VISIBLE);
                msgHolder.receiverMTime.setVisibility(View.VISIBLE);
                msgHolder.receiverMessage.setText(model.getMessage());
                msgHolder.receiverMTime.setText(model.getTime());
            } else if (holder.getClass() == AudioHolder.class) {
                AudioHolder audioHolder = (AudioHolder) holder;
                audioHolder.receiver.setVisibility(View.VISIBLE);
                audioHolder.receiverTime.setVisibility(View.VISIBLE);
                audioHolder.receiverTime.setText(model.getTime());
                audioHolder.receiverPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AudioPlayer.class);
                        intent.putExtra("AudioUri", model.getAudioUri());
                        context.startActivity(intent);
                    }
                });
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        chatModel model = list.get(position);
        switch (model.getType()) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AudioHolder extends RecyclerView.ViewHolder {
        ImageView senderPlay, receiverPlay;
        TextView senderTime, receiverTime;
        LinearLayout sender, receiver;

        public AudioHolder(@NonNull View itemView) {
            super(itemView);
            senderPlay = itemView.findViewById(R.id.sender_play_btn);
            receiverPlay = itemView.findViewById(R.id.receiver_play_btn);
            senderTime = itemView.findViewById(R.id.sender_time);
            receiverTime = itemView.findViewById(R.id.receiver_audio_time);
            sender = itemView.findViewById(R.id.sender_LL);
            receiver = itemView.findViewById(R.id.receiver_LL);
        }
    }

    public class MsgHolder extends RecyclerView.ViewHolder {
        TextView senderMessage, receiverMessage;
        TextView senderMTime, receiverMTime;

        public MsgHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.sender_msg);
            senderMTime = itemView.findViewById(R.id.sender_msg_time);
            receiverMessage = itemView.findViewById(R.id.receiver_msg);
            receiverMTime = itemView.findViewById(R.id.receiver_msg_time);
        }
    }
}
