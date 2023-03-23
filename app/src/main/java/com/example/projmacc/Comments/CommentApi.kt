package com.example.projmacc.Comments

import com.example.projmacc.Register.ApiCall
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommentApi {
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

        fun getUserService(): CommentCall {
            val commentCall: CommentCall = getRetrofit().create(CommentCall::class.java)
            return commentCall
        }
    }
}