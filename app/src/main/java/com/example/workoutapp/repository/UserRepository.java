package com.example.workoutapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {
    private FirebaseAuth mAuth;
    private DatabaseReference usersDbRef;
    private FirebaseUser user;

    private static UserRepository instance;

    private MutableLiveData<String> name = new MutableLiveData<>();

    private UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null){
            // User is signed In
            usersDbRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            setupDbListener();
        } else {
            // No User is signed in
        }

    }

    private void setupDbListener() {
        usersDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dataName = dataSnapshot.child("name").getValue(String.class);
                if (!dataName.equals(name.getValue())){
                    name.setValue(dataName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static UserRepository getUserRepository() {
        if (instance == null ) {
            instance = new UserRepository();
        }
        return instance;
    }

    public MutableLiveData<String> getUserName() {
        return name;
    }

}
