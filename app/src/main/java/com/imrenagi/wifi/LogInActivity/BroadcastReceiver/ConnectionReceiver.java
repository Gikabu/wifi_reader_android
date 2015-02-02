package com.imrenagi.wifi.LogInActivity.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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

    }
}
