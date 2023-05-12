package com.example.workoutapp.entity.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.workoutapp.dao.WorkoutDao;
import com.example.workoutapp.dao.WorkoutRecordDao;
import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.entity.WorkoutRecord;

import android.content.Context;

import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Workout.class, WorkoutRecord.class}, version = 8)
public abstract class WorkoutDatabase extends RoomDatabase {
    public abstract WorkoutDao workoutDao();
    public abstract WorkoutRecordDao workoutRecordDao();
    private static volatile WorkoutDatabase INSTANCE;
    //we create an ExecutorService with a fixed thread pool so we can later run database operations asynchronously on a background thread.
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //A synchronized method in a multi threaded environment means that two threads are not allowed to access data at the same time
    public static WorkoutDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WorkoutDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WorkoutDatabase.class, "workout_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

}
