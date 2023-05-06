package com.example.workoutapp.navigation;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigation {
    private AppCompatActivity mActivity;

    public BottomNavigation(AppCompatActivity activity) {
        mActivity = activity;
    }

    public void setupBottomNavigation(BottomNavigationView.OnNavigationItemSelectedListener listener) {
        BottomNavigationView bottomNavigationView = mActivity.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
    }
}
