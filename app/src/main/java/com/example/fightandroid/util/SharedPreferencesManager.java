package com.example.fightandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.fightandroid.response.LoginResponse;


public class SharedPreferencesManager {
    private static final String PREFERENCE_NAME = "MyAppPreferences";
    private static final String USER_DATA_KEY = "user_data";

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;




    public SharedPreferencesManager(Context context) {
        this.sharedPreferences=context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.editor= sharedPreferences.edit();
    }


    public void saveUserData(LoginResponse loginResponse) {
        String userDataJson = GsonObject.instance().toJson(loginResponse);
        editor.putString(USER_DATA_KEY, userDataJson);
        editor.apply();
    }

    public LoginResponse getUserData() {
        String userDataJson = sharedPreferences.getString(USER_DATA_KEY, null);
        if (userDataJson != null) {
            return GsonObject.instance().fromJson(userDataJson, LoginResponse.class);
        }
        return null;
    }
    public void clearData() {
        editor.remove(USER_DATA_KEY);
        editor.apply();
    }
}

