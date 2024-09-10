package com.jadd.easykds;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    public TextView itemName,quantity;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.item_name);
        quantity = itemView.findViewById(R.id.item_quan);

    }
}
