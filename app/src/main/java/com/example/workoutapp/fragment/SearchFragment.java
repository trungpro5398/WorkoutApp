package com.example.workoutapp.fragment;

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

import com.example.workoutapp.R;
import com.example.workoutapp.adapter.WorkoutCategoryAdapter;
import com.example.workoutapp.databinding.SearchFragmentBinding;
import com.example.workoutapp.viewmodel.WorkoutViewModel;

public class SearchFragment extends Fragment {

    private WorkoutViewModel workoutViewModel;
    private WorkoutCategoryAdapter adapter;
    private SearchFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new WorkoutCategoryAdapter();
        binding.workoutRecyclerView.setAdapter(adapter);
        binding.workoutRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
        workoutViewModel.getDistinctWorkoutTypes().observe(getViewLifecycleOwner(), workoutTypes -> {
            adapter.setWorkoutTypeList(workoutTypes);
        });
        adapter.setOnItemClickListener(workoutType -> {
            WorkoutListFragment fragment = new WorkoutListFragment();
            Bundle args = new Bundle();
            args.putString("workoutType", workoutType.getType());
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
