package com.imrenagi.wifi.LogInActivity.View;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

/**
 * Created by imre on 2/2/15.
 */
public interface LoginView {

    public void showProgress();
    public void hideProgress();

    public void showUserNameField();
    public void hideUserNameField();

    public void showPasswordField();
    public void hidePasswordField();

    public void setPasswordError();
    public void navigateToHome();

    public void setSSIDName(String SSIDName);

    public void createAlertDialog(String title, String message);

    public void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter);
    public void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver);

}
