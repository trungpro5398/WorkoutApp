package com.example.workoutapp.entity;

import java.util.ArrayList;
import java.util.List;

public class NewVideo extends Workout {

    public NewVideo(String title, String videoId, String imageUrl, String level) {
        super(title, videoId, null, imageUrl, level);
    }

    //this is used to populate the list with a few items at the start of the application
//it is static so it can be called without instantiating the class
    public static List<NewVideo> createNewVideoList() {
        List<NewVideo> newVideosList = new ArrayList<>();
        newVideosList.add(new NewVideo("Workout Title 1", "jKTxe236-4U", null, "beginner"));
        newVideosList.add(new NewVideo("Workout Title 2", "jKTxe236-4U", null, "beginner"));
        return newVideosList;
    }
}
