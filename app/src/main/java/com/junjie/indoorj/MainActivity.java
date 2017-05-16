package com.junjie.indoorj;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //    private static final int MIN_RSSI = -100;
//    private static final int MAX_RSSI = -55;
    private ArrayList<String> info = new ArrayList<>();
    // private ArrayList<String> temp=new ArrayList<>();
    private SensorManager sensorManager;
    private Sensor geomagnetic;

    private TextView tvmagnet;
    private TextView tvmagnetX;
    private TextView tvmagnetY;
    private TextView tvmagnetZ;

    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.lv_wifi);
        Button btn = (Button) findViewById(R.id.bt);
        tvmagnet = (TextView) findViewById(R.id.tv_magnet);
        tvmagnetX = (TextView) findViewById(R.id.tv_magnetX);
        tvmagnetY = (TextView) findViewById(R.id.tv_magnetY);
        tvmagnetZ = (TextView) findViewById(R.id.tv_magnetZ);

        sensorManager = (SensorManager) this.getApplicationContext().getSystemService(SENSOR_SERVICE);




        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getAroundWifiDeviceInfo(MainActivity.this));



        listView.setAdapter(arrayAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = getAroundWifiDeviceInfo(MainActivity.this);
                arrayAdapter.notifyDataSetChanged();
                Log.d("O", info.toString());
            }
        });



    }

    public ArrayList getAroundWifiDeviceInfo(Context context) {
        ArrayList<String> sInfo = new ArrayList<>();
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);

        //本机wifi信息
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();
        int rssi = info.getRssi();
        sInfo.add(ssid + ":?" + rssi);
        Log.d("L", ssid);

        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();//搜素到的设备列表


        if (scanResults.isEmpty()) {
            if (wifiManager.getWifiState() == 3) {
                Toast.makeText(context, "当前区域没有无线网络", Toast.LENGTH_SHORT).show();
            } else if (wifiManager.getWifiState() == 2) {
                Toast.makeText(context, "wifi正在开启，请稍后扫描", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WiFi没有开启", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "扫描到wifi", Toast.LENGTH_SHORT).show();
            //周边wifi信息
            for (ScanResult scanResult : scanResults) {
                sInfo.add("SSID:" + scanResult.SSID + "RSSI:" + scanResult.level);
                /*
                wifi信号等级*/
                //"Cal:" + wifiManager.calculateSignalLevel(scanResult.level, 4);
                //Log.d("L", scanResult.SSID);
            }
        }
        return sInfo;
    }

    //    public int calculateSignalLevel(int rssi, int numLevels) {
//        if (rssi <= MIN_RSSI) {
//            return 0;
//        } else if (rssi >= MAX_RSSI) {
//            return numLevels - 1;
//        } else {
//            int partitionSize = (MAX_RSSI - MIN_RSSI) / (numLevels - 1);
//            return (rssi - MIN_RSSI) / partitionSize;
//        }
//    }
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float magnetX = event.values[0];
            float magnetY = event.values[1];
            float magnetZ = event.values[2];
            tvmagnetX.setText("X:"+String.valueOf(magnetX));
            tvmagnetY.setText("Y:"+String.valueOf(magnetY));
            tvmagnetZ.setText("Z:"+String.valueOf(magnetZ));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        geomagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(sensorEventListener, geomagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
}
