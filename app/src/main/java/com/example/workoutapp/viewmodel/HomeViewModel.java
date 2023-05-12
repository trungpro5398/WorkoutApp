package com.example.workoutapp.viewmodel;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class HomeViewModel extends AndroidViewModel {
    public HomeViewModel (@NonNull Application application) {
        super(application);
    }

    public Pair<String,String> getCaloriesText(Integer calories){
        if( calories > 1000 ){
            Float kcal = calories/1000f;
            String strKcal = Float.toString(kcal);
            return new Pair<>(strKcal, "Kcal");
        }
        return new Pair<>(Integer.toString(calories), "Cal");
    }


}
