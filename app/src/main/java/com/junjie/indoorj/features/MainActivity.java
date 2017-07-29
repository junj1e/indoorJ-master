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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junjie.indoorj.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //  private static final int MIN_RSSI = -100;
//  private static final int MAX_RSSI = -55;
    private List<String> info = new ArrayList<>();
    // private List<String> temp=new ArrayList<>();
    private SensorManager sensorManager;
    private Sensor geomagneticSensor;

    private Button btn;
    private ListView listView;

    private TextView tvMagnet;
    private TextView tvMagnetX;
    private TextView tvMagnetY;
    private TextView tvMagnetZ;

    private TextView testText;


    private ArrayAdapter<String> arrayAdapter;

    private Handler handler;

    //save the data
    private EditText etXAxis;
    private EditText etYAxis;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }

    public void init() {
        listView = (ListView) findViewById(R.id.lv_wifi);
        btn = (Button) findViewById(R.id.bt);
        tvMagnet = (TextView) findViewById(R.id.tv_magnet);
        tvMagnetX = (TextView) findViewById(R.id.tv_magnetX);
        tvMagnetY = (TextView) findViewById(R.id.tv_magnetY);
        tvMagnetZ = (TextView) findViewById(R.id.tv_magnetZ);

        testText = (TextView) findViewById(R.id.textView2);

        etXAxis = (EditText) findViewById(R.id.edit_x_axis);
        etYAxis = (EditText) findViewById(R.id.edit_y_axis);
        btnSave = (Button) findViewById(R.id.bt_save);

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


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsDataManager manager = SensorsDataManager.getInstance();
                manager.init(MainActivity.this,etXAxis,etYAxis);

                WifiDataManager wifiManager = WifiDataManager.getInstance();
                wifiManager.init(MainActivity.this,etXAxis,etYAxis);
//                Log.i("AddRssiList", wifiManager.getAddRssiList().toString());
//                RssiDAO rd = new RssiDAO(MainActivity.this);
//                rd.insert(wifiManager.getAddRssiList());


            }
        });
        //数据库测试代码
/*

        RssiBean testRssi = new RssiBean(1, 2, 3, 4, 5, 6, 34.4f, 45.5f);
        RssiDAO rd = new RssiDAO(MainActivity.this);
        rd.insert(testRssi);
        List<RssiBean> list = new ArrayList<RssiBean>();
        list = rd.selectAll();
        //testText.setText(list.get(0).toString());
        Log.i("rssi", String.valueOf(list));

        List<MagnetBean> ll=new ArrayList<MagnetBean>();
        for(int i=0;i<10;i++) {
            MagnetBean magnet = new MagnetBean(1.1f, 2.2f, 3.3f, 45.5f, 65.5f);
            ll.add(magnet);
        }
        MagnetDAO md = new MagnetDAO(MainActivity.this);
        md.insert(ll);
        List<MagnetBean> listM = new ArrayList<MagnetBean>();
        listM = md.selectAll();
        Log.i("magnet", String.valueOf(listM));
*/

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

    public List<String> getAroundWifiDeviceInfo(Context context) {
        List<String> sInfo = new ArrayList<>();
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);

        //本机wifi信息
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();
        int rssi = info.getRssi();
        String MACAddress = info.getBSSID();
        sInfo.add("当前连接的网络:" + ssid + ":?" + rssi + ":?" + MACAddress);
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
                sInfo.add("SSID:" + scanResult.SSID + "RSSI:" + scanResult.level + "MACAddress:" + scanResult.BSSID);
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
            tvMagnetX.setText("X:" + String.valueOf(magnetX));
            tvMagnetY.setText("Y:" + String.valueOf(magnetY));
            tvMagnetZ.setText("Z:" + String.valueOf(magnetZ));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        // geomagneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // sensorManager.registerListener(sensorEventListener, geomagneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear_db:

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
