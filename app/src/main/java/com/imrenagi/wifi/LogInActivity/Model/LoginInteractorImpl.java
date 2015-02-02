package com.imrenagi.wifi.LogInActivity.Model;

import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.imrenagi.wifi.LogInActivity.BroadcastReceiver.AuthenticationErrorReceiver;
import com.imrenagi.wifi.LogInActivity.BroadcastReceiver.ConnectionReceiver;
import com.imrenagi.wifi.LogInActivity.WifiAuthentication.OpenConnectAction;
import com.imrenagi.wifi.LogInActivity.WifiAuthentication.WEPConnectAction;
import com.imrenagi.wifi.LogInActivity.WifiAuthentication.WPAConnectAction;
import com.imrenagi.wifi.Universal.ServiceHandler;

/**
 * Created by imre on 2/2/15.
 */
public class LoginInteractorImpl implements LoginInteractor, OnLoginFinishedListener{

    private ServiceHandler serviceHandler;

    private AuthenticationErrorReceiver authenticationErrorReceiver;
    private ConnectionReceiver connectionReceiver;
    private OnLoginFinishedListener listener;
    private ReceiverRegistration receiverRegistration;

    public LoginInteractorImpl(ServiceHandler serviceHandler, OnLoginFinishedListener listener, ReceiverRegistration receiverRegistration) {
        this.serviceHandler = serviceHandler;
        this.listener = listener;

        authenticationErrorReceiver = new AuthenticationErrorReceiver(this);
        connectionReceiver = new ConnectionReceiver(this);
    }

    @Override
    public void onResume() {
        receiverRegistration.registerBroadcastReceiver(authenticationErrorReceiver, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
        receiverRegistration.registerBroadcastReceiver(connectionReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
    }

    @Override
    public void onDestroy() {
        receiverRegistration.unregisterBroadcastReceiver(authenticationErrorReceiver);
        receiverRegistration.unregisterBroadcastReceiver(connectionReceiver);
    }

    @Override
    public void connect(ScanResult wifi, String password, OnLoginFinishedListener listener) {
        if (wifi.capabilities.contains("WPA") && wifi.capabilities.contains("WEP")) {

        } else if (wifi.capabilities.contains("WPA")) {
            WPAConnectAction wpaConnectAction = new WPAConnectAction(wifi.SSID, password, serviceHandler.getWifiManager());
            wpaConnectAction.connect();
        } else if (wifi.capabilities.contains("WEP")) {
            WEPConnectAction wepConnectAction = new WEPConnectAction(wifi.SSID, password, serviceHandler.getWifiManager());
            wepConnectAction.connect();
        } else {
            OpenConnectAction openConnectAction = new OpenConnectAction(wifi.SSID, serviceHandler.getWifiManager());
            openConnectAction.connect();
        }
    }

    @Override
    public void disconnect() {
        serviceHandler.getWifiManager().disconnect();
    }

    @Override
    public void onConnected() {
        listener.onConnected();
    }

    @Override
    public void onAuthenticationError() {
        listener.onAuthenticationError();
    }
}
