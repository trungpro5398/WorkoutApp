package com.example.workoutapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutapp.fragment.AnalyticalFragment;
import com.example.workoutapp.fragment.CalendarFragment;
import com.example.workoutapp.fragment.HomeFragment;
import com.example.workoutapp.fragment.MapFragment;
import com.example.workoutapp.fragment.SearchFragment;
import com.example.workoutapp.viewmodel.WorkoutViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.widget.Toast;

import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.retrofit.YouTubeApiService;
import com.example.workoutapp.retrofit.YouTubeResponse;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        // Set up the bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Set the default fragment to HomeFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
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

                        String title = item.snippet.title;
                        String videoId = item.id.videoId;
                        String thumbnailUrl = item.snippet.thumbnails.high.url; // Use high quality thumbnail
                        workouts.add(new Workout(title, videoId, workoutType, thumbnailUrl));

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




    // Bottom navigation item selection listener
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            menuItem -> {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.analytical:
                        selectedFragment = new AnalyticalFragment();
                        break;
                    case R.id.search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.calendar:
                        selectedFragment = new CalendarFragment();
                        break;
                    case R.id.map:
                        selectedFragment = new MapFragment();
                        break;
                }

                // Replace the current fragment with the selected one
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();

                return true;
            };
}
