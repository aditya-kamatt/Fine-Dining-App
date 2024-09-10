package com.jadd.easykds.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easykds.Adapter.RestaurantRecyclerViewAdapter;
import com.jadd.easykds.R;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity   implements RestaurantRecyclerViewAdapter.OnRestaurantClickListener{

    RecyclerView recyclerView;
    RestaurantRecyclerViewAdapter restaurantRecyclerViewAdapter;
    ArrayList<String> restaurantNames;
    DatabaseReference databaseReference;
    boolean ownerFlag;
    String ownerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ownerFlag = getIntent().getBooleanExtra("OWNER_FLAG",true);
        restaurantNames = new ArrayList<>();
        recyclerView = findViewById(R.id.restaurant_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(RestaurantActivity.this));
        //Toast.makeText(this, "Why ?", Toast.LENGTH_SHORT).show();

        if(ownerFlag){
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Owner")
                    .child(FirebaseAuth.getInstance().getUid());
            ownerUID = FirebaseAuth.getInstance().getUid();
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Employee")
                    .child(FirebaseAuth.getInstance().getUid());
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!ownerFlag){
                    ownerUID = dataSnapshot.child("ownerUid").getValue(String.class);
                }
                for(DataSnapshot item : dataSnapshot.child("Restaurants").getChildren()){
                    restaurantNames.add(item.getKey());
                }
                restaurantRecyclerViewAdapter = new RestaurantRecyclerViewAdapter(RestaurantActivity.this,restaurantNames
                        ,RestaurantActivity.this);
                recyclerView.setAdapter(restaurantRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.owner_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_owner_sign_out){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(RestaurantActivity.this, FirstActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onRestaurantClick(int position) {
        Intent intent = new Intent(RestaurantActivity.this,MainActivity.class);
        intent.putExtra("RESTAURANT_NAME",restaurantNames.get(position));
        intent.putExtra("OWNER_FLAG",false);
        intent.putExtra("OWNER_UID",ownerUID);
        startActivity(intent);
    }

}
