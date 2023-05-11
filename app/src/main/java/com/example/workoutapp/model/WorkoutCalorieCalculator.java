package com.example.workoutapp.model;

public class WorkoutCalorieCalculator {

    private static WorkoutUtils workoutMapper = WorkoutUtils.getWorkoutMapper();
    public static Integer getWorkoutCalories(WorkoutType workoutType, Integer minutes){
        Integer calories = 0;
        Integer rate = workoutMapper.getWorkoutTypeCaloriesRate(workoutType);
        calories = rate * minutes;
        return calories;
    }
}
