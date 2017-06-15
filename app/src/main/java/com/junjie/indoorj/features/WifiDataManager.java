package com.junjie.indoorj.features;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.junjie.indoorj.database.dao.RssiDAO;
import com.junjie.indoorj.database.entity.RssiBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by junjihua on 2017/6/14.
 */

public class WifiDataManager {
    //计数控制10次记录一次
    //public boolean isfineshed = false;//判断是否完成检测；
    private final static int COLLECTCOUNT = 10;
    private static int count = 0;
    private int numcount = 0;//计数addRssiList10次
    private EditText etXAxis;
    private EditText etYAxis;

    private WifiManager wifiManager;
    private List<RssiBean> addRssiList = null;//返回的addRssiList对象
    //private static List<Map<String,String>> rssiMACMapName=new ArrayList<Map<String,String>>(6);
    //Map<String,Integer> hashMapMAC = new HashMap<>();//Map<String,String>第一个参数MAC地址，第二个参数是wifi的强度

     private static List<String> listMAC = new ArrayList<String>();//记录MAC地址的列表
//    public final static List<String> listMAC = Arrays.asList(
//            "0c:d9:96:4d:ae:ce",
//            "0c:d9:96:4d:b4:92",
//            "0c:d9:96:4d:ae:cd",
//            "fc:d7:33:88:60:a0",
//            "0c:d9:96:4d:ae:c0",
//            "8c:ab:8e:f2:41:f8"
//    );

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
        addRssiList = new ArrayList<>();
        //isfineshed = false;
        startGetRssi(context);

    }

    public void startGetRssi(Context context) {
       /* while (numcount > 0) {

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
                Map<String, Integer> hashMapMAC = new HashMap<String, Integer>();
                for (ScanResult scanResult : scanResults) {

                    if (count < 6)
                        listMAC.add(scanResult.BSSID);
                    count++;
                    hashMapMAC.put(scanResult.BSSID, scanResult.level);


                }
                Log.i("hashMapMac", hashMapMAC.toString());
                RssiBean newRssi = new RssiBean();
                newRssi.setRssi1(hashMapMAC.get(listMAC.get(0)));
                newRssi.setRssi2(hashMapMAC.get(listMAC.get(1)));
                newRssi.setRssi3(hashMapMAC.get(listMAC.get(2)));
                newRssi.setRssi4(hashMapMAC.get(listMAC.get(3)));
                newRssi.setRssi5(hashMapMAC.get(listMAC.get(4)));
                newRssi.setRssi6(hashMapMAC.get(listMAC.get(5)));
                newRssi.setX(1.1f);
                newRssi.setY(2.2f);

                addRssiList.add(newRssi);
                numcount--;
                //wifiManager.startScan();

            }
        }
        */
        wifiManager.startScan();
        context.registerReceiver(wifiBroadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    private final BroadcastReceiver wifiBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> scanResults = wifiManager.getScanResults();
            Map<String, Integer> hashMapMAC = new HashMap<String, Integer>();
            for (ScanResult scanResult : scanResults) {

                if (count < 6)
                    listMAC.add(scanResult.BSSID);
                count++;
                hashMapMAC.put(scanResult.BSSID, scanResult.level);


            }
            Log.i("listMAC", listMAC.toString());
            Log.i("hashMapMac", hashMapMAC.toString());
            RssiBean newRssi = new RssiBean();
            if(hashMapMAC.containsKey(listMAC.get(0)))
                newRssi.setRssi1(hashMapMAC.get(listMAC.get(0)));
            else
                newRssi.setRssi1(0);
            if(hashMapMAC.containsKey(listMAC.get(1)))
            newRssi.setRssi2(hashMapMAC.get(listMAC.get(1)));
            else
                newRssi.setRssi2(0);
            if(hashMapMAC.containsKey(listMAC.get(2)))
            newRssi.setRssi3(hashMapMAC.get(listMAC.get(2)));
            else
                newRssi.setRssi3(0);
            if(hashMapMAC.containsKey(listMAC.get(3)))
            newRssi.setRssi4(hashMapMAC.get(listMAC.get(3)));
            else
                newRssi.setRssi4(0);
            if(hashMapMAC.containsKey(listMAC.get(4)))
            newRssi.setRssi5(hashMapMAC.get(listMAC.get(4)));
            else
                newRssi.setRssi5(0);
            if(hashMapMAC.containsKey(listMAC.get(5)))
            newRssi.setRssi6(hashMapMAC.get(listMAC.get(5)));
            else
                newRssi.setRssi6(0);
            newRssi.setX(1.1f);
            newRssi.setY(2.2f);
            if (numcount < COLLECTCOUNT) {
                addRssiList.add(newRssi);
                numcount++;
            } else
                stopGetRssi(context);


            wifiManager.startScan();

        }
    };

    public List<RssiBean> getAddRssiList() {
        return addRssiList;
    }

    public void setAddRssiList(List<RssiBean> addRssiList) {
        this.addRssiList = addRssiList;
    }

    public void stopGetRssi(Context context) {
        context.unregisterReceiver(wifiBroadcastReceiver);
        //isfineshed=true;
        numcount = 0;
        Log.i("AddRssiList", addRssiList.toString());
        RssiDAO rd = new RssiDAO(context);
        rd.insert(addRssiList);

    }
}
