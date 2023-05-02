package com.example.workoutapp.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YouTubeApiService {
    @GET("search?part=snippet&type=video&maxResults=10&q=workout")
    Call<YouTubeResponse> getWorkoutVideos(@Query("key") String apiKey);
}
