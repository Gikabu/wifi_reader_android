package com.imrenagi.wifi.LogInActivity.WifiAuthentication;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/**
 * Created by imre on 1/20/15.
 */
public class WEPConnectAction extends NetworkAction {

    private String mWifiKey;

    public WEPConnectAction(String ssid, String key, WifiManager wifiManager) {
        super(ssid, wifiManager);
        mWifiKey = key;
    }

    @Override
    public WifiConfiguration createConfiguration() {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + this.getSSID() + "\"";
        conf.hiddenSSID = true;
        conf.status = WifiConfiguration.Status.DISABLED;
        conf.priority = 40;

        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

        conf.wepKeys[0] = "\"" + mWifiKey + "\"";
        conf.wepTxKeyIndex = 0;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        return conf;
    }
}
