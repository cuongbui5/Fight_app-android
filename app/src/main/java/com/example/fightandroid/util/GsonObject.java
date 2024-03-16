package com.example.fightandroid.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonObject {
    private static Gson gson;
    public static Gson instance() {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        return gson;
    }

}
