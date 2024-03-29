package com.example.workoutapp.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.workoutapp.dao.WorkoutRecordDao;
import com.example.workoutapp.entity.database.WorkoutDatabase;
import com.example.workoutapp.entity.WorkoutRecord;

import java.util.List;

public class WorkoutRecordRepository {
    private WorkoutRecordDao workoutRecordDao;
    private LiveData<List<WorkoutRecord>> allWorkoutRecords;

    public WorkoutRecordRepository(Application application) {
        WorkoutDatabase db = WorkoutDatabase.getDatabase(application);
        workoutRecordDao = db.workoutRecordDao();
        allWorkoutRecords = workoutRecordDao.getAllWorkoutRecords();
    }

    public LiveData<List<WorkoutRecord>> getAllWorkoutRecords() {
        return allWorkoutRecords;
    }

    public void insert(WorkoutRecord record) {
        WorkoutDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                workoutRecordDao.insert(record);
            }
        });
    }

    public LiveData<List<WorkoutRecord>> getAllUserWorkouts(String id){
        return workoutRecordDao.getAllUserWorkoutRecords(id);
    }
}
