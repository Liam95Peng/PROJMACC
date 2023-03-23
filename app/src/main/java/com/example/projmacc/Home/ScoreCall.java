package com.example.projmacc.Home;

import com.example.projmacc.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ScoreCall {

    @GET("getScoreGoogle/")
    Call<GoogleScoreResponse> getScore();
}
