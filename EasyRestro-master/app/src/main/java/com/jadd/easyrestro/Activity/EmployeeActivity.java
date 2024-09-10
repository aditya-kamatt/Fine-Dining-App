package com.jadd.easyrestro.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.Adapter.EmployeeRecyclerViewAdapter;
import com.jadd.easyrestro.Adapter.RestaurantRecyclerViewAdapter;
import com.jadd.easyrestro.LoginAndSignUp.SignUpActivity;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.classes.Employee;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity implements EmployeeRecyclerViewAdapter.OnEmployeeNameClickListner {

    ArrayList<String> names;
    private RecyclerView recyclerView;
    private String restroName;
    private DatabaseReference databaseReference;
    private EmployeeRecyclerViewAdapter restaurantRecyclerViewAdapter;
    String ownerUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        restroName = getIntent().getStringExtra("RESTAURANT_NAME");
        ownerUID = getIntent().getStringExtra("OWNER_UID");
        names = new ArrayList<>();
        recyclerView = findViewById(R.id.employee_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(EmployeeActivity.this));

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Owner")
                .child(ownerUID);

        databaseReference.child("Restaurants").child(restroName).child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                names.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    names.add(item.child("name").getValue().toString());
                }
                restaurantRecyclerViewAdapter = new EmployeeRecyclerViewAdapter(EmployeeActivity.this,names
                        ,EmployeeActivity.this);
                recyclerView.setAdapter(restaurantRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_employee_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeActivity.this, AddEmployeeActivity.class);
                intent.putExtra("OWNER_FLAG" ,true);
                intent.putExtra("RESTAURANT_NAME",restroName);
                intent.putExtra("OWNER_UID",ownerUID);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onEmployeeClick(int pos) {

    }
}
