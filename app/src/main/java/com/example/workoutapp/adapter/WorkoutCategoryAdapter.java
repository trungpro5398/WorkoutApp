package com.example.workoutapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.entity.WorkoutType;

import java.util.ArrayList;
import java.util.List;

public class WorkoutCategoryAdapter extends RecyclerView.Adapter<WorkoutCategoryAdapter.WorkoutCategoryHolder> {

    private List<Workout> workoutCategories = new ArrayList<>();
    private OnItemClickListener listener;
    private List<WorkoutType> workoutTypes = new ArrayList<>();

    @NonNull
    @Override
    public WorkoutCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_category_item, parent, false);
        return new WorkoutCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutCategoryHolder holder, int position) {
        WorkoutType currentWorkoutType = workoutTypes.get(position);
        holder.workoutTypeTextView.setText(currentWorkoutType.getType());
    }

    @Override
    public int getItemCount() {
        return workoutTypes.size();
    }

    public void setWorkoutTypeList(List<WorkoutType> workoutTypes) {
        this.workoutTypes = workoutTypes;
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
                    listener.onItemClick(workoutTypes.get(position));
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
