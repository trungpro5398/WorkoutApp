package com.example.workoutapp.fragment;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.workoutapp.R;
import com.example.workoutapp.adapter.WorkoutRecordAdapter;
import com.example.workoutapp.databinding.AnalyticalFragmentBinding;
import com.example.workoutapp.entity.Workout;
import com.example.workoutapp.entity.WorkoutRecord;
import com.example.workoutapp.viewmodel.WorkoutRecordViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.WeekDay;
import com.kizitonwose.calendar.view.ViewContainer;
import com.kizitonwose.calendar.view.WeekCalendarView;
import com.kizitonwose.calendar.view.WeekDayBinder;

import java.util.ArrayList;
import java.util.List;

public class AnalyticalFragment extends Fragment {

    private WorkoutRecordViewModel workoutRecordViewModel;
    private WorkoutRecordAdapter adapter;
    private AnalyticalFragmentBinding binding;
    private List<WorkoutRecord> recordList;

    private WeekCalendarView calendarView;

    // Pie chart data
    private List<PieEntry> entries = new ArrayList<>();

    public AnalyticalFragment(){
        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        binding = AnalyticalFragmentBinding.inflate(inflater, container, false);
        PieDataSet set = new PieDataSet(entries, "Calories 600/1000kcal");
        PieData data = new PieData(set);
        PieChart pieChart = binding.chart;
        pieChart.setData(data);
        pieChart.invalidate();

        calendarView = binding.weekCalendarView;
//        calendarView.setDayBinder(new WeekDayBinder<DayViewContainer>() {
//            @NonNull
//            @Override
//            public DayViewContainer create(@NonNull View view) {
//                return new DayViewContainer(view);
//            }
//
//            @Override
//            public void bind(@NonNull DayViewContainer container, WeekDay weekDay) {
//                container.day = weekDay;
//                container.textView.setText((weekDay.getDate().getDayOfMonth()));
//
//            }
//        });
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new WorkoutRecordAdapter();
        binding.workoutRecordRecyclerView.setAdapter(adapter);
        binding.workoutRecordRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        workoutRecordViewModel = new ViewModelProvider(requireActivity()).get(WorkoutRecordViewModel.class);
        workoutRecordViewModel.getAllWorkoutRecords().observe(getViewLifecycleOwner(), workoutRecords -> {
            adapter.setWorkoutRecords(workoutRecords);
            String duration = workoutRecordViewModel.getWorkoutDurationForDate("2023-05-08");
            String dailyDurationGoal = "20";
            binding.tvDuration.setText(duration+ "/"+ dailyDurationGoal + " min");
            }

        );
//        String duration = workoutRecordViewModel.getWorkoutDurationForDate("2023-05-08");
//        String dailyDurationGoal = "20";






    }
    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}

//public class DayViewContainer  {
//     TextView textView;
//     WeekDay day;
//
//    public DayViewContainer(View view) {
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                textView = view.findViewById(R.id.calendarDayText);
//
//            }
//        });
//    }
//}