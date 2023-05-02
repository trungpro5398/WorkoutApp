package com.example.workoutapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.workoutapp.dao.WorkoutDao;
import com.example.workoutapp.database.WorkoutDatabase;
import com.example.workoutapp.entity.Workout;

import java.util.List;

public class WorkoutRepository {
    private final WorkoutDao workoutDao;
    private final LiveData<List<Workout>> allWorkouts;

    public WorkoutRepository(Application application) {
        WorkoutDatabase db = WorkoutDatabase.getDatabase(application);
        workoutDao = db.workoutDao();
        allWorkouts = workoutDao.getAllWorkouts();
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }

    public void insert(List<Workout> workouts) {
        new InsertAsyncTask(workoutDao).execute(workouts);
    }

    private static class InsertAsyncTask extends AsyncTask<List<Workout>, Void, Void> {
        private final WorkoutDao workoutDao;

        InsertAsyncTask(WorkoutDao workoutDao) {
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(List<Workout>... workouts) {
            workoutDao.insertAll(workouts[0]);
            return null;
        }
    }
}
