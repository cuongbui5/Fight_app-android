package com.example.fightandroid.api;

import android.content.Context;


import com.example.fightandroid.api.interceptor.TokenInterceptor;
import com.example.fightandroid.util.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance;
    private final Retrofit retrofit;

    private RetrofitClient(Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(context));
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }








}
