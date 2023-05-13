package com.example.workoutapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.entity.WorkoutType;
import com.example.workoutapp.repository.WorkoutRepository;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private final WorkoutRepository workoutRepository;
    private final LiveData<List<Workout>> allWorkouts;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        workoutRepository = new WorkoutRepository(application);
        allWorkouts = workoutRepository.getAllWorkouts();
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }

    public void insert(List<Workout> workouts) {
        workoutRepository.insert(workouts);
    }

    public LiveData<List<WorkoutType>> getDistinctWorkoutTypes() {
        return workoutRepository.getDistinctWorkoutTypes();
    }
    public LiveData<List<Workout>> getWorkoutsByType(String workoutType) {
        return workoutRepository.getWorkoutsByType(workoutType);
    }


    public LiveData<List<Workout>> getRandomWorkoutsByLevel(String level, int limit) {
        return workoutRepository.getRandomWorkoutsByLevel(level, limit);
    }

}
