package com.example.workoutapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.adapter.WorkoutAdapter;
import com.example.workoutapp.viewmodel.WorkoutViewModel;

public class WorkoutListFragment extends Fragment {
    private WorkoutViewModel workoutViewModel;
    private WorkoutAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.workout_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String workoutType = args != null ? args.getString("workoutType") : "";

        RecyclerView recyclerView = view.findViewById(R.id.workout_list_recycler_view);
        adapter = new WorkoutAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
        workoutViewModel.getWorkoutsByType(workoutType).observe(getViewLifecycleOwner(), workouts -> {
            adapter.setWorkoutList(workouts);
        });

        adapter.setOnItemClickListener(workout -> {
            VideoFragment videoFragment = new VideoFragment();
            Bundle videoArgs = new Bundle();
            videoArgs.putString("videoId", workout.getVideoId());
            videoFragment.setArguments(videoArgs);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, videoFragment)
                    .addToBackStack(null)
                    .commit();
        });


    }
}
