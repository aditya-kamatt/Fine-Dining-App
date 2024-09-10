package com.jadd.easyrestro.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.LoginAndSignUp.FirstActivity;
import com.jadd.easyrestro.R;

public class EmployeeWaitActivity extends AppCompatActivity {

    boolean ownerFlag;
    Button id;
    DatabaseReference loginReference;
    boolean assigned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_wait);
        ownerFlag = getIntent().getBooleanExtra("OWNER_FLAG",false);
        id = findViewById(R.id.employee_copy_id);
        loginReference = FirebaseDatabase.getInstance().getReference("Users").child("Employee").child(FirebaseAuth.getInstance().getUid());

        loginReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assigned = dataSnapshot.child("assigned").getValue(Boolean.class);
                if(assigned){
                    Intent intent = new Intent(EmployeeWaitActivity.this,EmployeeRestaurantActivity.class);
                    intent.putExtra("OWNER_FLAG",ownerFlag);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ID", FirebaseAuth.getInstance().getUid());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(EmployeeWaitActivity.this, "Your id has been copied!", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(EmployeeWaitActivity.this, FirstActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
