package com.example.projmacc.Register;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiCall {
    //UserSevice -> ApiCall

    @PUT("updateGoogle/")
    Call<RegisterResponse> updateGoogleUser(@Body GoogleUpdateRequest registRequest);

    @POST("createEmail/")
    Call<RegisterResponse> createEmail(@Body  EmailRequest emailRequest);
}
