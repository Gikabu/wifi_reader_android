package com.imrenagi.wifi.MainActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by imre on 1/30/15.
 */
public interface MainView {

    public void setItems(List<ScanResult> results);

    public void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter);

    public void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver);
}
