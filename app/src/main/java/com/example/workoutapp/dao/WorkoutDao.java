package com.example.workoutapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.workoutapp.entity.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Workout> workouts);

    @Query("SELECT * FROM workout")
    LiveData<List<Workout>> getAllWorkouts();
}

