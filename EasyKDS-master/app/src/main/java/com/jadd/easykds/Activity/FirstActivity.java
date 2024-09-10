package com.jadd.easykds.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jadd.easykds.R;

public class FirstActivity extends AppCompatActivity {

    TextView owner,employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        owner = findViewById(R.id.first_owner);
        employee = findViewById(R.id.first_employee);


        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this,LoginActivity.class);
                intent.putExtra("OWNER_FLAG",true);
                startActivity(intent);
            }
        });
        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this,LoginActivity.class);
                intent.putExtra("OWNER_FLAG",false);
                startActivity(intent);
            }
        });
    }
}
