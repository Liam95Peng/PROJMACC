package com.example.projmacc.Home

import com.example.projmacc.LoginCall
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmailLogOutClient {
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

        fun getUserService(): LogOutCall {
            val logoutCall: LogOutCall = getRetrofit().create(LogOutCall::class.java)
            return logoutCall
        }
    }
}