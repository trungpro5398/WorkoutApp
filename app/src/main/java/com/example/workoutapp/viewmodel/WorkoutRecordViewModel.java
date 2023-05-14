package com.example.workoutapp.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.entity.WorkoutRecord;
import com.example.workoutapp.repository.UserRepository;
import com.example.workoutapp.utils.WorkoutCalorieCalculator;
import com.example.workoutapp.utils.WorkoutUtils;
import com.example.workoutapp.model.WorkoutType;
import com.example.workoutapp.repository.WorkoutRecordRepository;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WorkoutRecordViewModel extends AndroidViewModel {
    private final WorkoutRecordRepository wrRepository;
    private LiveData<List<WorkoutRecord>> allWorkoutRecords;

    public final static String FILTER_DATE_PATTERN = "yyyy-MM-dd";
    private final static String DISPLAY_DATE_PATTERN = "E dd MMM";
    private MutableLiveData<String> selectedDate = new MutableLiveData<>(getTodayDate());

    private String todayDisplayDate = getTodayDate(DISPLAY_DATE_PATTERN);
    private LiveData<Integer> dailyDuration;

    private LiveData<Integer> totalCalories;

    private LiveData<List<WorkoutRecord>> todayWorkoutRecords;

    private MutableLiveData<List<WorkoutRecord>> selectedWorkoutRecords;
    private String today;

    private HashMap<WorkoutType, Integer> workoutTypeDurations;

    public WorkoutRecordViewModel (@NonNull Application application) {
        super(application);
        wrRepository = new WorkoutRecordRepository(application);
        String userId = UserRepository.getUserRepository().getUserId();
        allWorkoutRecords = userId != null ? wrRepository.getAllUserWorkouts(userId) : new MutableLiveData<>(new ArrayList<>());
        today = getTodayDate();
        setSelectedDate(today);
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

    public LiveData<List<WorkoutRecord>> getSelectedWorkoutRecords() {
        if (selectedWorkoutRecords == null) {
            selectedWorkoutRecords = new MutableLiveData<>(new ArrayList<>());
        }
        return selectedWorkoutRecords;
    }

    public void updateSelectedWorkoutRecords(){
        if (allWorkoutRecords.getValue()!= null && selectedDate.getValue() != null){
            List<WorkoutRecord> selectedRecords = getWorkoutRecordsByDate(allWorkoutRecords.getValue(), selectedDate.getValue());
            selectedWorkoutRecords.setValue(selectedRecords);
        }
    }
    private List<WorkoutRecord> getWorkoutRecordsByDate(List<WorkoutRecord> records, String date) {
        List<WorkoutRecord> selectedRecords = new ArrayList<>();
        if (records != null){
            for (WorkoutRecord record: records
            ) {
                if (record.getWorkoutDate().equals(date) ) {
                    selectedRecords.add(record);
                }
            }
        }
        return selectedRecords;
    }

    public List<WorkoutRecord> getWorkoutRecordsByDate(String date) {
        List<WorkoutRecord> selectedRecords = new ArrayList<>();
        List<WorkoutRecord> allRecords = allWorkoutRecords.getValue();
        if (allRecords != null){
            for (WorkoutRecord record: allRecords
            ) {
                if (record.getWorkoutDate().equals(date) ) {
                    selectedRecords.add(record);
                }
            }
        }
        return selectedRecords;
    }
    private List<WorkoutRecord> getTodayWorkoutRecords(List<WorkoutRecord> records) {
        return getWorkoutRecordsByDate(records, today);
    }


    private Integer calculateDurationByDate(String date, List<WorkoutRecord> records) {
        Integer totalDuration = 0;
        if (records != null){
            for (WorkoutRecord record: records
            ) {
                if (record.getWorkoutDate().equals(date) ) {
                    String d = record.getWorkoutDuration();
                    Integer durationMins = WorkoutUtils.convertToIntDuration(d);
                    totalDuration += durationMins;
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
        return dateFormat.format(LocalDate.now());
    }

    private static String getTodayDate(String pattern){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
        return dateFormat.format(LocalDate.now());
    }

    public void insert(WorkoutRecord workoutRecord) {
        String userId = UserRepository.getUserRepository().getUserId();
        if (userId != null){
            workoutRecord.setUserId(userId);
        }

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

    public Integer getSelectedDayTotalCalories() {
        if(selectedWorkoutRecords == null || selectedWorkoutRecords.getValue() == null) {
            return 0;
        }
        return calculateTotalCalories(selectedWorkoutRecords.getValue());
    }

    public LiveData<Integer> getTotalCalories() {
        return totalCalories;
    }

    public MutableLiveData<String> getSelectedDate (){
        return selectedDate;
    }

    public Integer getSelectedDayTotalDuration() {
        if(selectedWorkoutRecords == null ) {
            return 0;
        }
        List<WorkoutRecord> workoutRecords = selectedWorkoutRecords.getValue();
        Integer duration = calculateDurationByDate(getSelectedDate().getValue(), workoutRecords);
        return duration;
    }

    public List<WorkoutRecord> getAllWorkoutTypeDurations() {
        List<WorkoutRecord> list = new ArrayList<>();
        if(workoutTypeDurations != null) {
            String date = getSelectedDate().getValue();
            workoutTypeDurations.forEach((key, value) -> {
                if (value!=null && value > 0) {
                    String workoutType = StringUtils.capitalize(key.name().toLowerCase());
                    WorkoutRecord record = new WorkoutRecord(workoutType, Integer.toString(value), date);
                    list.add(record);
                }
            });
        }
        return list;
    }


}
