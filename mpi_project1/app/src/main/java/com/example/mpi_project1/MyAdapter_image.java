package com.example.mpi_project1;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class MyAdapter_image extends RecyclerView.Adapter<MyAdapter_image.ViewHolder> {
    List<String> list;
    Context context;
    // Declare necessary variables and data source here
    public MyAdapter_image(List<String> list){
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to views here
        String image=this.list.get(position);
        Glide.with(holder.itemView.getContext()).load(image).into(holder.img);
        //holder.img.setImageURI(image);

    }
    @Override
    public int getItemCount() {
        // Return the number of items in the data source
        return this.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        // Declare your views here

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.product_image);
            // Initialize your views here
        }
    }
}

