package com.example.workoutapp.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.workoutapp.utils.WorkoutUtils;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "workout")
public class Workout {
    private String imageUrl;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String videoId;
    private String type;
    private String level;
    // Add a constructor with a type parameter
    public Workout(String title, String videoId, String type, String imageUrl, String level) {
        this.title = title;
        this.videoId = videoId;
        this.type = type;
        this.imageUrl = imageUrl;
        this.level = level;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Add a getter and setter for the type attribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

//    public static List<Workout> createDummyNewVideoList() {
//        List<Workout> newVideosList = new ArrayList<>();
//        newVideosList.add(new Workout("Workout Title 1", "jKTxe236-4U", null, "beginner"));
//        newVideosList.add(new Workout("Workout Title 2", "jKTxe236-4U", null, "beginner"));
//        return newVideosList;
//    }
}