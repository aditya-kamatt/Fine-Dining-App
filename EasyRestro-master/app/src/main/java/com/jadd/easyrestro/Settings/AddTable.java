package com.jadd.easyrestro.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.classes.Table;

public class AddTable extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText tableNumberEditText;
    Button button;
    Table table;
    String restroName;
    private String ownerUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);
        restroName = getIntent().getStringExtra("RESTAURANT_NAME");
        ownerUID = getIntent().getStringExtra("OWNER_UID");
        tableNumberEditText = findViewById(R.id.table_number);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Table");

        button = findViewById(R.id.add_table_button);
        //Toast.makeText(AddTable.this, "Table Added", Toast.LENGTH_SHORT).show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tableN = tableNumberEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(tableN)) {
                    int tableNumber = Integer.parseInt(tableNumberEditText.getText().toString());
                    table = new Table(tableNumber, true,false);
                    databaseReference.child(String.valueOf(tableNumber)).setValue(table);
                    tableNumberEditText.setText("");
                    Toast.makeText(AddTable.this, "Table Added", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddTable.this, "Table not Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
