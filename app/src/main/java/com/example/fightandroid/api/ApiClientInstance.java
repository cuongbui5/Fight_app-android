package com.example.fightandroid.api;

import android.content.Context;

public class ApiClientInstance {
    private static ApiClient instance;
    private static final Object LOCK = new Object();

    public static ApiClient getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = createApiClient(context);
                }
            }
        }
        return instance;
    }

    private static ApiClient createApiClient(Context context) {
        return RetrofitClient.getInstance(context).getRetrofit().create(ApiClient.class);
    }
}
