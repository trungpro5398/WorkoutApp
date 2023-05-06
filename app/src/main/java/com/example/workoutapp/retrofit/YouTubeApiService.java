package com.example.workoutapp.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YouTubeApiService {
    @GET("search?part=snippet&type=video&maxResults=100")
    Call<YouTubeResponse> getWorkoutVideos(@Query("q") String workoutType, @Query("key") String apiKey);
}

