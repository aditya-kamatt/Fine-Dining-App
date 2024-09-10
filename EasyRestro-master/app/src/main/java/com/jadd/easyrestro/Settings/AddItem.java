package com.jadd.easyrestro.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.classes.Item;

import java.util.ArrayList;

import javax.xml.validation.Validator;

public class AddItem extends AppCompatActivity {

    EditText itemName,itemPrice;
    Spinner spinner;
    Button button;
    ValueEventListener valueEventListener;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    DatabaseReference databaseReference,databaseItem;
    long maxId= 0;
    String restroName;

    Validator validator;
    private String ownerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        restroName = getIntent().getStringExtra("RESTAURANT_NAME");
        ownerUID = getIntent().getStringExtra("OWNER_UID");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Category");

        itemName = findViewById(R.id.item_name_edit_text);
        itemPrice = findViewById(R.id.item_price_edit_text);
        button = findViewById(R.id.add_item_button);
        spinner = findViewById(R.id.spinner);

        list = new ArrayList<>();

        retrievedata();




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

    }

    private void addItem() {
        String item = itemName.getText().toString().trim();






        if(!itemName.getText().toString().equals("") && !itemPrice.getText().toString().equals("")){
            //String id = databaseReference.push().getKey();
            int price = Integer.parseInt(itemPrice.getText().toString().trim());
            String category = spinner.getSelectedItem().toString();
            databaseItem = databaseReference.child(category).child("Items");
            Item item1 = new Item(item,price,maxId+1);
            databaseItem.child(item).setValue(item1);
            itemName.setText("");
            itemPrice.setText("");
            Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Item not addded!",Toast.LENGTH_SHORT).show();
        }

    }

    public void retrievedata(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    //Category category = item.getKey();
                    list.add(item.getKey());

                }
                adapter = new ArrayAdapter<>(AddItem.this,android.R.layout.select_dialog_item,list);
                spinner.setAdapter(adapter);
                //Toast.makeText(AddItem.this, "Add", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
