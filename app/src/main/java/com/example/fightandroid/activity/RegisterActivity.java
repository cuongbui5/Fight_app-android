package com.example.fightandroid.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fightandroid.R;
import com.example.fightandroid.api.ApiClient;
import com.example.fightandroid.api.ApiClientInstance;
import com.example.fightandroid.api.RetrofitAuth;
import com.example.fightandroid.request.RegisterRequest;
import com.example.fightandroid.response.MessageResponse;
import com.example.fightandroid.util.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private EditText username,password,passwordConfirm;
    private TextView tvSignIn;
    private Button btnRegister;

    @Override
    public void setUp() {

    }

    public void register(RegisterRequest registerRequest){
        Dialog dialog= Helper.createDialogLoad(RegisterActivity.this);
        dialog.show();
        ApiClient apiClient= RetrofitAuth.getInstance().getRetrofit().create(ApiClient.class);
        apiClient.register(registerRequest).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    MessageResponse messageResponse=response.body();
                    if(messageResponse!=null){
                        Helper.showDialog(messageResponse.getMessage(),RegisterActivity.this);
                    }


                }else {
                    Helper.handlerErrorResponse(response,RegisterActivity.this);

                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Helper.showDialog("Error server!",RegisterActivity.this);

            }
        });

    }

    @Override
    public void initComponents() {
        username=findViewById(R.id.editUsername);
        password=findViewById(R.id.editPassword);
        passwordConfirm=findViewById(R.id.editPasswordConfirm);
        btnRegister=findViewById(R.id.btnRegister);
        tvSignIn=findViewById(R.id.tvSignin);
        tvSignIn.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> {
            String u=username.getText().toString().trim();
            String p=password.getText().toString().trim();
            String pc=passwordConfirm.getText().toString().trim();
            if(u.equals("")||p.equals("")||pc.equals("")){
                Helper.showDialog("Không được để trống!",RegisterActivity.this);
                return;
            }
            RegisterRequest registerRequest=new RegisterRequest(u,p,pc);
            register(registerRequest);


        });

    }

    @Override
    public void getDataFromIntent() {

    }

    @Override
    public void loadUser() {

    }

    @Override
    public int getResourcesLayout() {
        return R.layout.activity_register;
    }
}
