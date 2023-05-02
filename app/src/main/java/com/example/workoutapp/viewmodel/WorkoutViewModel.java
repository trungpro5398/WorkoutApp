package com.example.workoutapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.repository.WorkoutRepository;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private final WorkoutRepository repository;
    private final LiveData<List<Workout>> allWorkouts;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
        allWorkouts = repository.getAllWorkouts();
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }

    public void insert(List<Workout> workouts) {
        repository.insert(workouts);
    }
}
