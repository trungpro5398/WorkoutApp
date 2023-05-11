package com.example.workoutapp.model;

import android.util.Log;

import java.util.HashMap;
import java.util.List;

public class WorkoutUtils {

    private HashMap<String, WorkoutType> workoutTypeMapper ;

    private  HashMap<String, WorkoutType> getWorkoutTypeEnumMapper() {
        HashMap<String, WorkoutType> hashmap = new HashMap<>();
        hashmap.put("Running", WorkoutType.RUNNING);
        hashmap.put("Walking", WorkoutType.WALKING);
        hashmap.put("Cycling", WorkoutType.CYCLING);
        hashmap.put("Weights", WorkoutType.WEIGHTS);
        hashmap.put("Calisthenics", WorkoutType.CALISTHENICS);
        return hashmap;
    }


    //    https://www.health.harvard.edu/diet-and-weight-loss/calories-burned-in-30-minutes-for-people-of-three-different-weights

    // Calories / min
    private HashMap<WorkoutType, Integer> getWorkoutTypeCaloriesMap() {
        HashMap<WorkoutType, Integer>  hashmap = new HashMap<>();
        hashmap.put(WorkoutType.RUNNING, 240/30);
        hashmap.put(WorkoutType.WALKING, 107/30);
        hashmap.put(WorkoutType.CYCLING, 300/30);
        hashmap.put(WorkoutType.WEIGHTS, 90/30);
        hashmap.put(WorkoutType.CALISTHENICS, 135/30);
        return hashmap;
    }

    private HashMap<WorkoutType, Integer> workoutTypeCaloriesMap;



    private WorkoutUtils(){
        workoutTypeMapper = getWorkoutTypeEnumMapper();
        workoutTypeCaloriesMap = getWorkoutTypeCaloriesMap();

    }

    private static WorkoutUtils instance;
    public static WorkoutUtils getWorkoutMapper(){
        if (instance == null) {
            return new WorkoutUtils();
        }
        else {
            return instance;
        }

    }

    public Integer getWorkoutTypeCaloriesRate(WorkoutType type) {
        return workoutTypeCaloriesMap.get(type);
    };

    public WorkoutType mapStrToWorkoutType(String workoutType){
        return workoutTypeMapper.get(workoutType);
    }

    public static Integer convertToIntDuration(String duration){
        int minutes = 0;
        if (duration.contains("hr")) {
            minutes = Integer.valueOf(duration.replace("hr", ""));
            return minutes = minutes * 60;
        }
        else if (duration.contains("min")) {
            return minutes = Integer.valueOf(duration.replace("min", ""));
        }
        Log.d("TAG", "convertToIntDuration: not converted successfully");
        return minutes;
    }

    public static HashMap<WorkoutType, Integer> getWorkoutTypeIntMap() {
        WorkoutType[] workoutTypes = WorkoutType.values();
        HashMap<WorkoutType, Integer> hashMap = new HashMap<>(workoutTypes.length);
        for (WorkoutType type: workoutTypes
             ) {
            hashMap.put(type, 0);
        }
        return hashMap;
    }
}
