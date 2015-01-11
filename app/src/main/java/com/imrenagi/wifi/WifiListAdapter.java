package com.imrenagi.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by imrenagi on 10/01/15.
 */
public class WifiListAdapter extends ArrayAdapter<ScanResult> {

    private LayoutInflater mInflater;
    private Context context;
    private int layoutResourceId;
    private List<ScanResult> wifiData;

    public WifiListAdapter(Context context, int layoutResourceId, List<ScanResult> wifiData) {
        super(context, layoutResourceId, wifiData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.wifiData = wifiData;
        this.mInflater = (LayoutInflater) (this.context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return wifiData.size();
    }

    @Override
    public ScanResult getItem(int position) {
        return wifiData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        WifiHolder holder;

        if (row == null) {
            row = mInflater.inflate(layoutResourceId, parent, false);
            holder = new WifiHolder(row);
            row.setTag(holder);
        } else {
            holder = (WifiHolder) row.getTag();
        }

        holder.ssid.setText(wifiData.get(position).SSID);
        holder.rssi.setText(String.valueOf(rssiToWifiStrengthLevel(wifiData.get(position).level)));
//        holder.rssi.setText(String.valueOf(wifiData.get(position).level));
        if (wifiData.get(position).capabilities.contains("WPA") || wifiData.get(position).capabilities.contains("WEP")) {
            holder.isSecured.setText("locked");
        } else {
            holder.isSecured.setText("open");
        }
        holder.capabilities.setText(wifiData.get(position).capabilities);

        return row;
    }

    private int rssiToWifiStrengthLevel(int rssi) {
        double percentage = rssiToPercentage(rssi);
        return percentageToSignalLevel(percentage);
    }

    private int percentageToSignalLevel(double percentage) {
        int signalLevel = 1;
        if (percentage >= 75) {
            signalLevel = 4;
        } else if (percentage >= 50 && percentage < 75) {
            signalLevel = 3;
        } else if (percentage >= 25 && percentage < 50) {
            signalLevel = 2;
        } else if (percentage < 25) {
            signalLevel = 1;
        }
        return signalLevel;
    }

    private double rssiToPercentage(int rssi) {
        double quality;
        if (rssi <= -100)
            quality = 0;
        else if (rssi >= -50)
            quality = 100;
        else
            quality = 2 * (rssi + 100);
        return quality;
    }

    static class WifiHolder {
        @InjectView(R.id.ssid)
        TextView ssid;

        @InjectView(R.id.isSecured)
        TextView isSecured;

        @InjectView(R.id.rssi)
        TextView rssi;

        @InjectView(R.id.capabilities)
        TextView capabilities;

        public WifiHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
