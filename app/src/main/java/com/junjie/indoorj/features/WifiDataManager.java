package com.junjie.indoorj.features;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.EditText;
import android.widget.Toast;

import com.junjie.indoorj.database.entity.MagnetBean;
import com.junjie.indoorj.database.entity.RssiBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by junjihua on 2017/6/14.
 */

public class WifiDataManager {
    //计数控制10次记录一次
    private static int count = 0;
    private EditText etXAxis;
    private EditText etYAxis;

    private WifiManager wifiManager;
    private List<RssiBean> addRssiList = new ArrayList<>();//返回的addRssiList对象
    //private static List<Map<String,String>> rssiMACMapName=new ArrayList<Map<String,String>>(6);
    private static Map<String, String> hashMapMAC = new HashMap<String, String>();//Map<String,String>第一个参数MAC地址，第二个参数是wifi的name

    private volatile static WifiDataManager wifiDataManager = null;

    public static WifiDataManager getInstance() {
        if (wifiDataManager == null) {
            synchronized (WifiDataManager.class) {
                if (wifiDataManager == null) {
                    wifiDataManager = new WifiDataManager();
                }
            }
        }
        return wifiDataManager;
    }

    public void init(Context context) {
        if (wifiManager == null) {
            wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
        }
        if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            Toast.makeText(context, "正在开启wifi，请稍后...", Toast.LENGTH_SHORT)
                    .show();
            if (wifiManager == null) {
                wifiManager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
            }
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        }
        startGetRssi(context);

    }

    public void startGetRssi(Context context) {
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
            //Map<String, String> hashMapMAC = new HashMap<String, String>();
            for (ScanResult scanResult : scanResults) {
                if (count < 6)
                    hashMapMAC.put(scanResult.BSSID, scanResult.SSID);
                count++;
            }
        }
        //  context.registerReceiver();
    }

/*    class  wifiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> scanResults = wifiManager.getScanResults();

        }
    }*/

    public void stopGetRssi() {

    }
}
