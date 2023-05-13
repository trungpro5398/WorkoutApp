package com.example.workoutapp.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.workoutapp.dao.WorkoutRecordDao;
import com.example.workoutapp.entity.WorkoutRecord;
import com.example.workoutapp.entity.database.WorkoutDatabase;
import com.example.workoutapp.viewmodel.WorkoutRecordViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UploadWorker extends Worker {
    private WorkoutRecordDao workoutRecordDao;

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        workoutRecordDao = WorkoutDatabase.getDatabase(context).workoutRecordDao();

    }

    @NonNull
    @Override
    public Result doWork() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get the WorkoutRecord data here
        List<WorkoutRecord> workoutRecords = workoutRecordDao.getAllWorkoutRecordsSync();

        // Get current user ID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = (user != null) ? user.getUid() : null;

        // Write workoutRecords to Firebase
        for (WorkoutRecord workoutRecord : workoutRecords) {

            if (uid != null) {
                workoutRecord.setUserId(uid);
            }

            databaseReference.child("workout_records").push().setValue(workoutRecord);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date());
        Log.d("UploadWorker", "doWork method called at " + formattedDate);

        return Result.success();
    }
}
