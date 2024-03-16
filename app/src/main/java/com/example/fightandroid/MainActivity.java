package com.example.fightandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.fightandroid.activity.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btnStart=findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });


    }
}