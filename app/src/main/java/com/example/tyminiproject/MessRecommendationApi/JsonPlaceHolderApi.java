package com.example.tyminiproject.MessRecommendationApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @GET("/mess")
    Call<List<Mess>> getPosts(
            @Query("title") String title);

}
