package com.example.workoutapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.workoutapp.dao.WorkoutRecordDao;
import com.example.workoutapp.dao.Workout_Record_Subinfo;
import com.example.workoutapp.entity.database.WorkoutDatabase;
import com.example.workoutapp.entity.WorkoutRecord;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class WorkoutRecordRepository {
    private WorkoutRecordDao workoutRecordDao;
    private LiveData<List<WorkoutRecord>> allWorkoutRecords;

    private LiveData<List<Workout_Record_Subinfo>> selectedDateRecords;

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

    public LiveData<List<Workout_Record_Subinfo>> getAllWorkoutRecordsByDate(String date){
        selectedDateRecords = workoutRecordDao.getAllWorkoutRecordsByDate(date);
        return selectedDateRecords;
    }
}
