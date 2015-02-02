package com.imrenagi.wifi.LogInActivity.Model;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

/**
 * Created by imre on 2/2/15.
 */
public interface ReceiverRegistration {

    public void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter);
    public void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver);
}
