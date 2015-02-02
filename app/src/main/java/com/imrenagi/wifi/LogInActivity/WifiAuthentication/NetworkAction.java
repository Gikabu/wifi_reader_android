package com.imrenagi.wifi.LogInActivity.WifiAuthentication;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/**
 * Created by imre on 1/20/15.
 */
public abstract class NetworkAction {

    private String mSSID;
    public WifiManager mWifiManager;

    public abstract WifiConfiguration createConfiguration();

    public NetworkAction(String ssid, WifiManager wifiManager) {
        this.mSSID = ssid;
        this.mWifiManager = wifiManager;
    }

    public void connect() {
        int netId = mWifiManager.addNetwork(createConfiguration());
        mWifiManager.saveConfiguration();
        mWifiManager.enableNetwork(netId, true);
        mWifiManager.reconnect();
    }

    public void disconnect() {
        mWifiManager.disconnect();
    }

    public String getSSID() {
        return mSSID;
    }
}
