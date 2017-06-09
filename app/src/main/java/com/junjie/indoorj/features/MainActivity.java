package com.junjie.indoorj.features;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junjie.indoorj.R;
import com.junjie.indoorj.database.dao.MagnetDAO;
import com.junjie.indoorj.database.dao.RssiDAO;
import com.junjie.indoorj.database.entity.MagnetBean;
import com.junjie.indoorj.database.entity.RssiBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//  private static final int MIN_RSSI = -100;
//  private static final int MAX_RSSI = -55;
    private ArrayList<String> info = new ArrayList<>();
// private ArrayList<String> temp=new ArrayList<>();
    private SensorManager sensorManager;
    private Sensor geomagnetic;

    private Button btn;
    private ListView listView;

    private TextView tvmagnet;
    private TextView tvmagnetX;
    private TextView tvmagnetY;
    private TextView tvmagnetZ;

    private TextView testText;


    private ArrayAdapter<String> arrayAdapter;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }

    public void init() {
        listView = (ListView) findViewById(R.id.lv_wifi);
        btn = (Button) findViewById(R.id.bt);
        tvmagnet = (TextView) findViewById(R.id.tv_magnet);
        tvmagnetX = (TextView) findViewById(R.id.tv_magnetX);
        tvmagnetY = (TextView) findViewById(R.id.tv_magnetY);
        tvmagnetZ = (TextView) findViewById(R.id.tv_magnetZ);

        testText= (TextView) findViewById(R.id.textView2);

        sensorManager = (SensorManager) this.getApplicationContext().getSystemService(SENSOR_SERVICE);


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getAroundWifiDeviceInfo(MainActivity.this));


        listView.setAdapter(arrayAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new MyThread();
                thread.start();
                handler = new MyHander();
                //Log.d("O", info.toString());
            }
        });

        //数据库测试代码
        RssiBean testRssi=new RssiBean(1,2,3,4,5,6,34.4,45.5);
        RssiDAO rd=new RssiDAO(MainActivity.this);
        rd.insert(testRssi);
        List<RssiBean> list=new ArrayList<RssiBean>();
        list=rd.selectAll();
        //testText.setText(list.get(0).toString());
        Log.i("rssi", String.valueOf(list));

        MagnetBean magnet=new MagnetBean(1.1,2.2,3.3,45.5,65.5);
        MagnetDAO md=new MagnetDAO(MainActivity.this);
        md.insert(magnet);
        List<MagnetBean> listM=new ArrayList<MagnetBean>();
        listM=md.selectAll();
        Log.i("magnet", String.valueOf(listM));

    }

    class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            info = (ArrayList<String>) msg.obj;
            arrayAdapter.clear();
            arrayAdapter.addAll(info);
            arrayAdapter.notifyDataSetChanged();
            // Log.d("count",arrayAdapter.toString());
        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10);
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = getAroundWifiDeviceInfo(MainActivity.this);
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public ArrayList<String> getAroundWifiDeviceInfo(Context context) {
        ArrayList<String> sInfo = new ArrayList<>();
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);

        //本机wifi信息
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();
        int rssi = info.getRssi();
        sInfo.add(ssid + ":?" + rssi);
        //Log.d("L", ssid);

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
            //Toast.makeText(context, "扫描到wifi", Toast.LENGTH_SHORT).show();
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

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float magnetX = event.values[0];
            float magnetY = event.values[1];
            float magnetZ = event.values[2];
            tvmagnetX.setText("X:" + String.valueOf(magnetX));
            tvmagnetY.setText("Y:" + String.valueOf(magnetY));
            tvmagnetZ.setText("Z:" + String.valueOf(magnetZ));
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
