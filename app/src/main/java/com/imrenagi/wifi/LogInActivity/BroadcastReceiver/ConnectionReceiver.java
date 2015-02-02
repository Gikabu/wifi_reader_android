package com.imrenagi.wifi.LogInActivity.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.imrenagi.wifi.LogInActivity.Model.OnLoginFinishedListener;

/**
 * Created by imre on 2/2/15.
 */
public class ConnectionReceiver extends BroadcastReceiver {

    OnLoginFinishedListener listener;

    public ConnectionReceiver(OnLoginFinishedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo mWifiNetworkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        if (mWifiNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
            WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
            listener.onConnected(wifiInfo.getSSID());
        } else if (mWifiNetworkInfo.getState() == NetworkInfo.State.CONNECTING) {

        }
    }
}
