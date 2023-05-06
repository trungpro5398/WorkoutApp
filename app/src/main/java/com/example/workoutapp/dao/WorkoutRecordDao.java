package com.example.workoutapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.entity.WorkoutRecord;

import java.util.List;

@Dao
public interface WorkoutRecordDao {

    @Insert
    void insert(WorkoutRecord workoutRecord);

    @Query("SELECT * FROM workout_record")
    LiveData<List<WorkoutRecord>> getAllWorkoutRecords();

}

