package com.example.workoutapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.entity.WorkoutRecord;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class WorkoutRecordAdapter extends RecyclerView.Adapter<WorkoutRecordAdapter.RecordViewHolder> {
    private List<WorkoutRecord> workoutRecords = new ArrayList<>();


    public void setWorkoutRecords(List<WorkoutRecord> recordList) {
        this.workoutRecords = recordList;
        notifyDataSetChanged();
    }

    @Override
    public WorkoutRecordAdapter.RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_record_item, parent, false);
        return new RecordViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        WorkoutRecord record = workoutRecords.get(position);
        String workoutType = StringUtils.capitalize(record.getWorkoutType().toLowerCase());
        holder.workoutTypeText.setText(workoutType + " " + record.getWorkoutDate());
        holder.durationText.setText(record.getWorkoutDuration() + ":00 mins");
    }

    @Override
    public int getItemCount() {
        if (workoutRecords == null) return 0;
        return workoutRecords.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView workoutTypeText;
        TextView durationText;

        RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutTypeText = itemView.findViewById(R.id.rc_workout_type);
            durationText = itemView.findViewById(R.id.rc_workout_duration);
        }
    }

}
