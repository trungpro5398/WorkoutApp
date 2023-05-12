package com.example.workoutapp.model;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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



    private HashMap<WorkoutType, Integer> workoutTypeCaloriesMap;



    private WorkoutUtils(){
        workoutTypeMapper = getWorkoutTypeEnumMapper();

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

    /** @param:date in milliseconds
     * */
    public static String dateFormatter(long date, String formatPattern){
            SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
            Date dateObj = new Date(date);
            String parsedDate = formatter.format(dateObj);
            return parsedDate;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Long parseDateToMs(String date, String formatPattern){
        SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
        try {
            Date parsedDate = formatter.parse(date);
            Long ms = parsedDate.getTime();
            return ms;
        } catch (ParseException e) {
            Log.d("TAG", "parseDateToMs: Failed to parse");
            return 0L;

        }
    }
}
