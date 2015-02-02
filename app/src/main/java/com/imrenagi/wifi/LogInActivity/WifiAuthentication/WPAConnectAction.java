package com.imrenagi.wifi.LogInActivity.WifiAuthentication;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/**
 * Created by imre on 1/20/15.
 */
public class WPAConnectAction extends NetworkAction {

    private String mWifiKey;

    public WPAConnectAction(String ssid, String key, WifiManager wifiManager) {
        super(ssid, wifiManager);
        this.mWifiKey = key;
    }

    @Override
    public WifiConfiguration createConfiguration() {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + this.getSSID()+ "\"";
        conf.preSharedKey = "\"" + mWifiKey + "\"";
        return conf;
    }
}
