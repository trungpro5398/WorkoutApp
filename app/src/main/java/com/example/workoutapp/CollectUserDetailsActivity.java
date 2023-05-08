package com.example.workoutapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.workoutapp.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CollectUserDetailsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    TextInputEditText editTextAge, editTextWeight, editTextHeight,  editTextName;
    Button submitButton;

    Spinner spinner, spinner2;

    String goal, gender;
    String[] goals = {"Select Your Fitness Goal","Lose Weight", "Gain Muscle", "Stay Fit"};
    String[] genders = {"Select Gender","Male", "Female", "Other", "Choose not to say"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_user_details);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        // Replace these with the EditText fields you added in the layout file
        editTextAge = findViewById(R.id.age);
        editTextHeight = findViewById(R.id.height);
        editTextWeight= findViewById(R.id.weight);
        editTextName  = findViewById(R.id.nameField);

        //Spinner 1
        spinner = findViewById(R.id.spinner);

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, goals);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                goal = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //spinner 2
        spinner2 = findViewById(R.id.spinner2);

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genders);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        // submit button
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            String age = editTextAge.getText().toString().trim();
            String weight = editTextWeight.getText().toString().trim();
            String height = editTextHeight.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            // Retrieve values from other EditText fields as needed

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(CollectUserDetailsActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(age)) {
                Toast.makeText(CollectUserDetailsActivity.this, "Please enter your age", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(weight)) {
                Toast.makeText(CollectUserDetailsActivity.this, "Please enter your weight", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(height)) {
                Toast.makeText(CollectUserDetailsActivity.this, "Please enter your height", Toast.LENGTH_SHORT).show();
                return;
            }

            if (goal.equals("Select Your Fitness Goal")) {
                Toast.makeText(CollectUserDetailsActivity.this, "Please select your fitness goal", Toast.LENGTH_SHORT).show();
                return;
            }

            if (gender.equals("Select Gender")) {
                Toast.makeText(CollectUserDetailsActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                return;
            }

            saveUserDataToDatabase(name, age, weight, height);

        });
    }

    private void saveUserDataToDatabase(String name, String age, String weight, String height) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference usersRef = mDatabase.getReference("users");

            // Add your user data to a Map (e.g., name, age, etc.)
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", user.getEmail());
            userData.put("name", name);
            userData.put("age", age);
            userData.put("weight", weight);
            userData.put("height", height);
            userData.put("goal", goal);
            userData.put("gender",gender);
            // Add other data as needed

            usersRef.child(user.getUid()).setValue(userData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(CollectUserDetailsActivity.this, "User details saved!", Toast.LENGTH_SHORT).show();
                            // Redirect to the main activity of your app
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                            finish();
                        } else {
                            Toast.makeText(CollectUserDetailsActivity.this, "Failed to save user details" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(CollectUserDetailsActivity.this, "No user found", Toast.LENGTH_SHORT).show();
        }
    }

}
