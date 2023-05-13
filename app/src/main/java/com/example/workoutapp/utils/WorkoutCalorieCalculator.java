package com.example.workoutapp.utils;

import com.example.workoutapp.model.WorkoutType;

import java.util.HashMap;

public class WorkoutCalorieCalculator {
    // Calories / min
    private static HashMap<WorkoutType, Integer> workoutTypeRateMapper;
    private static HashMap<WorkoutType, Integer> getWorkoutTypeCaloriesMap() {
        if (workoutTypeRateMapper == null) {
            HashMap<WorkoutType, Integer> hashmap = new HashMap<>();
            hashmap.put(WorkoutType.RUNNING, 240 / 30);
            hashmap.put(WorkoutType.WALKING, 107 / 30);
            hashmap.put(WorkoutType.CYCLING, 300 / 30);
            hashmap.put(WorkoutType.WEIGHTS, 90 / 30);
            hashmap.put(WorkoutType.CALISTHENICS, 135 / 30);
            workoutTypeRateMapper = hashmap;
        }
        return workoutTypeRateMapper;
    }

    public static Integer getWorkoutTypeCaloriesRate(WorkoutType type) {
        return getWorkoutTypeCaloriesMap().get(type);
    }
    public static Integer getWorkoutCalories(WorkoutType workoutType, Integer minutes){
        Integer rate = getWorkoutTypeCaloriesRate(workoutType);
        Integer calories = rate * minutes;
        return calories;
    }
}
