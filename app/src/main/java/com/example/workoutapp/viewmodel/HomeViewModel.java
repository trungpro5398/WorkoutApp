package com.example.workoutapp.viewmodel;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.repository.UserRepository;

public class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<String> name;
    private UserRepository userRepository;
    public HomeViewModel (@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getUserRepository();
        name = userRepository.getUserName();
    }

    public Pair<String,String> getCaloriesText(Integer calories){
        if( calories > 1000 ){
            Float kcal = calories/1000f;
            String strKcal = Float.toString(kcal);
            return new Pair<>(strKcal, "Kcal");
        }
        return new Pair<>(Integer.toString(calories), "Cal");
    }

    public MutableLiveData<String> getUserName() {
        return name;
    }


}
