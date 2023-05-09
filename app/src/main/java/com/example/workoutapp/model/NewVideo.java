package com.example.workoutapp.model;

import java.util.ArrayList;
import java.util.List;

public class NewVideo {
    private String mTitle;
    private String mUrl;

    private String mThumbnail;
    public NewVideo(String title, String url) {
        mTitle = title;
        mUrl = url;
    }
    public String getTitle() {
        return mTitle;
    }
    public String getUrl() {
        return mUrl;
    }
    //this is used to populate the list with a few items at the start of the application
//it is static so it can be called without instantiating the class
    public static List<NewVideo> createNewVideoList() {
        List<NewVideo> newVideosList = new ArrayList<>();
        newVideosList.add(new NewVideo( "Workout Title 1","https://www.youtube.com/watch?v=jKTxe236-4U"));
        newVideosList.add(new NewVideo( "Workout Title 2","https://www.youtube.com/watch?v=jKTxe236-4U"));
        return newVideosList;
    }
}
