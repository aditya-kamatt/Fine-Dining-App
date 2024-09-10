package com.jadd.easyrestro.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.Adapter.RecyclerViewAdapter;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.classes.Cart;
import com.jadd.easyrestro.classes.Item;

import java.util.ArrayList;

public class CartAddItemActivity extends AppCompatActivity implements RecyclerViewAdapter.OnQuanListner {

    Spinner spinner;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    String category;
    DatabaseReference databaseReference,dataBase,tableNumberReference,databaseAddItemReference;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    ArrayList<Cart> items = new ArrayList<Cart>();
    String itemName;
    Long itemPrice;
    static int quan1,flag;
    Item item1;
    long id;
    Cart cart;
    Button goButton;
    private String tableNumber;
    String restroName;
    private String ownerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_add_item);
        restroName = getIntent().getStringExtra("RESTAURANT_NAME");
        ownerUID = getIntent().getStringExtra("OWNER_UID");
        tableNumber = getIntent().getStringExtra("TABLE_NUMBER");
        spinner = findViewById(R.id.category_spinner_add_item_cart);

        goButton = findViewById(R.id.go_button);
        list = new ArrayList<>();
        spinnerData();

        recyclerView = findViewById(R.id.category_recyclerView_add_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartAddItemActivity.this));

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.clear();
                //Toast.makeText(CartAddItemActivity.this, "Go Clicked!", Toast.LENGTH_SHORT).show();
                category = String.valueOf(spinner.getSelectedItem());
                databaseReference = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child("Owner").child(ownerUID)
                        .child("Restaurants").child(restroName).child("Category").child(category).child("Items");
                databaseAddItemReference = FirebaseDatabase.getInstance().getReference("Users")
                        .child("Owner").child(ownerUID)
                        .child("Restaurants").child(restroName).child("Table")
                        .child(tableNumber).child("Cart");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null) {
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                quan1 = 0;
                                flag = 0;
                                item1 = item.getValue(Item.class);
                                cart = new Cart(category, item1);
                                items.add(cart);

                            }

                            databaseAddItemReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    if (dataSnapshot1 != null) {
                                        for (DataSnapshot ch : dataSnapshot1.getChildren()) {
                                            for (int i = 0; i < items.size(); i++) {
                                                if (ch.getKey().equals(items.get(i).getItem().getName()) && dataSnapshot1.exists()) {
                                                    //Toast.makeText(CartAddItemActivity.this, "Here!", Toast.LENGTH_SHORT).show();
                                                    quan1 = ((Long) ch.child("quantity").getValue()).intValue();
                                                    items.get(i).setQuantity(quan1);
                                                    flag = 1;
                                                }
                                            }
                                        }
                                        recyclerViewAdapter = new RecyclerViewAdapter(CartAddItemActivity.this, items, CartAddItemActivity.this);
                                        recyclerView.setAdapter(recyclerViewAdapter);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            if (flag != 1) {
                                recyclerViewAdapter = new RecyclerViewAdapter(CartAddItemActivity.this, items, CartAddItemActivity.this);
                                recyclerView.setAdapter(recyclerViewAdapter);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public interface MyCallback {
        void onCallback(int value);
    }

    public void readData(final MyCallback myCallback) {
        databaseAddItemReference = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(FirebaseAuth.getInstance().getUid())
                .child("Restaurants").child(restroName).child("Table")
                .child(tableNumber).child("Cart").child(category);
        databaseAddItemReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {

                if(dataSnapshot1!=null){
                    for(DataSnapshot ch: dataSnapshot1.getChildren()) {
                        if (ch.getKey().equals(item1.getName()) && dataSnapshot1.exists()) {
                            quan1 = ((Long) ch.child("quantity").getValue()).intValue();
                            myCallback.onCallback(quan1);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void spinnerData() {

        dataBase = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Category");
        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    //Category category = item.getKey();
                    list.add(item.getKey());

                }
                adapter = new ArrayAdapter<>(CartAddItemActivity.this,android.R.layout.select_dialog_item,list);
                spinner.setAdapter(adapter);
                //Toast.makeText(AddItem.this, "Add", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onQuanClick(int position,int quan) {
        tableNumberReference = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Table")
                .child(tableNumber).child("Cart");
        String category = items.get(position).getCategory();
        String itemName = items.get(position).getItem().getName();
        if(quan==0){
            tableNumberReference.child(itemName).removeValue();
            return;
        }
        items.get(position).setQuantity(quan);

        tableNumberReference.child(itemName).setValue(items.get(position));
    }
}
