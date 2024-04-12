package com.example.fightandroid.api;



import com.example.fightandroid.util.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAuth {
    private static RetrofitAuth instance;
    private final Retrofit retrofit;

    private RetrofitAuth() {
        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public static synchronized RetrofitAuth getInstance() {
        if (instance == null) {
            instance = new RetrofitAuth();
        }
        return instance;
    }
}
