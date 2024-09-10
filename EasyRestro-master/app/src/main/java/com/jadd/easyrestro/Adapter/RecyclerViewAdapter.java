package com.jadd.easyrestro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.classes.Cart;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context mConext;
    ArrayList<Cart> items;
    //String category;
    OnQuanListner onQuanListner;

    public RecyclerViewAdapter(Context mConext, ArrayList<Cart> items, OnQuanListner onQuanListner) {
        this.mConext = mConext;
        this.items = items;
        //this.category = category;
        this.onQuanListner = onQuanListner;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mConext).inflate(R.layout.cart_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,onQuanListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = items.get(position).getItem().getName();
        holder.itemName.setText(name);
        long price = items.get(position).getItem().getPrice();
        holder.itemPrice.setText(String.valueOf(price));
        String quan = String.valueOf(items.get(position).getQuantity());
        if(!Integer.valueOf(items.get(position).getQuantity()).equals("")){
            holder.itemQuantity.setNumber(quan);
            //Toast.makeText(mConext, quan, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ElegantNumberButton.OnValueChangeListener {

        TextView itemName,itemPrice;
        ElegantNumberButton itemQuantity;
        OnQuanListner onQuanListner;
        int quan;

        public ViewHolder(@NonNull View itemView,OnQuanListner onQuanListner) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            this.onQuanListner = onQuanListner;

            itemQuantity.setOnValueChangeListener(this);


        }



        @Override
        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
            quan = Integer.valueOf(itemQuantity.getNumber());
            onQuanListner.onQuanClick(getAdapterPosition(),newValue);
        }
    }

    public interface OnQuanListner{
        void onQuanClick(int pos,int quan);
    }

}
