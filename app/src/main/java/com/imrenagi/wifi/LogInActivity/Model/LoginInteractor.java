package com.imrenagi.wifi.LogInActivity.Model;

import android.net.wifi.ScanResult;

/**
 * Created by imre on 2/2/15.
 */
public interface LoginInteractor {

    public void onResume();
    public void onDestroy();

    public void connect(ScanResult wifi, String password, OnLoginFinishedListener listener);
    public void disconnect();

}
