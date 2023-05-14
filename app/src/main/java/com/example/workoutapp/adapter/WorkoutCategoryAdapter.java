package com.example.workoutapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.entity.WorkoutType;

import java.util.ArrayList;
import java.util.List;

public class WorkoutCategoryAdapter extends RecyclerView.Adapter<WorkoutCategoryAdapter.WorkoutCategoryHolder> {

    private OnItemClickListener listener;
    private List<WorkoutType> workoutTypes = new ArrayList<>();
    private List<WorkoutType> filteredWorkoutTypes;

    public WorkoutCategoryAdapter() {
        this.filteredWorkoutTypes = new ArrayList<>(workoutTypes);
    }

    @NonNull
    @Override
    public WorkoutCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_category_item, parent, false);
        return new WorkoutCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutCategoryHolder holder, int position) {
        WorkoutType currentWorkoutType = filteredWorkoutTypes.get(position);
        holder.workoutTypeTextView.setText(currentWorkoutType.getType());
    }

    @Override
    public int getItemCount() {
        return filteredWorkoutTypes.size();
    }

    public void setWorkoutTypeList(List<WorkoutType> workoutTypes) {
        this.workoutTypes = workoutTypes;
        this.filteredWorkoutTypes = new ArrayList<>(workoutTypes);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        filteredWorkoutTypes.clear();

        if (query.isEmpty()) {
            filteredWorkoutTypes.addAll(workoutTypes);
        } else {
            for (WorkoutType workoutType : workoutTypes) {
                if (workoutType.getType().toLowerCase().contains(query.toLowerCase())) {
                    filteredWorkoutTypes.add(workoutType);
                }
            }
        }

        notifyDataSetChanged();
    }

    public class WorkoutCategoryHolder extends RecyclerView.ViewHolder {
        private TextView workoutTypeTextView;

        public WorkoutCategoryHolder(@NonNull View itemView) {
            super(itemView);
            workoutTypeTextView = itemView.findViewById(R.id.workout_type_text_view);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(filteredWorkoutTypes.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(WorkoutType workoutType);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
