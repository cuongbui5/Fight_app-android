package com.example.fightandroid.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.example.fightandroid.util.Helper;

public class BroadcastReceiverInternet extends BroadcastReceiver {
    private boolean check=true;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if(!check){
                Helper.showDialog("Có mạng rồi!",context);
            }

        } else {
            Helper.showDialog("Mất mạng rồi!",context);
            check=false;
        }

    }
}
