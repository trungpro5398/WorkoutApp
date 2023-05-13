package com.example.workoutapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_record")
public class WorkoutRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "user_id")
    private String userId;
    @NonNull
    private String workoutType;
    @NonNull
    private String workoutDuration;

    @NonNull
    private String workoutDate;

    public WorkoutRecord(String workoutType, String workoutDuration, String workoutDate) {
        this.workoutType = workoutType;
        this.workoutDuration = workoutDuration;
        this.workoutDate = workoutDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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
