package com.imrenagi.wifi.MainActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.imrenagi.wifi.LogInActivity.LoginActivity;
import com.imrenagi.wifi.LogInActivity.View.LoginScreenActivity;
import com.imrenagi.wifi.R;
import com.imrenagi.wifi.WifiListAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity implements MainView, AdapterView.OnItemClickListener, View.OnClickListener {

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

    private WifiListAdapter wifiListAdapter;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mWifiList.setOnItemClickListener(this);
        mEnabledButton.setOnClickListener(this);

        presenter = new MainPresenterImpl(this);

        prepareTextView();
        prepareButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    private void prepareTextView() {
        if (presenter.isWifiEnabled()) {
            mIsEnabled.setText(getString(R.string.enabled));
        } else {
            mIsEnabled.setText(getString(R.string.disabled));
        }
    }

    private void prepareButton() {
        if (presenter.isWifiEnabled()) {
            mEnabledButton.setText(getString(R.string.turn_off_wifi));
        } else {
            mEnabledButton.setText(getString(R.string.turn_on_wifi));
        }
    }

    @Override
    public void setItems(List<ScanResult> results) {
        wifiListAdapter = new WifiListAdapter(this, R.layout.wifi_item, results);
        mWifiList.setAdapter(wifiListAdapter);
    }

    @Override
    public void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ScanResult scanResult = (ScanResult) parent.getItemAtPosition(position);
        Intent intent = new Intent(MainActivity.this, LoginScreenActivity.class);
        intent.putExtra("data", scanResult);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (presenter.isWifiEnabled()) {
            presenter.setWifiEnable(false);

            mIsEnabled.setText(getString(R.string.disabled));
            mEnabledButton.setText(getString(R.string.turn_on_wifi));

            wifiListAdapter.clear();
        } else {
            presenter.setWifiEnable(true);

            mIsEnabled.setText(getString(R.string.enabled));
            mEnabledButton.setText(getString(R.string.turn_off_wifi));

            presenter.startScanningWifiAround();
        }
    }
}
