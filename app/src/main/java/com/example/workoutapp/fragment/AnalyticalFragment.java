package com.example.workoutapp.fragment;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AnalyticalFragment extends Fragment {

    private WorkoutRecordViewModel workoutRecordViewModel;
    private WorkoutRecordAdapter adapter;
    private AnalyticalFragmentBinding binding;
    private List<WorkoutRecord> recordList = new ArrayList<>();

    private WeekCalendarView calendarView;

    //private int duration;

    // Pie chart data
    private List<PieEntry> entries = new ArrayList<>();

    public AnalyticalFragment() {
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
        View view = binding.getRoot();
        adapter = new WorkoutRecordAdapter();
        binding.workoutRecordRecyclerView.setAdapter(adapter);
        binding.workoutRecordRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        workoutRecordViewModel = new ViewModelProvider(requireActivity()).get(WorkoutRecordViewModel.class);

        /*
        This observer is just for the recycler view
         */
        workoutRecordViewModel.getAllWorkoutRecords().observe(getViewLifecycleOwner(), workoutRecords -> {
                    adapter.setWorkoutRecords(workoutRecords);
                }

        );
        /*
        The calendarSelect method is the method that you'll be working on for everything i assume
         */
        calendarSelect();
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

    public void calendarSelect() {

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {


            /*
            The function that triggers when the date is changed.
             */
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                String selectedDate = String.format("%d-%02d-%02d", y, m + 1, d);


                // The fancy observer model for this DO NOT EDIT
                workoutRecordViewModel.getAllWorkoutRecords().observe(getViewLifecycleOwner(), new Observer<List<WorkoutRecord>>() {

                    @Override
                    public void onChanged(@Nullable final List<WorkoutRecord> workoutRecords) {
                        /*
                        INSIDE HERE is where I think you'll be doing all the work.
                        Call all of your setters for the dashboards from inside here
                        You'll probably have to declare an int calories = 0 right here
                         */
                        int duration = 0;
                        for (WorkoutRecord r: workoutRecords) {

                            if (r.getWorkoutDate().equals(selectedDate) ) {
                                String d = r.getWorkoutDuration();
                                /*
                                Past here is the bit where each record is checked by date
                                so you'll prob be putting in the logic for the calorie calculation.
                                 */

                                if (d.contains("hr")) {
                                    int nd = Integer.valueOf(d.replace("hr", ""));
                                    nd = nd * 60;
                                    duration = duration + nd;
                                }
                                else if (d.contains("min")) {
                                    int nd = Integer.valueOf(d.replace("min", ""));
                                    duration = duration + nd;
                                }

                                /*
                                For example
                                String t = r.getWorkoutType();

                                if (t.equals("Running") {
                                    calories = hourlyRunningCals * (nd/60)
                                    }
                                etc etc
                                 */
                            }

                        }
                        String dailyDurationGoal = "20";
                        binding.tvDuration.setText(duration + "/" + dailyDurationGoal + " min");

                        /*
                        This is where you'll be doing the setters for the progress bar things same as
                        I did for tvDuration
                         */
                    }});
    }

     });
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