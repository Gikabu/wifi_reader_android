package com.imrenagi.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends ActionBarActivity {


    private static final String INT_PRIVATE_KEY = "private_key";
    private static final String INT_PHASE2 = "phase2";
    private static final String INT_PASSWORD = "password";
    private static final String INT_IDENTITY = "identity";
    private static final String INT_EAP = "eap";
    private static final String INT_CLIENT_CERT = "client_cert";
    private static final String INT_CA_CERT = "ca_cert";
    private static final String INT_ANONYMOUS_IDENTITY = "anonymous_identity";
    final String INT_ENTERPRISEFIELD_NAME = "android.net.wifi.WifiConfiguration$EnterpriseField";

    @InjectView(R.id.ssid_name)
    TextView mSsidLabel;
    @InjectView(R.id.pass_field)
    EditText mPasswordField;
    @InjectView(R.id.connect)
    Button mConnectButton;
    @InjectView(R.id.disconnect)
    Button mDisconnectButton;
    @InjectView(R.id.pass_label)
    TextView mPasswordLabel;
    @InjectView(R.id.user_section)
    LinearLayout mUserSectionLayout;
    @InjectView(R.id.password_section)
    LinearLayout mPasswordSectionLayout;

    private ScanResult mScanResult;
    private WifiManager mWifiManager;

    private BroadcastReceiver mWifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                SupplicantState supl_state = ((SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE));
                switch (supl_state) {
                    case ASSOCIATED:
                        Log.i("SupplicantState", "ASSOCIATED");
                        break;
                    case ASSOCIATING:
                        Log.i("SupplicantState", "ASSOCIATING");
                        break;
                    case AUTHENTICATING:
                        Log.i("SupplicantState", "Authenticating...");
                        break;
                    case COMPLETED:
                        Log.i("SupplicantState", "Connected");
                        break;
                    case DISCONNECTED:
                        Log.i("SupplicantState", "Disconnected");
                        break;
                    case DORMANT:
                        Log.i("SupplicantState", "DORMANT");
                        break;
                    case FOUR_WAY_HANDSHAKE:
                        Log.i("SupplicantState", "FOUR_WAY_HANDSHAKE");
                        break;
                    case GROUP_HANDSHAKE:
                        Log.i("SupplicantState", "GROUP_HANDSHAKE");
                        break;
                    case INACTIVE:
                        Log.i("SupplicantState", "INACTIVE");
                        break;
                    case INTERFACE_DISABLED:
                        Log.i("SupplicantState", "INTERFACE_DISABLED");
                        break;
                    case INVALID:
                        Log.i("SupplicantState", "INVALID");
                        break;
                    case SCANNING:
                        Log.i("SupplicantState", "SCANNING");
                        break;
                    case UNINITIALIZED:
                        Log.i("SupplicantState", "UNINITIALIZED");
                        break;
                    default:
                        Log.i("SupplicantState", "Unknown");
                        break;
                }

                int supl_error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if (supl_error == WifiManager.ERROR_AUTHENTICATING) {
                    Log.i("ERROR_AUTHENTICATING", "ERROR_AUTHENTICATING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            }
        }
    };

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        mScanResult = getIntent().getParcelableExtra("data");

        initializeWifiManager();
        prepareSSIDLabel();
        prepareEditText();
        prepareButton();
    }

    private void prepareSSIDLabel() {
        if (mScanResult != null) {
            mSsidLabel.setText(mScanResult.SSID);
        }
    }

    private void prepareEditText() {
        if (mScanResult.capabilities.contains("WPA") || mScanResult.capabilities.contains("WEP")) {
            mUserSectionLayout.setVisibility(View.GONE);
            mPasswordSectionLayout.setVisibility(View.VISIBLE);
        } else if (mScanResult.capabilities.contains("EAP")) {
            mUserSectionLayout.setVisibility(View.VISIBLE);
            mPasswordSectionLayout.setVisibility(View.VISIBLE);
        } else {
            mUserSectionLayout.setVisibility(View.GONE);
            mPasswordSectionLayout.setVisibility(View.GONE);
        }
    }

    private void initializeWifiManager() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(mWifiStateReceiver, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnReceiver);
        unregisterReceiver(mWifiStateReceiver);
    }

    private WifiConfiguration wpaConfiguration() { //works for WPA or WPA2 authentication
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + mScanResult.SSID + "\"";
        conf.preSharedKey = "\"" + mPasswordField.getText().toString() + "\"";

        return conf;
    }

    private WifiConfiguration wepConfiguration() {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + mScanResult.SSID + "\"";
        conf.hiddenSSID = true;
        conf.status = WifiConfiguration.Status.DISABLED;
        conf.priority = 40;

        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

        conf.wepKeys[0] = "\"" + mPasswordField.getText().toString() + "\"";
        conf.wepTxKeyIndex = 0;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        return conf;
    }

    private WifiConfiguration openNetworkConfiguration() {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + mScanResult.SSID + "\"";
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        return conf;
    }

    private WifiConfiguration WPAEnterpriseConfiguration() {
        WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "icerad";
        wifiConfig.hiddenSSID = true;
        wifiConfig.priority = 40;
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);

        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        enterpriseConfig.setIdentity("user"); //change with wifi userid
        enterpriseConfig.setPassword("password"); //change with password
        enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);

        wifiConfig.enterpriseConfig = enterpriseConfig;

        return wifiConfig;
    }

    private void disconnectWifi() {
        mWifiManager.disconnect();
    }

    private void prepareButton() {
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiConfiguration conf;
                if (mScanResult.capabilities.contains("WPA")) {
                    if (mScanResult.capabilities.contains("EAP")) {
                        conf = WPAEnterpriseConfiguration();
                    } else {
                        conf = wpaConfiguration();
                    }

                } else if (mScanResult.capabilities.contains("WEP")) {
                    conf = wepConfiguration();
                } else {
                    conf = openNetworkConfiguration();
                }

                int netId = mWifiManager.addNetwork(conf);
                mWifiManager.saveConfiguration();
                mWifiManager.enableNetwork(netId, true);
                mWifiManager.reconnect();
            }
        });

        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectWifi();
            }
        });
    }

}
