package com.example.fightandroid.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fightandroid.R;
import com.example.fightandroid.api.ApiClient;
import com.example.fightandroid.api.RetrofitAuth;
import com.example.fightandroid.request.LoginRequest;
import com.example.fightandroid.response.LoginResponse;
import com.example.fightandroid.util.Helper;
import com.example.fightandroid.util.SharedPreferencesManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private Button btnLogin;
    private TextView tvSignup;
    private EditText username,password;


    @Override
    public void setUp() {

    }

    public void login(LoginRequest loginRequest){
        Dialog dialog= Helper.createDialogLoad(LoginActivity.this);
        dialog.show();

        ApiClient apiClient= RetrofitAuth.getInstance().getRetrofit().create(ApiClient.class);
        apiClient.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    LoginResponse loginResponse=response.body();
                    SharedPreferencesManager sharedPreferencesManager=new SharedPreferencesManager(LoginActivity.this);
                    sharedPreferencesManager.saveUserData(loginResponse);
                    Intent intent=new Intent(LoginActivity.this, AppActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else {
                    Helper.handlerErrorResponse(response,LoginActivity.this);
                }

            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Helper.showDialog("Error server!",LoginActivity.this);

            }
        });


    }

    @Override
    public void initComponents() {
        btnLogin=findViewById(R.id.btnLogin);
        tvSignup=findViewById(R.id.tvSignup);
        username=findViewById(R.id.editUsernameLogin);
        password=findViewById(R.id.editPasswordLogin);
        tvSignup.setOnClickListener(v -> {
            Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(v -> {
            String u= username.getText().toString().trim();
            String p= password.getText().toString().trim();
            if(u.equals("") || p.equals("")){
                Helper.showDialog("Không được để trống!",LoginActivity.this);
                return;
            }
            LoginRequest loginRequest=new LoginRequest(u,p);
            login(loginRequest);



        });
    }

    @Override
    public void getDataFromIntent() {
        Intent intent=getIntent();
        if(intent!=null){
            String message=intent.getStringExtra("message");
            if(message!=null){
                Helper.showDialog(message,this);
            }

        }

    }

    @Override
    public void loadUser() {

    }

    @Override
    public int getResourcesLayout() {
        return R.layout.activity_login;
    }
}
