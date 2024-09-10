package com.jadd.easyrestro.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.R;

import java.util.ArrayList;

public class NewOrderActivity extends AppCompatActivity {

    Spinner tableNumberSpinner;
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<String> spinnerList;
    DatabaseReference databaseTableNumber;
    Button nextButton;
    String restroName;
    private String ownerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        restroName = getIntent().getStringExtra("RESTAURANT_NAME");
        ownerUID = getIntent().getStringExtra("OWNER_UID");

        databaseTableNumber = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Table");

        //nameField = findViewById(R.id.order_name_field);
        //phoneNumberField = findViewById(R.id.order_phone_number_field);
        tableNumberSpinner = findViewById(R.id.order_table_number_spinner);
        nextButton = findViewById(R.id.next_button);

        final String category = (String) tableNumberSpinner.getSelectedItem();



        spinnerData();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseTableNumber.child((String)tableNumberSpinner
                        .getSelectedItem()).child("empty").setValue(false);
                Intent intent = new Intent(NewOrderActivity.this, OrderActivity.class);
                intent.putExtra("TABLE_NUMBER",(String)tableNumberSpinner
                        .getSelectedItem());
                intent.putExtra("RESTAURANT_NAME",restroName);
                intent.putExtra("OWNER_UID",ownerUID);
                startActivity(intent);
            }
        });

    }

    private void spinnerData() {
        spinnerList = new ArrayList<>();
        databaseTableNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    //Category category = item.getKey();
                    if((boolean) item.child("empty").getValue().equals(true)) {
                        spinnerList.add(item.getKey());
                    }
                }
                spinnerAdapter = new ArrayAdapter<>(NewOrderActivity.this,android.R.layout.select_dialog_item,spinnerList);
                tableNumberSpinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
