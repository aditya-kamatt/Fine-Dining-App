package com.jadd.easyrestro.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.classes.Category;

public class AddCategory extends AppCompatActivity {

    EditText categoryNameEditTExt;
    Button button;
    DatabaseReference databaseReference;
    long maxID = 0;
    String restroName;
    String ownerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        restroName = getIntent().getStringExtra("RESTAURANT_NAME");
        ownerUID = getIntent().getStringExtra("OWNER_UID");


        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Category");

        categoryNameEditTExt = findViewById(R.id.category_edit_text);
        button = findViewById(R.id.add_category_button);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxID = dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
                categoryNameEditTExt.setText("");
            }
        });

    }

    private void addCategory(){

        String categoryName = categoryNameEditTExt.getText().toString().trim();

        if(!TextUtils.isEmpty(categoryName)){
            //String id = databaseReference.push().getKey();
            Category category = new Category(categoryName,maxID+1);
            databaseReference.child(categoryName).setValue(category);
            Toast.makeText(this, "Category added!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Category not addded!",Toast.LENGTH_SHORT).show();
        }
    }
}
