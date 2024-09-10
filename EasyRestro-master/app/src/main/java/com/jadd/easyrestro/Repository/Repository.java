package com.jadd.easyrestro.Repository;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jadd.easyrestro.classes.User;

import java.util.concurrent.Executor;

import androidx.annotation.NonNull;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class Repository {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference signUpReference;
    private User user;
    String password;

    public Repository(User user, String password) {
        this.user = user;
        this.password = password;
    }

    public void signUp() {
        auth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            signUpReference = FirebaseDatabase.getInstance().getReference("Users");
                            signUpReference.child(user.getUid()).setValue(Repository.this.user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}
