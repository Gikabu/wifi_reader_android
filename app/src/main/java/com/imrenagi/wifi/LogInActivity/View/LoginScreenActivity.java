package com.imrenagi.wifi.LogInActivity.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imrenagi.wifi.LogInActivity.Presenter.LoginPresenter;
import com.imrenagi.wifi.LogInActivity.Presenter.LoginPresenterImpl;
import com.imrenagi.wifi.R;
import com.imrenagi.wifi.Universal.ServiceHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginScreenActivity extends Activity implements LoginView, ServiceHandler {

    @InjectView(R.id.ssid_label)
    TextView mSsidLabel;
    @InjectView(R.id.ssid_name)
    TextView mSsidName;
    @InjectView(R.id.user_label)
    TextView mUserLabel;
    @InjectView(R.id.user_field)
    EditText mUserField;
    @InjectView(R.id.user_section)
    LinearLayout mUserSection;
    @InjectView(R.id.pass_label)
    TextView mPassLabel;
    @InjectView(R.id.pass_field)
    EditText mPassField;
    @InjectView(R.id.password_section)
    LinearLayout mPasswordSection;
    @InjectView(R.id.connect)
    Button mConnect;
    @InjectView(R.id.disconnect)
    Button mDisconnect;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        presenter = new LoginPresenterImpl(this, this);

        ScanResult wifi = getIntent().getParcelableExtra("data");
        presenter.setSelectedWifi(wifi);

        prepareButtonListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showUserNameField() {
        mUserSection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUserNameField() {
        mUserSection.setVisibility(View.GONE);
    }

    @Override
    public void showPasswordField() {
        mPasswordSection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePasswordField() {
        mPasswordSection.setVisibility(View.GONE);
    }

    @Override
    public void setPasswordError() {
        mPassField.setError("Password can't be empty!");
    }

    @Override
    public void navigateToHome() {

    }

    @Override
    public void setSSIDName(String SSIDName) {
        mSsidName.setText(SSIDName);
    }

    @Override
    public void createAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        unregisterReceiver(broadcastReceiver);
    }

    private void prepareButtonListener() {
        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.validateWepOrWpaPassword(mPassField.getText().toString());
            }
        });

        mDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.disconnect();
            }
        });
    }

    @Override
    public WifiManager getWifiManager() {
        return (WifiManager)getSystemService(WIFI_SERVICE);
    }


}
