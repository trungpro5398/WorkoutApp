package com.example.workoutapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutapp.entity.WorkoutRecord;
import com.example.workoutapp.repository.WorkoutRecordRepository;

import java.util.List;

public class WorkoutRecordViewModel extends AndroidViewModel {
    private WorkoutRecordRepository wrRepository;
    private LiveData<List<WorkoutRecord>> allWorkoutRecords;

    public WorkoutRecordViewModel (@NonNull Application application) {
        super(application);
        wrRepository = new WorkoutRecordRepository(application);
        allWorkoutRecords = wrRepository.getAllWorkoutRecords();
    }

    public LiveData<List<WorkoutRecord>> getAllWorkoutRecords() {
        return allWorkoutRecords;
    }

    public void insert(WorkoutRecord workoutRecord) {
        wrRepository.insert(workoutRecord);
    }
}
