package com.example.workoutapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.workoutapp.databinding.CalendarFragmentBinding;

public class CalendarFragment extends Fragment {


    private CalendarFragmentBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CalendarFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

//        binding.calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
//                String selectedDate = String.format("%d-%02d-%02d", y, m + 1, d);
//
//            }
//
//
//
//        });

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
