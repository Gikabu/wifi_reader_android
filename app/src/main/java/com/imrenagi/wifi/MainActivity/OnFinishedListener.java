package com.imrenagi.wifi.MainActivity;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by imre on 1/30/15.
 */
public interface OnFinishedListener {

    public void onFinish(List<ScanResult> scanResults);
}
