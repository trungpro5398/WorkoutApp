package com.example.workoutapp.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_record")
public class WorkoutRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String workoutType;
    private String workoutDuration;
    private String workoutDate;

    public WorkoutRecord(int id, String workoutType, String workoutDuration, String workoutDate) {
        this.id = id;
        this.workoutType = workoutType;
        this.workoutDuration = workoutDuration;
        this.workoutDate = workoutDate;
    }

    public int getId() { return id; }

    public String getWorkoutDate() {
        return workoutDate;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public String getWorkoutDuration() {
        return workoutDuration;
    }

    public void setWorkoutDate(String workoutDate) {
        this.workoutDate = workoutDate;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public void setWorkoutDuration(String workoutDuration) {
        this.workoutDuration = workoutDuration;
    }
}
