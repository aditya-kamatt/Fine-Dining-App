package com.jadd.easyrestro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jadd.easyrestro.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

    public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder>{

    Context mConext;
    ArrayList<String> items;
    OnTableNumberListner onTableNumberListner;

    public MainRecyclerViewAdapter(Context mConext, ArrayList<String> items,OnTableNumberListner onTableNumberListner) {
        this.mConext = mConext;
        this.items = items;
        this.onTableNumberListner = onTableNumberListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button button;
        OnTableNumberListner onTableNumberListner;
        public MyViewHolder(@NonNull View itemView,OnTableNumberListner onTableNumberListner) {
            super(itemView);
            button = itemView.findViewById(R.id.table_number_main);
            this.onTableNumberListner = onTableNumberListner;
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTableNumberListner.onTableNumberClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mConext).inflate(R.layout.row_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view,onTableNumberListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.button.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnTableNumberListner{
        void onTableNumberClick(int position);
    }

}
