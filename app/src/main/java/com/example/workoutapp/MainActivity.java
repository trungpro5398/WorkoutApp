package com.example.workoutapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.adapter.WorkoutAdapter;
import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.retrofit.YouTubeApiService;
import com.example.workoutapp.retrofit.YouTubeResponse;
import com.example.workoutapp.viewmodel.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private WorkoutViewModel workoutViewModel;
    private WorkoutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.workout_recycler_view);
        adapter = new WorkoutAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        workoutViewModel.getAllWorkouts().observe(this, workouts -> {
            adapter.setWorkoutList(workouts);
        });

        adapter.setOnItemClickListener(workout -> {
            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            intent.putExtra("videoId", workout.getVideoId());
            startActivity(intent);
        });

        // Fetch workouts and insert them into the database using Retrofit
        fetchWorkouts();
    }

    private void fetchWorkouts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YouTubeApiService service = retrofit.create(YouTubeApiService.class);
        Call<YouTubeResponse> call = service.getWorkoutVideos(BuildConfig.YOUTUBE_API_KEY);

        call.enqueue(new Callback<YouTubeResponse>() {
            @Override
            public void onResponse(Call<YouTubeResponse> call, Response<YouTubeResponse> response) {
                if (response.isSuccessful()) {
                    List<Workout> workouts = new ArrayList<>();
                    for (YouTubeResponse.Item item : response.body().getItems()) {
                        workouts.add(new Workout(item.snippet.title, item.id.videoId));
                    }
                    workoutViewModel.insert(workouts);
                }
            }

            @Override
            public void onFailure(Call<YouTubeResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error fetching workouts", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
