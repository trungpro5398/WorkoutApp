package com.example.workoutapp.fragment;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Toast;
import java.text.SimpleDateFormat;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.workoutapp.R;
import com.example.workoutapp.databinding.CalendarFragmentBinding;
import com.example.workoutapp.entity.WorkoutRecord;
import com.example.workoutapp.viewmodel.WorkoutRecordViewModel;
import com.example.workoutapp.viewmodel.WorkoutViewModel;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarFragment extends Fragment {


    private CalendarFragmentBinding binding;
    private WorkoutRecordViewModel workoutRecordViewModel;
    private String wDuration;
    private String wType;
    private String wDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CalendarFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        workoutRecordViewModel = new ViewModelProvider(requireActivity()).get(WorkoutRecordViewModel.class);
        binding.calendar.setMaxDate((new Date()).getTime());
        typeSpinner();
        durationSpinner();
        calendarSelect();

        binding.selectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                WorkoutRecord record = new WorkoutRecord(wType, wDuration, wDate);

                Toast.makeText(getActivity(), "Recorded " + record.getWorkoutType() + " " +
                        record.getWorkoutDuration()+ " " + record.getWorkoutDate(), Toast.LENGTH_LONG).show();

                workoutRecordViewModel.insert(record);
            }
        });



        return view;
    }

    public void typeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.workout_record_types, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.selectWorkoutType.setAdapter(adapter);

        binding.selectWorkoutType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                wType = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void durationSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.workout_record_durations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.selectWorkoutDuration.setAdapter(adapter);

        binding.selectWorkoutDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                wDuration= selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void calendarSelect() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long date = binding.calendar.getDate();
        String initialDate = dateFormat.format(date);
        wDate = initialDate;
        binding.calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                String selectedDate = String.format("%d-%02d-%02d", y, m + 1, d);
                wDate=selectedDate;
    }});
    }

    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding=null;
    }
}
