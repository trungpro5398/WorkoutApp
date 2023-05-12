package com.example.workoutapp.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.workoutapp.adapter.WorkoutRecordAdapter;
import com.example.workoutapp.databinding.AnalyticalFragmentBinding;
import com.example.workoutapp.entity.WorkoutRecord;
import com.example.workoutapp.utils.WorkoutUtils;
import com.example.workoutapp.viewmodel.WorkoutRecordViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AnalyticalFragment extends Fragment {

    private WorkoutRecordViewModel workoutRecordViewModel;
    private WorkoutRecordAdapter adapter;
    private AnalyticalFragmentBinding binding;

    // Pie chart data
    private List<PieEntry> entries = new ArrayList<>();

    private String CALORIES_GOAL = "1000";
    private String dailyDurationGoal = "20";
    public AnalyticalFragment() {
        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));
    }

    private void createBarChart(){
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));
        BarDataSet set = new BarDataSet(entries, "BarDataSet");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        binding.barChart.setData(data);
        binding.barChart.setFitBars(true); // make the x-axis fit exactly all bars
        binding.barChart.invalidate(); // refresh

        BarChart barChartP = new BarChart(getContext());

    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        binding = AnalyticalFragmentBinding.inflate(inflater, container, false);
        PieDataSet set = new PieDataSet(entries, "Calories 600/1000kcal");
        PieData data = new PieData(set);
        PieChart pieChart = binding.chart;
        pieChart.setData(data);
        pieChart.invalidate();


        View view = binding.getRoot();
        adapter = new WorkoutRecordAdapter();
        binding.workoutRecordRecyclerView.setAdapter(adapter);
        binding.workoutRecordRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        workoutRecordViewModel = new ViewModelProvider(requireActivity()).get(WorkoutRecordViewModel.class);

        binding.calendarView.setDate(WorkoutUtils.parseDateToMs(workoutRecordViewModel.getSelectedDate().getValue(), "yyyy-MM-dd"));

        updateTotalCalories();
        updateTotalDuration();
        subscribeToWorkoutRecordViewModel();
        /*
        The calendarSelect method is the method that you'll be working on for everything i assume
         */
        addCalendarSelectListener();
        return view;

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    private void updateTotalCalories(){
        String calText = workoutRecordViewModel.getSelectedDayTotalCalories() + "/" + CALORIES_GOAL + "cal";
        binding.tvCalories.setText(calText);
    }

    private void updateTotalDuration(){
        Integer duration = workoutRecordViewModel.getSelectedDayTotalDuration();
        binding.tvDuration.setText(duration + "/" + dailyDurationGoal + " min");
    }

    public void subscribeToWorkoutRecordViewModel(){
        workoutRecordViewModel.getAllWorkoutRecords().observe(getViewLifecycleOwner(), workoutRecords -> {
                    workoutRecordViewModel.updateSelectedWorkoutRecords();
                }

        );

        workoutRecordViewModel.getSelectedDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String date) {
                workoutRecordViewModel.updateSelectedWorkoutRecords();
            }
        });

        workoutRecordViewModel.getSelectedWorkoutRecords().observe(getViewLifecycleOwner(), new Observer<List<WorkoutRecord>>() {
            @Override
            public void onChanged(List<WorkoutRecord> workoutRecords) {
//                adapter.setWorkoutRecords(workoutRecords);
                // TODO: Differentiate between past and future
                updateTotalCalories();
                updateTotalDuration();
                adapter.setWorkoutRecords(workoutRecordViewModel.getAllWorkoutTypeDurations());

            }
        });

    }
    public void addCalendarSelectListener() {

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {


            /*
            The function that triggers when the date is changed.
             */

            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                String selectedDate = String.format(Locale.US,"%d-%02d-%02d", y, m + 1, d);
                workoutRecordViewModel.setSelectedDate(selectedDate);
            }

        });
    }
}


