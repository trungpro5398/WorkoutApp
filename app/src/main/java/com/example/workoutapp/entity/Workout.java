package com.example.workoutapp.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String videoId;
    private String type;
    // Add a constructor with a type parameter
    public Workout(String title, String videoId, String type) {
        this.title = title;
        this.videoId = videoId;
        this.type = type;
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
}