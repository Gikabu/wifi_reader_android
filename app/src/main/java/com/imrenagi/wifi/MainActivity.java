package com.imrenagi.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.wifi_status)
    TextView mWifiStatus;
    @InjectView(R.id.wifi_is_enable)
    TextView mIsEnabled;
    @InjectView(R.id.wifi_info)
    TextView mWifiInfo;
    @InjectView(R.id.status_changer)
    Button mEnabledButton;
    @InjectView(R.id.wifi_list)
    ListView mWifiList;

    private List<ScanResult> scanList;
    private WifiManager wifiManager;
    private WifiListAdapter wifiListAdapter;
    private Comparator<ScanResult> comparator;

    private BroadcastReceiver mScanResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            wifiListAdapter.clear();
            scanList = wifiManager.getScanResults();
            Collections.sort(scanList, comparator);
            rearrangeCurrentActiveWifiPosition(scanList);
            wifiListAdapter.addAll(scanList);
        }
    };

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if (noConnectivity) {
                mWifiInfo.setText("Connected To : ");
            }
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
        }
    };

    private BroadcastReceiver mSignalStrengthReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            mWifiInfo.setText("Connected To : " + wifiInfo.getSSID());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        initializeComparator();

        prepareTextView();
        prepareListView();
        prepareButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnReceiver);
        unregisterReceiver(mScanResultReceiver);
        unregisterReceiver(mSignalStrengthReceiver);
    }

    private void rearrangeCurrentActiveWifiPosition(List<ScanResult> scanResults) {
        int activeWifiPosition = currentConnectedWifiPosition(scanResults);
        if (activeWifiPosition != -1) {
            ScanResult tempScan = scanResults.get(activeWifiPosition);
            scanResults.remove(activeWifiPosition);
            scanResults.add(0, tempScan);
        }
    }

    private int currentConnectedWifiPosition(List<ScanResult> results) {
        String str = getCurrentSsid(MainActivity.this.getApplicationContext());
        int currentWifiPosition = -1;
        if (str != null) {
            for (int i = 0; i < results.size(); i++) {
                if (str.equals(results.get(i).SSID)) {
                    currentWifiPosition = i;
                    break;
                }
            }
        }
        return currentWifiPosition;
    }

    private void initializeComparator() {
        comparator = new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                return (lhs.level > rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
            }
        };
    }

    public String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                ssid = connectionInfo.getSSID();
            }
        }
        return (ssid == null) ? ssid : ssid.replace("\"", "");
    }

    private void prepareTextView() {
        if (wifiManager.isWifiEnabled()) {
            mIsEnabled.setText("enabled");
        } else {
            mIsEnabled.setText("disabled");
        }
    }

    private void prepareListView() {
        wifiListAdapter = new WifiListAdapter(this, R.layout.wifi_item, new ArrayList<ScanResult>());
        mWifiList.setAdapter(wifiListAdapter);

        mWifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScanResult scanResult = (ScanResult) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("data", scanResult);
                startActivity(intent);
            }
        });
    }

    private void prepareButton() {
        if (wifiManager.isWifiEnabled()) {
            mEnabledButton.setText("Turn Off Wifi");
        } else {
            mEnabledButton.setText("Turn On Wifi");
        }

        mEnabledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    mIsEnabled.setText("disabled");
                    mEnabledButton.setText("Turn On Wifi");
                    wifiListAdapter.clear();
                } else {
                    wifiManager.setWifiEnabled(true);
                    mIsEnabled.setText("enabled");
                    mEnabledButton.setText("Turn Off Wifi");
                    wifiManager.startScan();
                }
            }
        });
    }

    private void registerReceivers() {
        registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(mSignalStrengthReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        registerReceiver(mScanResultReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }

}
