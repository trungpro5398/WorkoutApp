package com.example.workoutapp.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.workoutapp.dao.Workout_Record_Subinfo;
import com.example.workoutapp.entity.WorkoutRecord;
import com.example.workoutapp.repository.WorkoutRecordRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WorkoutRecordViewModel extends AndroidViewModel {
    private WorkoutRecordRepository wrRepository;
    private LiveData<List<WorkoutRecord>> allWorkoutRecords;

    private static String FILTER_DATE_PATTERN = "yyyy-MM-dd";
    private static String DISPLAY_DATE_PATTERN = "E dd MMM";
    private MutableLiveData<String> selectedDate;

    private String todayDisplayDate = getTodayDate(DISPLAY_DATE_PATTERN);
    private LiveData<Integer> dailyDuration;

    private String today;

    public WorkoutRecordViewModel (@NonNull Application application) {
        super(application);
        wrRepository = new WorkoutRecordRepository(application);
        allWorkoutRecords = wrRepository.getAllWorkoutRecords();
        today = getTodayDate();
        dailyDuration = Transformations.map(allWorkoutRecords, records -> {
            return getTodayWorkoutDuration(records);
        });
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
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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

//    private LiveDatacalculateTotalCalories(){
//
//    }
}
