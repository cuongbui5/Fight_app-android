package com.example.fightandroid.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fightandroid.broadcast.BroadcastReceiverInternet;

public abstract class BaseActivity extends AppCompatActivity {
    private BroadcastReceiverInternet broadcastReceiverInternet;
    private boolean isReceiverRegistered = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUser();
        setContentView(getResourcesLayout());
        initComponents();
        setUp();
        getDataFromIntent();
        broadcastReceiverInternet = new BroadcastReceiverInternet();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiverInternet, filter);
        isReceiverRegistered=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isReceiverRegistered){
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(broadcastReceiverInternet, filter);
            isReceiverRegistered=true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isReceiverRegistered) {
            unregisterReceiver(broadcastReceiverInternet);
            isReceiverRegistered = false;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isReceiverRegistered){
            unregisterReceiver(broadcastReceiverInternet);
            isReceiverRegistered = false;
        }

    }

    public abstract void setUp();

    public abstract void initComponents();

    public abstract void getDataFromIntent();
    public abstract void loadUser();

    public abstract int getResourcesLayout();
}
