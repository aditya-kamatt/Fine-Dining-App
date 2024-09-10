package com.jadd.easyrestro.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.Adapter.RecyclerViewAdapter;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.classes.Cart;
import com.jadd.easyrestro.classes.Order;
import com.jadd.easyrestro.classes.Tax;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity implements RecyclerViewAdapter.OnQuanListner{


    RecyclerView cartRecyclerView;
    RecyclerViewAdapter cartRecyclerViewAdapter;
    Button addItemButton,checkOutButton;
    TextView cartSubTotal,cartTotal,cartTax,cartDiscount;
    DatabaseReference databaseTableNumber,tableNumberReference,ref,taxRef,orderRef;
    String tableNumber;
    ArrayList<Cart> items = new ArrayList<Cart>();
    CheckBox kitchenCheckBox;
    boolean sendToKitchen,ownerFlag;
    Cart cart;
    Tax vat,serviceTax,serviceCharge,x;
    StringBuilder s;
    int subTotal,tax;
    Order order;
    int count=0;
    String restroName;
    private String ownerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        restroName = getIntent().getStringExtra("RESTAURANT_NAME");
        ownerUID = getIntent().getStringExtra("OWNER_UID");
        tableNumber = getIntent().getStringExtra("TABLE_NUMBER");
        ownerFlag = getIntent().getBooleanExtra("OWNER_FLAG",true);
        databaseTableNumber = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Table").child(tableNumber).child("Cart");
        orderRef = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Orders");
        ref = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Table");
        taxRef = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Constant").child("Tax");

        kitchenCheckBox = findViewById(R.id.sendToKitchen);
        cartDiscount = findViewById(R.id.cart_discount);
        cartDiscount.setVisibility(View.GONE);
        cartTax = findViewById(R.id.cart_tax);
        cartTotal = findViewById(R.id.amount_total);
        cartSubTotal = findViewById(R.id.amount_subtotal);
        addItemButton = findViewById(R.id.order_add_item_button);
        checkOutButton = findViewById(R.id.order_checkout_button);
        cartRecyclerView = findViewById(R.id.order_cart_recycler_view);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));


        databaseTableNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    items.clear();
                    subTotal = 0;
                    tax=0;
                    if(dataSnapshot==null){
                        return;
                    }
                    else {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            if (!item.getValue().equals(null)||!item.getValue().equals("0")) {
                                cart = item.getValue(Cart.class);
                                items.add(cart);
                            }
                        }
                        cartRecyclerViewAdapter = new RecyclerViewAdapter(OrderActivity.this, items, OrderActivity.this);
                        cartRecyclerView.setAdapter(cartRecyclerViewAdapter);
                        ref.child(tableNumber).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if((boolean)dataSnapshot.child("sendToKitchen").getValue()){
                                    kitchenCheckBox.setChecked(true);
                                }
                                else {
                                    kitchenCheckBox.setChecked(false);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        kitchenCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if(kitchenCheckBox.isChecked()){
                                    ref.child(tableNumber).child("sendToKitchen").setValue(true);
                                }
                                else {
                                    ref.child(tableNumber).child("sendToKitchen").setValue(false);
                                }
                            }
                        });
                    }
                    if(items.size()==0){
                        cartSubTotal.setText("SubTotal - 0");
                        cartTax.setText("Tax - 0");
                        cartTotal.setText("Total - 0");
                    }
                    else {
                        for(int i = 0;i<items.size();i++){
                            subTotal = (int) (subTotal + ((items.get(i).getQuantity())*(items.get(i).getItem().getPrice())));
                        }
                        taxRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                s = new StringBuilder();
                                int count=0;
                                for(DataSnapshot item1:dataSnapshot.getChildren()){

                                    x = item1.getValue(Tax.class);
                                    if(x.inUse){
                                        count++;
                                        if(count!=1){
                                            s.append("\n");
                                        }
                                        int temp = subTotal * (x.getPercentOrder())/100;
                                        temp = temp * (x.getPercentTax()) /100;
                                        s.append(item1.getKey()+" - "+temp);
                                        tax = tax + temp;

                                    }
                                }
                                s.trimToSize();
                                cartTax.setText(s);
                                cartTotal.setText("Total - "+(tax+subTotal));
                                checkOutButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                        order = new Order(items,currentDate,currentTime,Integer.valueOf(tableNumber),
                                                tax+subTotal,subTotal,tax);
                                        orderRef.child(currentDate).child(currentTime).setValue(order);
                                        databaseTableNumber.removeValue();
                                        ref.child(tableNumber).child("empty").setValue(true);
                                        ref.child(tableNumber).child("sendToKitchen").setValue(false);
                                        Intent i = new Intent(OrderActivity.this, MainActivity.class);
                                        i.putExtra("RESTAURANT_NAME",restroName);
                                        i.putExtra("OWNER_UID",ownerUID);
                                        startActivity(i);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        cartSubTotal.setText("SubTotal - "+subTotal);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, CartAddItemActivity.class);
                intent.putExtra("TABLE_NUMBER",tableNumber);
                intent.putExtra("RESTAURANT_NAME",restroName);
                intent.putExtra("OWNER_UID",ownerUID);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //i have changed
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            ref.child(tableNumber).child("empty").setValue(true);
            ref.child(tableNumber).child("sendToKitchen").setValue(false);
            databaseTableNumber.removeValue();
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("RESTAURANT_NAME",restroName);
            intent.putExtra("OWNER_UID",ownerUID);
            intent.putExtra("OWNER_FLAG",ownerFlag);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_print) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onQuanClick(int position, int quan) {
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
