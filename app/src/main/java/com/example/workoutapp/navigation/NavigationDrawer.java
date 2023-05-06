package com.example.workoutapp.navigation;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.workoutapp.R;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawer {
    private AppCompatActivity mActivity;
    private DrawerLayout mDrawerLayout;

    public NavigationDrawer(AppCompatActivity activity, DrawerLayout drawerLayout) {
        mActivity = activity;
        mDrawerLayout = drawerLayout;
    }

    public void setupNavigation(Toolbar toolbar, NavigationView.OnNavigationItemSelectedListener listener) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = mActivity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(listener);
    }
}