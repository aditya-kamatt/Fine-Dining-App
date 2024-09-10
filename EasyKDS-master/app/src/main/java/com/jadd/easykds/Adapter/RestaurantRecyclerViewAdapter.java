package com.jadd.easykds.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jadd.easykds.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.MyViewHolder>{

    Context mContext;
    ArrayList<String> restaurantNames;
    OnRestaurantClickListener onRestaurantClickListener;

    public RestaurantRecyclerViewAdapter(Context mContext, ArrayList<String> restaurantNames, OnRestaurantClickListener onRestaurantClickListener) {
        this.mContext = mContext;
        this.restaurantNames = restaurantNames;
        this.onRestaurantClickListener = onRestaurantClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_name,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view,onRestaurantClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(restaurantNames.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            OnRestaurantClickListener onRestaurantClickLister;
        TextView textView;
        public MyViewHolder(@NonNull View itemView, RestaurantRecyclerViewAdapter.OnRestaurantClickListener onRestaurantClickListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.restaurant_each_name);
            this.onRestaurantClickLister = onRestaurantClickListener;
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRestaurantClickListener.onRestaurantClick(getAdapterPosition());
        }
    }

    public interface OnRestaurantClickListener{
        void onRestaurantClick(int position);
    }

}
