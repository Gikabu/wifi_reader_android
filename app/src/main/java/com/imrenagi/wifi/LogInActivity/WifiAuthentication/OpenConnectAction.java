package com.imrenagi.wifi.LogInActivity.WifiAuthentication;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/**
 * Created by imre on 1/20/15.
 */
public class OpenConnectAction extends NetworkAction {

    public OpenConnectAction(String ssid, WifiManager wifiManager) {
        super(ssid, wifiManager);
    }

    @Override
    public WifiConfiguration createConfiguration() {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + this.getSSID() + "\"";
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        return conf;
    }
}
