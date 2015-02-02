package com.imrenagi.wifi.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by imre on 1/30/15.
 */
public class ScanResultReceiver extends BroadcastReceiver {

    Context mContext;
    WifiManager mWifiManager;
    List<ScanResult> scanResults;

    OnFinishedListener listener;

    public ScanResultReceiver(OnFinishedListener listener) {
        this.listener = listener;
    }

    Comparator comparator = new Comparator<ScanResult>() {
        @Override
        public int compare(ScanResult lhs, ScanResult rhs) {
            return (lhs.level > rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        scanResults = mWifiManager.getScanResults();

        Collections.sort(scanResults, comparator);

        rearrangeCurrentActiveWifiPosition();

        this.listener.onFinish(scanResults);
    }

    private void rearrangeCurrentActiveWifiPosition() {
        int activeWifiPosition = currentConnectedWifiPosition();
        if (activeWifiPosition != -1) {
            ScanResult tempScan = scanResults.get(activeWifiPosition);
            scanResults.remove(activeWifiPosition);
            scanResults.add(0, tempScan);
        }
    }

    private int currentConnectedWifiPosition() {
        String str = getCurrentSSID();
        int currentWifiPosition = -1;
        if (str != null) {
            for (int i = 0; i < scanResults.size(); i++) {
                if (str.equals(scanResults.get(i).SSID)) {
                    currentWifiPosition = i;
                    break;
                }
            }
        }
        return currentWifiPosition;
    }

    public String getCurrentSSID() {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                ssid = connectionInfo.getSSID();
            }
        }
        return (ssid == null) ? ssid : ssid.replace("\"", "");
    }

    public void clear() {
        mWifiManager = null;
        mContext = null;
        scanResults = null;
    }

}
