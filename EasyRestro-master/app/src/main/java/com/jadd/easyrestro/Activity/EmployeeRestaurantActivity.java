package com.jadd.easyrestro.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.Adapter.RestaurantRecyclerViewAdapter;
import com.jadd.easyrestro.LoginAndSignUp.FirstActivity;
import com.jadd.easyrestro.R;

import java.util.ArrayList;

public class EmployeeRestaurantActivity extends AppCompatActivity  implements RestaurantRecyclerViewAdapter.OnRestaurantClickListener{

    RecyclerView recyclerView;
    RestaurantRecyclerViewAdapter restaurantRecyclerViewAdapter;
    ArrayList<String> restaurantNames;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_restaurant);

        restaurantNames = new ArrayList<>();
        recyclerView = findViewById(R.id.employee_restaurant_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(EmployeeRestaurantActivity.this));
        //Toast.makeText(this, "Why ?", Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Employee")
                .child(FirebaseAuth.getInstance().getUid());

        databaseReference.child("Restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    restaurantNames.add(item.getKey());
                }
                restaurantRecyclerViewAdapter = new RestaurantRecyclerViewAdapter(EmployeeRestaurantActivity.this,restaurantNames
                        ,EmployeeRestaurantActivity.this);
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
            Intent intent = new Intent(EmployeeRestaurantActivity.this, FirstActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onRestaurantClick(int position) {
        Intent intent = new Intent(EmployeeRestaurantActivity.this,MainActivity.class);
        intent.putExtra("RESTAURANT_NAME",restaurantNames.get(position));
        intent.putExtra("OWNER_FLAG",false);
        startActivity(intent);
    }
}
