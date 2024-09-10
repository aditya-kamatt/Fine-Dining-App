package com.jadd.easyrestro.LoginAndSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadd.easyrestro.Activity.RestaurantActivity;
import com.jadd.easyrestro.R;
import com.jadd.easyrestro.Repository.Repository;
import com.jadd.easyrestro.classes.Employee;
import com.jadd.easyrestro.classes.User;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class SignUpActivity extends AppCompatActivity {

    EditText nameText,passwordText,emailText,phoneText;
    Button signUpButton;
    DatabaseReference signUpReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    User user1;
    Employee employee;
    Repository repository;
    private boolean ownerFlag;
    //Employee employee;
    private FirebaseUser ownerUser;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ownerFlag = getIntent().getBooleanExtra("OWNER_FLAG",true);
        nameText = findViewById(R.id.owner_activity_name);
        passwordText = findViewById(R.id.owner_activity_password);
        signUpButton = findViewById(R.id.sign_up_activity_button);
        emailText = findViewById(R.id.owner_activity_email);
        phoneText = findViewById(R.id.owner_activity_phone);
        //Toast.makeText(SignUpActivity.this, "OnCLick", Toast.LENGTH_SHORT).show();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ownerName = nameText.getText().toString();
                String ownerPassword = passwordText.getText().toString();
                String ownerEmail = emailText.getText().toString();
                String ownerPhone = phoneText.getText().toString();
                user1 = new User(ownerName,ownerEmail,ownerPhone);
                signUpReference = FirebaseDatabase.getInstance().getReference("Users");
                //signUpReference.child("x").setValue("x");

                if(auth.getCurrentUser()!=null){
                    ownerUser = FirebaseAuth.getInstance().getCurrentUser();
                }

                signUpReference.child("Employee").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        id = (long)dataSnapshot.child("count").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //Toast.makeText(SignUpActivity.this, "OnCLick", Toast.LENGTH_SHORT).show();
                //repository = new Repository(user1,ownerPassword);
                //repository.signUp();

                auth.createUserWithEmailAndPassword(user1.getEmail(), ownerPassword)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user1's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    final FirebaseUser user = auth.getCurrentUser();
                                    //signUpReference.child("a").setValue("x");
                                    if(ownerFlag) {
                                        signUpReference.child("Owner").child(user.getUid()).setValue(SignUpActivity.this.user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    else {
                                        employee = new Employee(user1,id+1);
                                        signUpReference.child("Employee").child(user.getUid()).setValue(SignUpActivity.this.employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                intent.putExtra("OWNER_FLAG",false );
                                                FirebaseAuth.getInstance().signOut();
                                                startActivity(intent);
                                            }
                                        });
                                        signUpReference.child("Employee").child("count").setValue(id+1);
                                    }
                                } else {
                                    // If sign in fails, display a message to the user1.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                }
                            }
                        });

                /*Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.putExtra("OWNER_NAME",ownerName);
                startActivity(intent);*/
            }
        });

    }
}
