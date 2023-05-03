package com.example.workoutapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.adapter.WorkoutAdapter;
import com.example.workoutapp.viewmodel.WorkoutViewModel;

public class WorkoutListActivity extends AppCompatActivity {
    private WorkoutViewModel workoutViewModel;
    private WorkoutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        String workoutType = getIntent().getStringExtra("workoutType");
        RecyclerView recyclerView = findViewById(R.id.workout_list_recycler_view);
        adapter = new WorkoutAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        workoutViewModel.getWorkoutsByType(workoutType).observe(this, workouts -> {

            adapter.setWorkoutList(workouts);
        });

        adapter.setOnItemClickListener(workout -> {
            Intent intent = new Intent(WorkoutListActivity.this, VideoActivity.class);
            intent.putExtra("videoId", workout.getVideoId());
            startActivity(intent);
        });
    }
}
