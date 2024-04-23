package com.example.rozgaar.PostManagement;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rozgaar.R;

import java.util.ArrayList;

public class postAdapter extends RecyclerView.Adapter<postAdapter.MyViewHolder> {
    Context context;
    ArrayList<Uri> list = new ArrayList<>();

    public postAdapter(Context context, ArrayList<Uri> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_items_images_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position)).into(holder.imageView);
        holder.count.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView count;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.selected_images_item);
            count = itemView.findViewById(R.id.selected_images_count);
        }
    }
}
