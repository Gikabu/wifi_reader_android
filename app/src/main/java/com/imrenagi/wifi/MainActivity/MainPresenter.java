package com.imrenagi.wifi.MainActivity;

/**
 * Created by imre on 1/30/15.
 */
public interface MainPresenter {

    public boolean isWifiEnabled();

    public void setWifiEnable(boolean enable);

    public void startScanningWifiAround();

    public void onResume();

    public void onPause();
}
