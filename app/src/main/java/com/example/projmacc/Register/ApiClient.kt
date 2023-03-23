package com.example.projmacc.Register


import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {


    companion object {

        fun getRetrofit(): Retrofit{

                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                val okHttpClient: OkHttpClient =
                    Builder().addInterceptor(httpLoggingInterceptor).build()
                val retrofit:Retrofit = Retrofit.Builder()
                     .baseUrl("http://bosilou.pythonanywhere.com/")
                     .addConverterFactory(GsonConverterFactory.create())
                     .client(okHttpClient)
                     .build()
                return retrofit
            }

        //UserSevice -> ApiCall

        fun getUserService(): ApiCall {
            val apiCall: ApiCall = getRetrofit().create(ApiCall::class.java)
            return apiCall
        }
    }
}