package com.jadd.easyrestro.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.classes.Tax;

public class TaxActivity extends AppCompatActivity {

    CheckBox vatCheckBox,serviceTaxCheckBox,serviceChargeCheckBox;
    EditText vatPercentText,vatOrderPercentText,serviceTaxPercentText,
            serviceTaxOrderPercentText,serviceChargePercentText,serviceChargeOrderPercentText;
    Button addVat,addServiceTax,addServiceCharge;
    LinearLayout vatlayout,serviceTaxLayout,serviceChargeLayout;
    Tax vat,serviceTax,serviceCharge;
    DatabaseReference databaseReference;
    String restroName;
    private String ownerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax);

        restroName = getIntent().getStringExtra("RESTAURANT_NAME");
        ownerUID = getIntent().getStringExtra("OWNER_UID");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child("Owner").child(ownerUID)
                .child("Restaurants").child(restroName).child("Constant").child("Tax");
        vatCheckBox =findViewById(R.id.vat_check_box);
        serviceTaxCheckBox = findViewById(R.id.service_tax_check_box);
        serviceChargeCheckBox = findViewById(R.id.service_charge_check_box);
        vatPercentText = findViewById(R.id.vat_percent);
        vatOrderPercentText = findViewById(R.id.vat_percent_order);
        serviceTaxPercentText = findViewById(R.id.service_tax_percent);
        serviceTaxOrderPercentText = findViewById(R.id.service_tax_percent_order);
        serviceChargePercentText = findViewById(R.id.service_charge_percent);
        serviceChargeOrderPercentText = findViewById(R.id.service_charge_percent_order);
        addVat = findViewById(R.id.add_vat);
        addServiceCharge = findViewById(R.id.add_service_charge);
        addServiceTax = findViewById(R.id.add_service_tax);
        vatlayout = findViewById(R.id.vat_view);
        serviceTaxLayout = findViewById(R.id.service_tax_view);
        serviceChargeLayout = findViewById(R.id.service_charge_view);
        vat = new Tax();
        serviceTax = new Tax();
        serviceCharge = new Tax();

        retrieveData();

        vatCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vatCheckBox.isChecked()){
                    //retrieveData();
                    vatlayout.setVisibility(View.VISIBLE);
                    vat.setInUse(true);
                    databaseReference.child("Vat").child("inUse").setValue(true);
                }
                else {
                    vatlayout.setVisibility(View.GONE);
                    vat.setInUse(false);
                    databaseReference.child("Vat").child("inUse").setValue(false);
                }
            }
        });
        serviceTaxCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(serviceTaxCheckBox.isChecked()){
                    serviceTaxLayout.setVisibility(View.VISIBLE);
                    serviceTax.setInUse(true);
                    databaseReference.child("Service Tax").setValue(serviceTax);
                }
                else {
                    serviceTaxLayout.setVisibility(View.GONE);
                    serviceTax.setInUse(false);
                    databaseReference.child("Service Tax").setValue(serviceTax);
                }
            }
        });
        serviceChargeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(serviceChargeCheckBox.isChecked()){
                    serviceChargeLayout.setVisibility(View.VISIBLE);
                    serviceCharge.setInUse(true);
                    databaseReference.child("Service Charge").setValue(serviceCharge);
                }
                else {
                    serviceChargeLayout.setVisibility(View.GONE);
                    serviceCharge.setInUse(false);
                    databaseReference.child("Service Charge").setValue(serviceCharge);
                }
            }
        });

        addVat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!String.valueOf(vatPercentText.getText()).equals("")&&
                        !String.valueOf(vatOrderPercentText.getText()).equals("")){
                    vat.setPercentTax(Integer.valueOf(String.valueOf(vatPercentText.getText())));
                    vat.setPercentOrder(Integer.valueOf(String.valueOf(vatOrderPercentText.getText())));
                    vat.setInUse(vatCheckBox.isChecked());
                    databaseReference.child("Vat").setValue(vat);
                }
                else {
                    Toast.makeText(TaxActivity.this, "Enter each field", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addServiceTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!String.valueOf(serviceTaxPercentText.getText()).equals("")&&
                        !String.valueOf(serviceTaxOrderPercentText.getText()).equals("")){
                    serviceTax.setPercentTax(Integer.valueOf(String.valueOf(serviceTaxPercentText.getText())));
                    serviceTax.setPercentOrder(Integer.valueOf(String.valueOf(serviceTaxOrderPercentText.getText())));
                    serviceTax.setInUse(serviceTaxCheckBox.isChecked());
                    databaseReference.child("Service Tax").setValue(serviceTax);
                }
                else {
                    Toast.makeText(TaxActivity.this, "Enter each field", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addServiceCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!String.valueOf(serviceChargePercentText.getText()).equals("")&&
                        !String.valueOf(serviceChargeOrderPercentText.getText()).equals("")){
                    serviceCharge.setPercentTax(Integer.valueOf(String.valueOf(serviceChargePercentText.getText())));
                    serviceCharge.setPercentOrder(Integer.valueOf(String.valueOf(serviceChargeOrderPercentText.getText())));
                    serviceCharge.setInUse(serviceChargeCheckBox.isChecked());
                    databaseReference.child("Service Charge").setValue(serviceCharge);
                }
                else {
                    Toast.makeText(TaxActivity.this, "Enter each field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void retrieveData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    Tax x = item.getValue(Tax.class);
                    if(String.valueOf(item.getKey()).equals("Vat")){
                        vatCheckBox.setChecked(x.inUse);
                        if(!x.inUse){
                            vatlayout.setVisibility(View.GONE);
                        }
                        else {
                            vatlayout.setVisibility(View.VISIBLE);
                        }
                        vatPercentText.setText(String.valueOf(x.getPercentTax()));
                        vatOrderPercentText.setText(String.valueOf(x.getPercentOrder()));
                    }
                    else if(String.valueOf(item.getKey()).equals("Service Tax")){
                        serviceTaxCheckBox.setChecked(x.inUse);
                        if(!x.inUse){
                            serviceTaxLayout.setVisibility(View.GONE);
                        }
                        else {
                            serviceTaxLayout.setVisibility(View.VISIBLE);
                        }
                        serviceTaxPercentText.setText(String.valueOf(x.getPercentTax()));
                        serviceTaxOrderPercentText.setText(String.valueOf(x.getPercentOrder()));
                    }
                    else if(String.valueOf(item.getKey()).equals("Service Charge")){
                        serviceChargeCheckBox.setChecked(x.inUse);
                        if(!x.inUse){
                            serviceChargeLayout.setVisibility(View.GONE);
                        }
                        else {
                            serviceChargeLayout.setVisibility(View.VISIBLE);
                        }
                        serviceChargePercentText.setText(String.valueOf(x.getPercentTax()));
                        serviceChargeOrderPercentText.setText(String.valueOf(x.getPercentOrder()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
