package com.example.workoutapp.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.workoutapp.R;
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

import java.util.Arrays;
import java.util.Date;
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
            binding.barChart.setData(null);
            binding.barChart.notifyDataSetChanged();
            binding.barChart.invalidate();
            binding.chart.setData(null);
            binding.chart.notifyDataSetChanged();
            binding.chart.invalidate();

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
//            HashMap<Float, String> xAxisLabelMap = getXAxisLabelMap(workoutTypes);


            try {

                String label = String.format("Active Time \n%s/%s min", workoutRecordViewModel.getSelectedDayTotalDuration(), dailyDurationGoal);
                BarDataSet set = new BarDataSet(entries, label);
                set.setColors();
                set.setValueTextSize(15f);
                set.setValueTextColor(Color.WHITE);
                set.setColors( getColor(R.color.orange));


                BarData data = new BarData(set);
                data.setBarWidth(0.5f); // set custom bar width
                data.setValueTextColor(getColor(R.color.white));

                BarChart barChart = binding.barChart;
                barChart.setData(data);
                barChart.notifyDataSetChanged();
                barChart.setFitBars(true); // make the x-axis fit exactly all bars

                Legend legend = barChart.getLegend();
                legend.setEntries(new ArrayList<>());
                legend.setTextColor(getColor(R.color.white));
                legend.setTextSize(15f);
                legend.setWordWrapEnabled(true);

                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setTextSize(15f);
                yAxis.setTextColor(Color.WHITE);
                yAxis.setGranularity(10f);
                yAxis.setAxisMinimum(0f);
                barChart.setAutoScaleMinMaxEnabled(true);

                XAxis xaxis = barChart.getXAxis();
                xaxis.setTextColor(Color.WHITE);
                xaxis.setTextSize(15f);

                xaxis.setGranularityEnabled(true);
                xaxis.setGranularity(1f);
                xaxis.setAxisMaximum(workoutTypes.size());
                xaxis.setValueFormatter(new IndexAxisValueFormatter(workoutTypes));
                xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xaxis.setLabelRotationAngle(45f);
                xaxis.setLabelCount(workoutTypes.size());
                barChart.setHorizontalScrollBarEnabled(true);



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
//


                barChart.invalidate(); // refresh

//        BarChart barChartP = new BarChart(getContext());}
            } catch (NullPointerException e) {
                Log.d("TAG", "createBarChart: Not good");
                binding.barChart.setData(null);
                binding.barChart.notifyDataSetChanged();
                binding.barChart.invalidate();
            }


    }

    @ColorInt
    private int getColor(@ColorRes int id) {
       return ContextCompat.getColor(getContext(), id);
    }
    private void createPieChart(List<WorkoutRecord> workoutRecords ) {
        List<PieEntry> entries = new ArrayList<>();
        workoutRecords.forEach(record -> {
                entries.add(new PieEntry(Float.parseFloat(record.getWorkoutDuration()), record.getWorkoutType()));
            });
            String label = String.format("Active Time \n%s/%s min", workoutRecordViewModel.getSelectedDayTotalDuration(), dailyDurationGoal);
            PieDataSet set = new PieDataSet(entries, label);
            set.setValueTextSize(15f);
            set.setValueTextColor(getColor(R.color.white));

            set.setColors(getColor(R.color.purple_700), getColor(R.color.teal_700), getColor(R.color.orange), Color.BLUE, getColor(R.color.purple_500));

            PieData data = new PieData(set);
            data.setValueTextColor(getColor(R.color.white));
            data.setValueTextSize(15f);

            PieChart pieChart = binding.chart;
            pieChart.setCenterText(label);
            pieChart.setCenterTextColor(getColor(R.color.black));
            pieChart.setCenterTextSize(15f);
            pieChart.setEntryLabelTextSize(15f);

            Legend legend = pieChart.getLegend();
            legend.setTextColor(getColor(R.color.white));
            legend.setTextSize(20f);
            legend.setWordWrapEnabled(true);

            pieChart.setData(data);
            pieChart.notifyDataSetChanged();

            pieChart.invalidate();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        binding = AnalyticalFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Set up record list for recycler view adapter
        adapter = new WorkoutRecordAdapter();
        binding.workoutRecordRecyclerView.setAdapter(adapter);
        binding.workoutRecordRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up view model
        workoutRecordViewModel = new ViewModelProvider(requireActivity()).get(WorkoutRecordViewModel.class);
        subscribeToWorkoutRecordViewModel();

       // Set up calendar listener and ui
        addCalendarSelectListener();
        binding.calendarView.setMaxDate((new Date()).getTime());
        binding.calendarView.setDate(WorkoutUtils.parseDateToMs(workoutRecordViewModel.getSelectedDate().getValue(), "yyyy-MM-dd"));

        // Add tab listener and initial setup
        addTabListener();
        TabLayout chartTabSelector = binding.chartTabSelector;
        tabListener.onTabSelected(chartTabSelector.getTabAt(chartTabSelector.getSelectedTabPosition()));

        // Initial Setup of other UI components
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
        String calText = workoutRecordViewModel.getSelectedDayTotalCalories() + "/" + CALORIES_GOAL + " cal";
        binding.tvCalories.setText(calText);
    }

    private void updateTotalDuration() {
        String text = "Total workout duration: " + workoutRecordViewModel.getSelectedDayTotalDuration() + "/" + dailyDurationGoal + " min";
        binding.durationTextView.setText(text);
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


