package com.jadd.easyrestro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.classes.Restaurant;

public class NewRestaurantActivity extends AppCompatActivity {

    EditText restaurantName;
     Button button;
     Restaurant restaurant;
     DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_restaurant);
        restaurantName = findViewById(R.id.restaurant_name);
        button = findViewById(R.id.restaurant_next_button);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Owner")
                .child(FirebaseAuth.getInstance().getUid()).child("Restaurants");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String restroName = restaurantName.getText().toString().trim();
                if(restroName.isEmpty()){
                    restaurantName.setError("Restaurant name cannot be empty!");
                    restaurantName.requestFocus();
                    return;
                }
                if(restroName != null){
                    restaurant = new Restaurant(restroName);
                    databaseReference.child(restroName).setValue(restaurant);
                    restaurantName.setText("");
                }
                else {
                    Toast.makeText(NewRestaurantActivity.this, "Enter Restaurant Name!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
