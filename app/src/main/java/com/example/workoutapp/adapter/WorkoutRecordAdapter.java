package com.example.workoutapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.entity.WorkoutRecord;

import java.util.ArrayList;
import java.util.List;

public class WorkoutRecordAdapter extends RecyclerView.Adapter <WorkoutRecordAdapter.RecordViewHolder> {
    private List<WorkoutRecord> workoutRecords = new ArrayList<>();


    public void setWorkoutRecords(List<WorkoutRecord> recordList) {
        this.workoutRecords = recordList;
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
        holder.text.setText(record.getWorkoutType() + record.getWorkoutDuration() + record.getWorkoutDate());

    }
    @Override
    public int getItemCount() {
        if (workoutRecords==null) return 0;
        return workoutRecords.size();}

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        RecordViewHolder(@NonNull View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.workout_record_text);
        }
    }

}
