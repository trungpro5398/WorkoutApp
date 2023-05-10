package com.example.workoutapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutapp.fragment.AnalyticalFragment;
import com.example.workoutapp.fragment.CalendarFragment;
import com.example.workoutapp.fragment.HomeFragment;
import com.example.workoutapp.fragment.MapFragment;
import com.example.workoutapp.fragment.SearchFragment;
import com.example.workoutapp.navigation.BottomNavigation;
import com.example.workoutapp.navigation.NavigationDrawer;
import com.example.workoutapp.viewmodel.WorkoutViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.widget.Toast;

import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.retrofit.YouTubeApiService;
import com.example.workoutapp.retrofit.YouTubeResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    private WorkoutViewModel workoutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //Open Login and registration first before any other fragment, Can comment it out to test other fragements !
//        auth = FirebaseAuth.getInstance();
//
//        user = auth.getCurrentUser();
//        if (user == null){
//            Intent intent = new Intent(getApplicationContext(), Login.class);
//            startActivity(intent);
//            finish();
//        }


        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        // Initialize and set up NavigationDrawer
        NavigationDrawer navigationDrawer = new NavigationDrawer(this, drawerLayout);
        navigationDrawer.setupNavigation(toolbar, navigationItemSelectedListener);

        // Initialize and set up BottomNavigation
        BottomNavigation bottomNavigation = new BottomNavigation(this);
        bottomNavigation.setupBottomNavigation(navListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

//         fetchWorkouts();
    }

    private void fetchWorkouts() {
        //List<String> workoutTypes = Arrays.asList("swim workout", "badminton workout", "yoga workout", "running workout", "cycling workout", "strength training workout", "pilates workout", "dancing workout", "hiking workout", "boxing workout", "kickboxing workout", "MMA workout", "tennis workout", "basketball workout", "football workout", "soccer workout", "volleyball workout");
        List<String> workoutTypes = Arrays.asList("workout beginner, workout intermediate, workout advance");
        for (String workoutType : workoutTypes) {
            fetchWorkoutVideosByType(workoutType);
        }
    }

    // Fetching new workout videos on homepage
    private void fetchNewWorkouts(String workoutLevel) {
        List<String> workoutTypes = Arrays.asList("workout beginner, workout intermediate, workout advance");
            fetchWorkoutVideosByType("new workout " + workoutLevel);
    }
    private void fetchWorkoutVideosByType(String workoutType) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YouTubeApiService service = retrofit.create(YouTubeApiService.class);
//        Call<YouTubeResponse> call = service.getWorkoutVideos(workoutType, "AIzaSyA4J0UTDplJQyGVFgYUiyaDqOC3HcYFKeM");
//        Call<YouTubeResponse> call = service.getWorkoutVideos(workoutType, "AIzaSyA8yQYb2T6yPTFBsBf7ZUhhtwW7lcPL7Dw");
        Call<YouTubeResponse> call = service.getWorkoutVideos(workoutType, "dummy");

        call.enqueue(new Callback<YouTubeResponse>() {
            @Override
            public void onResponse(Call<YouTubeResponse> call, Response<YouTubeResponse> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    List<Workout> workouts = new ArrayList<>();
                    for (YouTubeResponse.Item item : response.body().getItems()) {

                        String title = item.snippet.title;
                        String videoId = item.id.videoId;
                        String thumbnailUrl = item.snippet.thumbnails.high.url; // Use high-quality thumbnail
                        String level = detectWorkoutLevel(title); // Detect workout level based on the title
                        workouts.add(new Workout(title, videoId, workoutType, thumbnailUrl, level)); // Add level to Workout object
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
    private String detectWorkoutLevel(String title) {
        String lowerCaseTitle = title.toLowerCase();
        if (lowerCaseTitle.contains("beginner") || lowerCaseTitle.contains("basic") || lowerCaseTitle.contains("easy")) {
            return "beginner";
        } else if (lowerCaseTitle.contains("intermediate")) {
            return "intermediate";
        } else if (lowerCaseTitle.contains("advanced") || lowerCaseTitle.contains("expert") || lowerCaseTitle.contains("hard")) {
            return "advanced";
        }
        return "intermediate";
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

    private final NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            menuItem -> {
                // Handle navigation view item clicks here.
                int id = menuItem.getItemId();

                Fragment selectedFragment = null;

                switch (id) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_analytical:
                        selectedFragment = new AnalyticalFragment();
                        break;
                    case R.id.nav_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.nav_calendar:
                        selectedFragment = new CalendarFragment();
                        break;
                    case R.id.nav_map:
                        selectedFragment = new MapFragment();
                        break;
                    case R.id.nav_sign_out:
                        signOut();
                        break;
                    // Add more cases for other menu items if needed
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            };
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        // Redirect to the login activity or another appropriate activity after signing out
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();

    }

}
