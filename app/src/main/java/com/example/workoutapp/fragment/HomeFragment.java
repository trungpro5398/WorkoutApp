package com.example.workoutapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.adapter.WorkoutAdapter;
import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.viewmodel.WorkoutViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {
    private WorkoutViewModel workoutViewModel;
    private WorkoutAdapter workoutAdapter;
    private RecyclerView recyclerView;
    private String userLevel = "beginner"; // Change this to "intermediate" or "advanced" based on the user's level
    private AppCompatActivity mActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        workoutAdapter = new WorkoutAdapter();

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
        fetchRandomWorkoutsByLevel(userLevel);


        return view;
    }



    private void fetchRandomWorkoutsByLevel(String level) {
        workoutViewModel.getRandomWorkoutsByLevel(level, 10).observe(getViewLifecycleOwner(), workouts -> {
            workoutAdapter.setWorkoutList(workouts);
        });
    }

}
