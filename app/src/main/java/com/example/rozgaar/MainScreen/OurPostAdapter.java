package com.example.rozgaar.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rozgaar.PostManagement.postModel;
import com.example.rozgaar.R;

import java.util.ArrayList;

public class OurPostAdapter extends RecyclerView.Adapter<OurPostAdapter.MyViewHolder> {
    Context context;

    public OurPostAdapter(Context context, ArrayList<postModel> list) {
        this.context = context;
        this.list = list;
    }

    ArrayList<postModel> list;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.our_post_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getPageImage()).into(holder.postImage);
        if (list.get(position).getPost_title().length() > 15) {
            holder.title.setText(list.get(position).getPost_title().substring(0, 15 - 3) + "...");
        } else {
            holder.title.setText(list.get(position).getPost_title());
        }

        if (list.get(position).getPost_description().length() > 15) {
            holder.des.setText(list.get(position).getPost_description().substring(0, 15 - 3) + "...");
        } else {
            holder.des.setText(list.get(position).getPost_description());
        }

        if (list.get(position).getPost_location().length() > 15) {
            holder.location.setText(list.get(position).getPost_location().substring(0, 15 - 3) + "...");
        } else {
            holder.location.setText(list.get(position).getPost_location());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TapOnItem.class);
                intent.putExtra("TAP_UID", list.get(position).getUser_uid());
                intent.putExtra("TAP_FCM", list.get(position).getUserFcm());
                intent.putExtra("TAP_PROFILE_PIC", list.get(position).getProfile_uri());
                intent.putExtra("TAP_TITLE", list.get(position).getPost_title());
                intent.putExtra("TAP_DES", list.get(position).getPost_description());
                intent.putExtra("TAP_LOCATION", list.get(position).getPost_location());
                intent.putExtra("TAP_POSTKEY", list.get(position).getPushId());
                intent.putExtra("TAP_FRONT_PAGE", list.get(position).getPageImage());
                intent.putExtra("TAP_DATE", list.get(position).getPost_date());
                intent.putStringArrayListExtra("POST_LIST", (ArrayList<String>) list.get(position).getList());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView title, des, location;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.work_ms);
            title = itemView.findViewById(R.id.work_title_ms);
            des = itemView.findViewById(R.id.work_des_ms);
            location = itemView.findViewById(R.id.work_location_ms);
        }
    }
}
