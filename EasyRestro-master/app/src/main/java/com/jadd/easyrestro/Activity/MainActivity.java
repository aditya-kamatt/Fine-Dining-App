package com.jadd.easyrestro.Activity;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.Adapter.MainRecyclerViewAdapter;
import com.jadd.easyrestro.LoginAndSignUp.FirstActivity;
import com.jadd.easyrestro.R;

import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.OnTableNumberListner
        , NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    RecyclerView recyclerView;
    MainRecyclerViewAdapter adapter;
    ArrayList<String> tableNumber;
    DatabaseReference databaseTableNumber,employeeReference;
    String restroName;
    private boolean ownerFlag;
    String ownerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ownerFlag = getIntent().getBooleanExtra("OWNER_FLAG",true);
        restroName = getIntent().getStringExtra("RESTAURANT_NAME");

        DrawerLayout drawerLayout = findViewById(R.id.main_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(this);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        employeeReference = FirebaseDatabase.getInstance().getReference("Users").child("Employee");
        //menu.setGroupVisible(R.id.owner_group, false);
        if(!ownerFlag) {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            menu.setGroupVisible(R.id.owner_group, false);
            navigationView.setNavigationItemSelectedListener(this);
            employeeReference.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ownerUID = dataSnapshot.child("ownerUid").getValue(String.class);
                    databaseTableNumber = FirebaseDatabase.getInstance().getReference("Users")
                            .child("Owner").child(ownerUID)
                            .child("Restaurants").child(restroName).child("Table");

                    recyclerView = findViewById(R.id.recycler_view_main);
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,4));
                    tableNumber = new ArrayList<String>();
                    databaseTableNumber.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot item: dataSnapshot.getChildren()){
                                //Category category = item.getKey();
                                if(((boolean) item.child("empty").getValue().equals(false))&&!tableNumber.contains(item.getKey())) {

                                    tableNumber.add(item.getKey());
                                }
                                if(((boolean) item.child("empty").getValue().equals(true))&&tableNumber.contains(item.getKey())){
                                    tableNumber.remove(item.getKey());
                                }
                            }

                            adapter = new MainRecyclerViewAdapter(MainActivity.this,tableNumber,MainActivity.this);

                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            ownerUID = FirebaseAuth.getInstance().getUid();
            navigationView.setNavigationItemSelectedListener(this);

            databaseTableNumber = FirebaseDatabase.getInstance().getReference("Users")
                    .child("Owner").child(FirebaseAuth.getInstance().getUid())
                    .child("Restaurants").child(restroName).child("Table");

            recyclerView = findViewById(R.id.recycler_view_main);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
            tableNumber = new ArrayList<String>();
            databaseTableNumber.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        //Category category = item.getKey();
                        if (((boolean) item.child("empty").getValue().equals(false)) && !tableNumber.contains(item.getKey())) {

                            tableNumber.add(item.getKey());
                        }
                        if (((boolean) item.child("empty").getValue().equals(true)) && tableNumber.contains(item.getKey())) {
                            tableNumber.remove(item.getKey());
                        }
                    }

                    adapter = new MainRecyclerViewAdapter(MainActivity.this, tableNumber, MainActivity.this);

                    recyclerView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewOrderActivity.class);
                intent.putExtra("RESTAURANT_NAME",restroName);
                intent.putExtra("OWNER_FLAG",ownerFlag);
                intent.putExtra("OWNER_UID",ownerUID);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTableNumberClick(int position) {
        Intent intent = new Intent(this, OrderActivity.class);
        String table_number = tableNumber.get(position);
        //Toast.makeText(this, table_number, Toast.LENGTH_SHORT).show();
        intent.putExtra("RESTAURANT_NAME",restroName);
        intent.putExtra("TABLE_NUMBER",table_number);
        intent.putExtra("OWNER_FLAG",ownerFlag);
        intent.putExtra("OWNER_UID",ownerUID);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id == R.id.action_settings){
            Intent intent = new Intent(this, SettingActivity.class);
            intent.putExtra("RESTAURANT_NAME",restroName);
            intent.putExtra("OWNER_UID",ownerUID);
            startActivity(intent);
        }
        else if(id == R.id.action_restaurant){
            Intent i;
            if(ownerFlag) {
                i = new Intent(this, RestaurantActivity.class);
            }
            else {
                i = new Intent(this, EmployeeRestaurantActivity.class);
            }
            startActivity(i);
        }
        else if(id == R.id.action_sign_out){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(MainActivity.this, FirstActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_employee){
            Intent intent = new Intent(MainActivity.this, EmployeeActivity.class);
            intent.putExtra("RESTAURANT_NAME",restroName);
            intent.putExtra("OWNER_UID",ownerUID);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        drawerView.bringToFront();
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
