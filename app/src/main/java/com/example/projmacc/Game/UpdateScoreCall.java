package com.example.projmacc.Game;

import com.example.projmacc.Register.GoogleUpdateRequest;
import com.example.projmacc.Register.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface UpdateScoreCall {
    @PUT("updateGoogleScore/")
    Call<GoogleUpdateResponse> updateGoogleScore(@Body GoogleScoreRequest updateScoreRequest);

    @PUT("updateEmailScore/")
    Call<UpdateScoreResponse> updateEmailScore(@Body UpdateScoreRequest updateScoreRequest);
}
