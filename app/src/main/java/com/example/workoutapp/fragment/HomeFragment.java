package com.example.workoutapp.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.adapter.HomeNewVideosAdapter;
import com.example.workoutapp.adapter.WorkoutAdapter;
import com.example.workoutapp.databinding.HomeFragmentBinding;
import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.entity.WorkoutRecord;
import com.example.workoutapp.viewmodel.HomeViewModel;
import com.example.workoutapp.viewmodel.WorkoutRecordViewModel;
import com.example.workoutapp.viewmodel.WorkoutViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment implements SensorEventListener {
    private HomeFragmentBinding binding;
    public HomeFragment(){}
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private WorkoutViewModel workoutViewModel;

    private WorkoutRecordViewModel workoutRecordViewModel;

    private HomeViewModel homeViewModel;
    private WorkoutAdapter workoutAdapter;
    private RecyclerView recyclerView;
    private String userLevel = "beginner"; // Change this to "intermediate" or "advanced" based on the user's level
    private AppCompatActivity mActivity;

    private RecyclerView recyclerView2;
    private List<Workout> newVideos;
    private RecyclerView.LayoutManager layoutManager;
//    private HomeNewVideosAdapter adapter;
    private WorkoutAdapter newVidAdapter;
    private TextView durationTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        homeViewModel = viewModelProvider.get(HomeViewModel.class);
        subscribeToHomeViewModel();

        workoutViewModel = viewModelProvider.get(WorkoutViewModel.class);
        workoutAdapter = new WorkoutAdapter();

        durationTextView = binding.activeTimeValue;
        workoutRecordViewModel = viewModelProvider.get(WorkoutRecordViewModel.class);
        subscribeToWorkoutRecordViewModel();
        binding.todayDate.setText(workoutRecordViewModel.getTodayDateDisplay());
        recyclerView = view.findViewById(R.id.recommended_vids);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(workoutAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        Button seeAllButton = view.findViewById(R.id.button);
        seeAllButton.setOnClickListener(v -> {
            SearchFragment searchFragment = new SearchFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, searchFragment)
                    .addToBackStack(null)
                    .commit();

            BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.search);
            NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_search);

        });
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        Sensor tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);


        if (tempSensor != null) {
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            Toast.makeText(getActivity(), "No temperature sensor on this device", Toast.LENGTH_SHORT).show();
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position) {
                    case 1:
                        userLevel = "intermediate";
                        break;
                    case 2:
                        userLevel = "advanced";
                        break;
                    default:
                        userLevel = "beginner";
                }
                fetchRandomWorkoutsByLevel(userLevel);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        // New videos
        workoutViewModel.getNewRandomWorkouts(userLevel, 10);
        newVideos = workoutViewModel.getNewRandomWorkouts(userLevel, 10).getValue();
        newVidAdapter = new WorkoutAdapter();
        newVidAdapter.setWorkoutList(newVideos);
        newVidAdapter.setOnItemClickListener((Workout video) -> {
            VideoFragment videoFragment = new VideoFragment();
            Bundle videoArgs = new Bundle();
            videoArgs.putString("videoId", video.getVideoId());
            videoFragment.setArguments(videoArgs);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, videoFragment)
                    .addToBackStack(null)
                    .commit();
        } );

        binding.newWorkoutsVids.setAdapter(newVidAdapter);
        layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        binding.newWorkoutsVids.setLayoutManager(layoutManager);


        fetchRandomWorkoutsByLevel(userLevel);
        return view;
    }

    private void subscribeToHomeViewModel(){
        homeViewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.username.setText("HELLO " + s.toUpperCase());
            }
        });
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()== Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature = event.values[0];
            String stringTemp = Float.toString(temperature);
            if (stringTemp != null ) {
                binding.tempText.setText(stringTemp);
            }

            if (temperature > 18) {
                binding.tempAdvice.setText("It's a great day to go for a run outside!");
            }
            else if (temperature < 18) {
                binding.tempAdvice.setText("It's brisk, a great day to work out at the gym");
            }
        }
    }
    private void subscribeToWorkoutRecordViewModel() {
        workoutRecordViewModel.getAllWorkoutRecords().observe(getViewLifecycleOwner(), new Observer<List<WorkoutRecord>>() {
            @Override
            public void onChanged(List<WorkoutRecord> workoutRecords) {
            }
        });
        workoutRecordViewModel.getDailyDuration().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                durationTextView.setText(Integer.toString(integer));
            }
        });

        workoutRecordViewModel.getTotalCalories().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer calories) {
                Pair<String,String> text = homeViewModel.getCaloriesText(calories);
                String value = text.first;
                String unit = text.second;
                binding.caloriesValue.setText(value);
                binding.caloriesUnit.setText(unit);
            }
        });

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

    }


    private void fetchRandomWorkoutsByLevel(String level) {
        workoutViewModel.getRandomWorkoutsByLevel(level, 10).observe(getViewLifecycleOwner(), workouts -> {
            workoutAdapter.setWorkoutList(workouts);
        });

        workoutViewModel.getNewRandomWorkouts(level, 10).observe(getViewLifecycleOwner(), workouts -> {
            newVidAdapter.setWorkoutList(workouts);
        });
    }

}
