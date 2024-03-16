package com.example.fightandroid.api.interceptor;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.fightandroid.activity.LoginActivity;
import com.example.fightandroid.response.LoginResponse;
import com.example.fightandroid.util.Helper;
import com.example.fightandroid.util.SharedPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;


public class TokenInterceptor implements Interceptor {

    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;





    public TokenInterceptor(Context context) {
        this.context=context;
        sharedPreferencesManager=new SharedPreferencesManager(context);


    }



    public void logout(){
        sharedPreferencesManager.clearData();
        Intent intent=new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("message","Phiên đăng nhập hết hạn!");
        context.startActivity(intent);

    }




    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LoginResponse loginResponse=sharedPreferencesManager.getUserData();
        if(loginResponse==null){
            logout();
        }else {
            String token = loginResponse.getToken();
            Log.d("request",request.toString());
            if (!token.isEmpty()) {
                Request newRequest = request.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();

                Response response = chain.proceed(newRequest);
                if (response.code() == 401) {
                    Log.d("token",request.toString());
                    Log.d("token","access token het han");
                    response.close();
                    logout();
                } else {
                    return response;
                }
            }

        }
        return chain.proceed(request);


    }




}
