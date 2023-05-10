package com.example.workoutapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutapp.dao.Workout_Record_Subinfo;
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

    public String getWorkoutDurationForDate(String date){
        // TODO: Replace Hardcoded Value with selected user date



        LiveData<List<Workout_Record_Subinfo>> data = wrRepository.getAllWorkoutRecordsByDate("2023-05-08");
        Integer totalDuration = 0;
        List<Workout_Record_Subinfo> workouts = data.getValue();
//        if (workouts != null){
//            for (Workout_Record_Subinfo workout: workouts
//                 ) {
//                totalDuration += Integer.parseInt(workout.workoutDuration);
//
//            }
//        }
        if (allWorkoutRecords.getValue() != null){
            for (WorkoutRecord workoutRecord : allWorkoutRecords.getValue()
            ) {
                if (workoutRecord.getWorkoutDate() == "2023-05-08" ) {
                    totalDuration += Integer.parseInt(workoutRecord.getWorkoutDuration());
                }
            }
        }
        return Integer.toString(totalDuration);
    }

    public void insert(WorkoutRecord workoutRecord) {
        wrRepository.insert(workoutRecord);
    }
}
