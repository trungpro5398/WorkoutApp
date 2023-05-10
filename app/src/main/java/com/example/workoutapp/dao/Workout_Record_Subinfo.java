package com.example.workoutapp.dao;

import androidx.room.ColumnInfo;

public class Workout_Record_Subinfo {
    @ColumnInfo(name = "workoutDuration")
    public String workoutDuration;

    @ColumnInfo(name = "workoutType")
    public String workoutType;
}
