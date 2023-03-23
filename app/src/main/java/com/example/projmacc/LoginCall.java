package com.example.projmacc;

import com.example.projmacc.Home.EmailLogOutRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface LoginCall {

    @POST("/createGoogle/")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);


    @PUT("emailLogin/")
    Call<LoginResponse> emailLoginCall(@Body EmailLoginRequest emailLoginRequest);
}
