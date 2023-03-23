package com.example.projmacc

import com.example.projmacc.Register.ApiCall
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GoogleApiClient {

    companion object {

        fun getRetrofit(): Retrofit {

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient: OkHttpClient =
                OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://bosilou.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit
        }

        //UserSevice -> ApiCall

        fun getUserService(): LoginCall {
            val loginCall: LoginCall = getRetrofit().create(LoginCall::class.java)
            return loginCall
        }
    }
}