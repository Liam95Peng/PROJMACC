package com.example.projmacc.Home;


import com.example.projmacc.LoginResponse;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface LogOutCall {

    @PUT("logOut/")
    Call<LoginResponse> logOutCall(@Body EmailLogOutRequest registRequest);
}
