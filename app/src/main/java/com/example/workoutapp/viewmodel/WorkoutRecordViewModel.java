package com.example.workoutapp.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.workoutapp.entity.WorkoutRecord;
import com.example.workoutapp.model.WorkoutCalorieCalculator;
import com.example.workoutapp.model.WorkoutUtils;
import com.example.workoutapp.model.WorkoutType;
import com.example.workoutapp.repository.WorkoutRecordRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WorkoutRecordViewModel extends AndroidViewModel {
    private WorkoutRecordRepository wrRepository;
    private LiveData<List<WorkoutRecord>> allWorkoutRecords;

    private static String FILTER_DATE_PATTERN = "yyyy-MM-dd";
    private static String DISPLAY_DATE_PATTERN = "E dd MMM";
    private MutableLiveData<String> selectedDate;

    private String todayDisplayDate = getTodayDate(DISPLAY_DATE_PATTERN);
    private LiveData<Integer> dailyDuration;

    private LiveData<Integer> totalCalories;

    private LiveData<List<WorkoutRecord>> todayWorkoutRecords;
    private String today;

    private HashMap<WorkoutType, Integer> workoutTypeDurations;

    public WorkoutRecordViewModel (@NonNull Application application) {
        super(application);
        wrRepository = new WorkoutRecordRepository(application);
        allWorkoutRecords = wrRepository.getAllWorkoutRecords();
        today = getTodayDate();
        todayWorkoutRecords = Transformations.map(allWorkoutRecords, records -> {
                    return getTodayWorkoutRecords(records);
                });
        dailyDuration = Transformations.map(allWorkoutRecords, records -> {
            return getTodayWorkoutDuration(records);
        });
        totalCalories = Transformations.map(todayWorkoutRecords, records -> {
            return calculateTotalCalories(records);
        });
    }

    private List<WorkoutRecord> getTodayWorkoutRecords(List<WorkoutRecord> records) {
        List<WorkoutRecord> todayRecords = new ArrayList<>();
        if (records != null){
            for (WorkoutRecord record: records
            ) {
                if (record.getWorkoutDate().equals(today) ) {
                    todayRecords.add(record);
                }
            }
        }
        return todayRecords;
    }

    private Integer calculateDurationByDate(String date, List<WorkoutRecord> records) {
        Integer totalDuration = 0;
        if (records != null){
            for (WorkoutRecord record: records
            ) {
                if (record.getWorkoutDate().equals(date) ) {
                    String d = record.getWorkoutDuration();

                    if (d.contains("hr")) {
                        int nd = Integer.valueOf(d.replace("hr", ""));
                        nd = nd * 60;
                        totalDuration = totalDuration + nd;
                    }
                    else if (d.contains("min")) {
                        int nd = Integer.valueOf(d.replace("min", ""));
                        totalDuration = totalDuration + nd;
                    }
//                    totalDuration += Integer.parseInt(record.getWorkoutDuration());
                }
            }
        }
        return totalDuration;
    }

    public Integer getTodayWorkoutDuration(List<WorkoutRecord> records) {
        return calculateDurationByDate(today, records);
    }

    public LiveData<List<WorkoutRecord>> getAllWorkoutRecords() {
        return allWorkoutRecords;
    }

    private static String getTodayDate(){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(FILTER_DATE_PATTERN);
        String todayDate = dateFormat.format(LocalDate.now());
        return todayDate;
    }

    private static String getTodayDate(String pattern){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
        String todayDate = dateFormat.format(LocalDate.now());
        return todayDate;
    }

    public void insert(WorkoutRecord workoutRecord) {
        wrRepository.insert(workoutRecord);
    }
    public LiveData<Integer> getDailyDuration(){
        return dailyDuration;
    }

    public void setSelectedDate(String date){
        selectedDate.setValue(date);
    }

    public String getTodayDateDisplay(){
        return todayDisplayDate;
    }

    private Integer calculateTotalCalories(List<WorkoutRecord> records){
        WorkoutUtils mapper = WorkoutUtils.getWorkoutMapper();
        HashMap<WorkoutType, Integer> caloriesTable = new HashMap<>();
        HashMap<WorkoutType, Integer> durationMap = WorkoutUtils.getWorkoutTypeIntMap();
        for (WorkoutRecord record: records
             ) {
            String sWorkoutType = record.getWorkoutType();
            WorkoutType workoutType = mapper.mapStrToWorkoutType(sWorkoutType);
            Integer durationMins = WorkoutUtils.convertToIntDuration(record.getWorkoutDuration());
            Integer calories = WorkoutCalorieCalculator.getWorkoutCalories(workoutType, durationMins);
            Integer currentCal = caloriesTable.get(workoutType) == null ? 0 : caloriesTable.get(workoutType);
            Integer updatedCalories = currentCal + calories;
            caloriesTable.put(workoutType, updatedCalories);
            durationMap.put(workoutType, durationMins + durationMap.get(workoutType));


        }

        Integer totalCalories = 0;
        if (caloriesTable.values() != null){
            for (Integer calorie : caloriesTable.values()) {
                totalCalories += calorie;
            }
        }
        workoutTypeDurations = durationMap;

        return totalCalories;
    }

    public LiveData<Integer> getTotalCalories() {
        return totalCalories;
    }

}
