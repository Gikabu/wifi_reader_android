package com.imrenagi.wifi.MainActivity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by imre on 1/30/15.
 */
public class MainPresenterImpl implements MainPresenter, OnFinishedListener {

    MainView mainView;
    WifiManager mWifiManager;

    ScanResultReceiver scanResultReceiver;

    public MainPresenterImpl(MainView mainView) {
        scanResultReceiver = new ScanResultReceiver(this);

        this.mainView = mainView;

        mWifiManager = (WifiManager)((Context)mainView).getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    @Override
    public void setWifiEnable(boolean enable) {
        mWifiManager.setWifiEnabled(enable);
    }

    @Override
    public void startScanningWifiAround() {
        mWifiManager.startScan();
    }

    @Override
    public void onResume() {
        mainView.registerBroadcastReceiver(scanResultReceiver,  new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        startScanningWifiAround();
    }

    @Override
    public void onPause() {
        mainView.unregisterBroadcastReceiver(scanResultReceiver);
    }

    @Override
    public void onFinish(List<ScanResult> scanResults) {
        mainView.setItems(scanResults);
    }
}
