package com.example.workoutapp.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.example.workoutapp.model.WorkoutType;
import com.example.workoutapp.utils.WorkoutUtils;
import com.example.workoutapp.viewmodel.WorkoutRecordViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AnalyticalFragment extends Fragment {

    private WorkoutRecordViewModel workoutRecordViewModel;
    private WorkoutRecordAdapter adapter;
    private AnalyticalFragmentBinding binding;

    private TabLayout.OnTabSelectedListener tabListener;

    private String CALORIES_GOAL = "1000";
    private String dailyDurationGoal = "20";

    private String chartType = "";

    public AnalyticalFragment() {

    }

    final static HashMap<Float, String> getXAxisLabelMap(List<String> list) {
        HashMap<Float, String> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(Float.valueOf(i), list.get(i));
        }
        return map;
    }

    private void updateCharts(){
        if(workoutRecordViewModel.getSelectedDayTotalDuration() > 0){
            List<WorkoutRecord> workoutRecordList = workoutRecordViewModel.getAllWorkoutTypeDurations();
            if (workoutRecordList != null && workoutRecordList.size() > 0) {
                createBarChart(workoutRecordList);
                createPieChart(workoutRecordList);
            }
        }
        else{
            Log.d("TAG", "updateCharts: no data to chart");
        }
    }

    private void createBarChart(List<WorkoutRecord> workoutRecordList) {

            List<BarEntry> entries = new ArrayList<>();
            List<String> workoutTypes = new ArrayList<>(workoutRecordList.size());
            for (int i = 0; i < workoutRecordList.size(); i++) {
                WorkoutRecord record = workoutRecordList.get(i);
                entries.add(new BarEntry(Float.valueOf(i), Float.parseFloat(record.getWorkoutDuration())));
                workoutTypes.add(i, record.getWorkoutType());
            }
            ;
            HashMap<Float, String> xAxisLabelMap = getXAxisLabelMap(workoutTypes);

//        String label = String.format("Time %s/%s min", workoutRecordViewModel.getSelectedDayTotalDuration(), dailyDurationGoal);
            try {


                BarDataSet set = new BarDataSet(entries, "BarDataSet");
                set.setValueTextSize(12f);
                set.setValueTextColor(Color.WHITE);
                BarData data = new BarData(set);
                data.setBarWidth(1f); // set custom bar width
                BarChart barChart = binding.barChart;
                barChart.setData(data);

                barChart.setFitBars(true); // make the x-axis fit exactly all bars
                Legend legend = barChart.getLegend();
                legend.setEntries(new ArrayList<>());
                new LegendEntry();
                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setTextSize(15f);
                yAxis.setTextColor(Color.WHITE);

                XAxis xaxis = barChart.getXAxis();
                xaxis.setTextColor(Color.WHITE);
                xaxis.setTextSize(15f);

                xaxis.setGranularityEnabled(true);
                xaxis.setGranularity(1f);
                xaxis.setAxisMaximum(workoutTypes.size());
                xaxis.setValueFormatter(new IndexAxisValueFormatter(workoutTypes));
//            xaxis.setValueFormatter(new ValueFormatter() {
//                @Override
//                public String getFormattedValue(float value) {
//                    if (value >= 0 && value <= xAxisLabelMap.size()  ) {
//                        return xAxisLabelMap.get(value) != null ? xAxisLabelMap.get(value):"" ;
//                    }
//                    return "";
//                }
//            });

//        ValueFormatter xAxisFormatter = new ValueFormatter() {
//            @Override
//            public String getAxisLabel(float value, AxisBase axis) {
//                WorkoutType type = xAxisLabels.get((int) value);
//                return StringUtils.capitalize(type.name().toLowerCase());
//
//            };
//
//        };


                XAxis xAxis = barChart.getXAxis();
                xAxis.setLabelCount(workoutTypes.size());


                barChart.invalidate(); // refresh

//        BarChart barChartP = new BarChart(getContext());}
            } catch (Exception e) {
                Log.d("TAG", "createBarChart: Not good");
            }


    }

    private void createPieChart(List<WorkoutRecord> workoutRecords ) {
        List<PieEntry> entries = new ArrayList<>();
        workoutRecords.forEach(record -> {
                entries.add(new PieEntry(Float.parseFloat(record.getWorkoutDuration()), record.getWorkoutType()));
            });
            String label = String.format("Time %s/%s min", workoutRecordViewModel.getSelectedDayTotalDuration(), dailyDurationGoal);
            PieDataSet set = new PieDataSet(entries, label);
            PieData data = new PieData(set);
            PieChart pieChart = binding.chart;
            pieChart.setData(data);

            pieChart.invalidate();

//            pieChart.getLegend()

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        binding = AnalyticalFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        adapter = new WorkoutRecordAdapter();
        binding.workoutRecordRecyclerView.setAdapter(adapter);
        binding.workoutRecordRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        workoutRecordViewModel = new ViewModelProvider(requireActivity()).get(WorkoutRecordViewModel.class);
        subscribeToWorkoutRecordViewModel();
        addCalendarSelectListener();

        binding.calendarView.setDate(WorkoutUtils.parseDateToMs(workoutRecordViewModel.getSelectedDate().getValue(), "yyyy-MM-dd"));
        addTabListener();

        updateCharts();
        updateTotalCalories();
        updateTotalDuration();
        return view;

    }

    private static final int BAR_CHART = 1;

    private void addTabListener() {
        tabListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position) {
                    case BAR_CHART:
                        binding.chart.setVisibility(View.GONE);
                        binding.barChart.setVisibility(View.VISIBLE);
                        break;
                    default:
                        chartType = "Pie Chart";
                        binding.chart.setVisibility(View.VISIBLE);
                        binding.barChart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        binding.chartTabSelector.addOnTabSelectedListener(tabListener);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.chartTabSelector.removeOnTabSelectedListener(tabListener);
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    private void updateTotalCalories() {
        String calText = workoutRecordViewModel.getSelectedDayTotalCalories() + "/" + CALORIES_GOAL + "cal";
        binding.tvCalories.setText(calText);
    }

    private void updateTotalDuration() {
        Integer duration = workoutRecordViewModel.getSelectedDayTotalDuration();
        binding.tvDuration.setText(duration + "/" + dailyDurationGoal + " min");
    }

    public void subscribeToWorkoutRecordViewModel() {
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
                updateTotalCalories();
                updateTotalDuration();
                adapter.setWorkoutRecords(workoutRecordViewModel.getAllWorkoutTypeDurations());
                // Refresh charts
                updateCharts();

            }
        });

    }

    public void addCalendarSelectListener() {

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {


            /*
            The function that triggers when the date is changed.
             */

            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                String selectedDate = String.format(Locale.US, "%d-%02d-%02d", y, m + 1, d);
                workoutRecordViewModel.setSelectedDate(selectedDate);
            }

        });
    }

}


