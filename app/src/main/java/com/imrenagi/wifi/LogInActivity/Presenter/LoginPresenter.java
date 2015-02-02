package com.imrenagi.wifi.LogInActivity.Presenter;

import android.net.wifi.ScanResult;

/**
 * Created by imre on 2/2/15.
 */
public interface LoginPresenter {

    public void validateWepOrWpaPassword(String password);

    public void disconnect();

    public void setSelectedWifi(ScanResult scanResult);

    public void onResume();
    public void onDestroy();

}
