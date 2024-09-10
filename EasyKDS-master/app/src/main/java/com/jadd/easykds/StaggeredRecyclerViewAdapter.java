package com.jadd.easykds;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder>{

    DatabaseReference databaseReference;
    Cart cart;
    ArrayList<String> tableNumber;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Cart> items = new ArrayList<>();
    //int tableNumber;

    public StaggeredRecyclerViewAdapter(Context mContext,ArrayList<String> tableNumber) {
        this.mContext = mContext;
        this.tableNumber = tableNumber;
    }

    Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_order,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.tableNumberView.setText("Table Number - "+tableNumber.get(position));

        //Add recycler view later
        databaseReference =FirebaseDatabase.getInstance().getReference("Table")
                .child(tableNumber.get(position)).child("Cart");

        //holder.orderView.setLayoutManager(new LinearLayoutManager(mContext));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.orderView.setLayoutManager(layoutManager);

        readData(new MyCallback() {
            @Override
            public void onCallback(ArrayList<Cart> items) {
                Log.d(TAG, "onCallback: "+items.toString());
                recyclerViewAdapter = new RecyclerViewAdapter(mContext, items);
                holder.orderView.setAdapter(recyclerViewAdapter);
            }
        });



        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                if(dataSnapshot!=null) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        for (DataSnapshot item1 : item.getChildren()) {
                            cart = item1.getValue(Cart.class);
                            items.add(cart);

                        }
                    }


                    recyclerViewAdapter = new RecyclerViewAdapter(mContext, items);
                    holder.orderView.setAdapter(recyclerViewAdapter);
                    for(int i = 0;i<items.size();i++) {
                        Log.d(TAG, "onDataChange: " + items.get(i).getItem().getName());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        //RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mContext,items);
        //holder.orderView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public int getItemCount() {
        return tableNumber.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView orderView;
        TextView tableNumberView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderView = itemView.findViewById(R.id.order_recycler_view);
            tableNumberView = itemView.findViewById(R.id.table_number_view);
        }
    }
    private interface MyCallback {
        void onCallback(ArrayList<Cart> items);
    }

    private void readData(final MyCallback myCallback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                if(dataSnapshot!=null) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        for (DataSnapshot item1 : item.getChildren()) {
                            cart = item1.getValue(Cart.class);
                            items.add(cart);

                        }
                    }

                    myCallback.onCallback(items);
                    recyclerViewAdapter = new RecyclerViewAdapter(mContext, items);
                    //holder.orderView.setAdapter(recyclerViewAdapter);
                    for(int i = 0;i<items.size();i++) {
                        Log.d(TAG, "onDataChange: " + items.get(i).getItem().getName());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+ databaseError.getMessage());
            }
        });
    }
}
