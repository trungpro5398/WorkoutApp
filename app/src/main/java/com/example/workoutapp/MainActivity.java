package com.example.workoutapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.adapter.WorkoutAdapter;
import com.example.workoutapp.adapter.WorkoutCategoryAdapter;
import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.retrofit.YouTubeApiService;
import com.example.workoutapp.retrofit.YouTubeResponse;
import com.example.workoutapp.viewmodel.WorkoutViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private WorkoutViewModel workoutViewModel;
    private WorkoutCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.workout_recycler_view);
        adapter = new WorkoutCategoryAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        workoutViewModel.getDistinctWorkoutTypes().observe(this, workoutTypes -> {
            adapter.setWorkoutTypeList(workoutTypes);
        });

        adapter.setOnItemClickListener(workoutType -> {
            Intent intent = new Intent(MainActivity.this, WorkoutListActivity.class);
            intent.putExtra("workoutType", workoutType.getType());
            startActivity(intent);
        });

        // Fetch workouts and insert them into the database using Retrofit
        fetchWorkouts();
    }



    private void fetchWorkouts() {
        List<String> workoutTypes = Arrays.asList("swim workout", "badminton workout", "yoga workout", "running workout", "cycling workout", "strength training workout", "pilates workout", "dancing workout", "hiking workout", "boxing workout", "kickboxing workout", "MMA workout", "tennis workout", "basketball workout", "football workout", "soccer workout", "volleyball workout");
        //List<String> workoutTypes = Arrays.asList("workout beginner, workout intermediate, workout advance");
        for (String workoutType : workoutTypes) {
            fetchWorkoutVideosByType(workoutType);
        }
    }

    private void fetchWorkoutVideosByType(String workoutType) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YouTubeApiService service = retrofit.create(YouTubeApiService.class);
        Call<YouTubeResponse> call = service.getWorkoutVideos(workoutType, "AIzaSyA4J0UTDplJQyGVFgYUiyaDqOC3HcYFKeM");

        call.enqueue(new Callback<YouTubeResponse>() {
            @Override
            public void onResponse(Call<YouTubeResponse> call, Response<YouTubeResponse> response) {
                if (response.isSuccessful()) {
                    List<Workout> workouts = new ArrayList<>();
                    for (YouTubeResponse.Item item : response.body().getItems()) {
                        workouts.add(new Workout(item.snippet.title, item.id.videoId, workoutType));
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
