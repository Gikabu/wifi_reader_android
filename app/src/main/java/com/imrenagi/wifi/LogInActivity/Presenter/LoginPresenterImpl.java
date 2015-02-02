package com.imrenagi.wifi.LogInActivity.Presenter;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;

import com.imrenagi.wifi.LogInActivity.Model.LoginInteractor;
import com.imrenagi.wifi.LogInActivity.Model.LoginInteractorImpl;
import com.imrenagi.wifi.LogInActivity.Model.OnLoginFinishedListener;
import com.imrenagi.wifi.LogInActivity.Model.ReceiverRegistration;
import com.imrenagi.wifi.LogInActivity.View.LoginView;
import com.imrenagi.wifi.Universal.ServiceHandler;

/**
 * Created by imre on 2/2/15.
 */
public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener, ReceiverRegistration {

    private LoginView loginView;
    private LoginInteractor loginInteractor;
    private ScanResult scanResult;
    private ServiceHandler serviceHandler;


    public LoginPresenterImpl(LoginView loginView, ServiceHandler serviceHandler) {
        this.loginView = loginView;
        this.serviceHandler = serviceHandler;

        loginInteractor = new LoginInteractorImpl(serviceHandler, this, this);
    }

    @Override
    public void onConnected() {
        loginView.createAlertDialog("Connected", "You are already connected to wireless network.");
    }

    @Override
    public void onAuthenticationError() {
        loginView.createAlertDialog("Authentication Failed", "You entered wrong authentication.");
    }

    @Override
    public void validateWepOrWpaPassword(String password) {
        loginInteractor.connect(scanResult, password, this);
    }

    @Override
    public void disconnect() {
        loginInteractor.disconnect();
    }

    @Override
    public void setSelectedWifi(ScanResult scanResult) {
        this.scanResult = scanResult;
        loginView.setSSIDName(scanResult.SSID);
    }

    @Override
    public void onResume() {
        prepareTextField();
        loginInteractor.onResume();
    }

    @Override
    public void onDestroy() {
        loginInteractor.onDestroy();
    }

    private void prepareTextField() {
        if (scanResult.capabilities.contains("WPA") && scanResult.capabilities.contains("EAP")) {
            loginView.showUserNameField();
            loginView.showPasswordField();
        } else if (scanResult.capabilities.contains("WPA") || scanResult.capabilities.contains("WEP")) {
            loginView.showPasswordField();
            loginView.hideUserNameField();
        } else {
            loginView.hideUserNameField();
            loginView.hidePasswordField();
        }
    }

    @Override
    public void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {

    }

    @Override
    public void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver) {

    }
}
