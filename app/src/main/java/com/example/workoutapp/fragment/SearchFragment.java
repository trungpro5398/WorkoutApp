package com.example.workoutapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.WorkoutListActivity;
import com.example.workoutapp.adapter.WorkoutCategoryAdapter;
import com.example.workoutapp.viewmodel.WorkoutViewModel;


public class SearchFragment extends Fragment {

    private WorkoutViewModel workoutViewModel;
    private WorkoutCategoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.workout_recycler_view);
        adapter = new WorkoutCategoryAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
        workoutViewModel.getDistinctWorkoutTypes().observe(getViewLifecycleOwner(), workoutTypes -> {
            adapter.setWorkoutTypeList(workoutTypes);
        });
        adapter.setOnItemClickListener(workoutType -> {
            Intent intent = new Intent(requireActivity(), WorkoutListActivity.class);
            intent.putExtra("workoutType", workoutType.getType());
            startActivity(intent);
        });
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }
}
