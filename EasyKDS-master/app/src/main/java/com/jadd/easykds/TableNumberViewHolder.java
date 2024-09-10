package com.jadd.easykds;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class TableNumberViewHolder extends RecyclerView.ViewHolder {
    public RecyclerView orderView;
    public LinearLayoutManager layoutManager;
    public TextView tableNumberView;
    public TableNumberViewHolder(@NonNull View itemView) {
        super(itemView);
        layoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.VERTICAL,false);
        orderView = itemView.findViewById(R.id.order_recycler_view);
        tableNumberView = itemView.findViewById(R.id.table_number_view);
        orderView.setLayoutManager(layoutManager);
    }
}
