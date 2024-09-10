package com.jadd.easyrestro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jadd.easyrestro.Settings.TaxActivity;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.Settings.AddCategory;
import com.jadd.easyrestro.Settings.AddItem;
import com.jadd.easyrestro.Settings.AddTable;
import com.jadd.easyrestro.Settings.MenuActivity;

public class SettingActivity extends AppCompatActivity {

    TextView menuText , addCategoryText, addItemText, addTableText,settingTax;
    Context context;
    String restroName;
    private String ownerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        restroName = getIntent().getStringExtra("RESTAURANT_NAME");
        ownerUID = getIntent().getStringExtra("OWNER_UID");
        context = getApplicationContext();

        settingTax = findViewById(R.id.setting_tax);
        menuText = findViewById(R.id.menu);
        addItemText = findViewById(R.id.addItem);
        addCategoryText = findViewById(R.id.addCategory);
        addTableText = findViewById(R.id.addTable);

        menuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , MenuActivity.class);
                intent.putExtra("RESTAURANT_NAME",restroName);
                intent.putExtra("OWNER_UID",ownerUID);
                startActivity(intent);
            }
        });

        settingTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , TaxActivity.class);
                intent.putExtra("RESTAURANT_NAME",restroName);
                intent.putExtra("OWNER_UID",ownerUID);
                startActivity(intent);
            }
        });

        addItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , AddItem.class);
                intent.putExtra("RESTAURANT_NAME",restroName);
                intent.putExtra("OWNER_UID",ownerUID);
                startActivity(intent);
            }
        });

        addCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , AddCategory.class);
                intent.putExtra("RESTAURANT_NAME",restroName);
                intent.putExtra("OWNER_UID",ownerUID);
                startActivity(intent);
            }
        });

        addTableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , AddTable.class);
                intent.putExtra("RESTAURANT_NAME",restroName);
                intent.putExtra("OWNER_UID",ownerUID);
                startActivity(intent);
            }
        });

    }
}
