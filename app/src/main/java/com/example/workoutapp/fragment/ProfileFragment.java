package com.example.workoutapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.workoutapp.MainActivity;
import com.example.workoutapp.R;
import com.example.workoutapp.workmanager.UploadWorker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private FirebaseUser user;

    private EditText editTextName, editTextEmail, editTextGoal;
    private Button buttonUpdate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);

        buttonUpdate = view.findViewById(R.id.buttonUpdate);

        // Load user data from Firebase
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);


                // Set the data to the EditText fields
                editTextName.setText(name);
                editTextEmail.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        // Update user data in Firebase when the update button is clicked
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editTextName.getText().toString();
                String newEmail = editTextEmail.getText().toString();


                // Update the user data in Firebase
                usersRef.child("name").setValue(newName);
                usersRef.child("email").setValue(newEmail);


                Toast.makeText(getContext(), "User data updated", Toast.LENGTH_SHORT).show();
            }
        });
        Button startUploadWorkerButton = view.findViewById(R.id.startUploadWorkerButton);
        startUploadWorkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
                String formattedDate = dateFormat.format(new Date());

                Log.d("BackUpRoomDirectly", "Back up process called at " +formattedDate);
                WorkManager.getInstance(getContext()).enqueue(new OneTimeWorkRequest.Builder(UploadWorker.class).build());

            }
        });
        return view;
    }


}