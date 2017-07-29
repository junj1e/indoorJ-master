package com.junjie.indoorj.features;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.EditText;

import com.junjie.indoorj.database.dao.MagnetDAO;
import com.junjie.indoorj.database.entity.MagnetBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by junjihua on 2017/6/12.
 */

public class SensorsDataManager {

    //计数控制10次记录一次
    private final static int COLLECTCOUNT = 10;
    private int count = 0;
    private EditText etXAxis;
    private EditText etYAxis;
    private Context context;


    private SensorManager sensorManager;
    private Sensor geomagneticSensor;
    private SensorEventListener geomagneticSensorListener;
    private List<MagnetBean> addMagnetList = null;

    private volatile static SensorsDataManager sensorsDataManager = null;

    public static SensorsDataManager getInstance() {
        if (sensorsDataManager == null) {
            synchronized (SensorsDataManager.class) {
                if (sensorsDataManager == null) {
                    sensorsDataManager = new SensorsDataManager();
                }
            }
        }
        return sensorsDataManager;
    }

    public void init(Context context,EditText x,EditText y) {
        this.context=context;
        this.etXAxis=x;
        this.etYAxis=y;

//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View view = layoutInflater.inflate(R.layout.activity_main, null);
//        LinearLayout ll= (LinearLayout) view.findViewById(R.id.linearLayout_locationadd);
//        etXAxis = (EditText) ll.findViewById(R.id.edit_x_axis);
//        etYAxis = (EditText) ll.findViewById(R.id.edit_y_axis);



        addMagnetList= new ArrayList<MagnetBean>();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        geomagneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        geomagneticSensorListener = new MSensorListener();
        sensorManager.registerListener(geomagneticSensorListener, geomagneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    class MSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {

//            tvMagnetX.setText("X:" + String.valueOf(magnetX));
//            tvMagnetY.setText("Y:" + String.valueOf(magnetY));
//            tvMagnetZ.setText("Z:" + String.valueOf(magnetZ));
            MagnetBean newMagnet = new MagnetBean();

            float magnetX = event.values[0];
            float magnetY = event.values[1];
            float magnetZ = event.values[2];
            newMagnet.setMagnetx(magnetX);
            newMagnet.setMagnety(magnetY);
            newMagnet.setMagnetz(magnetZ);
            String strx2 = etXAxis.getText().toString();
            String strx ="2.2";
            Log.d("strx2",strx2);
            float x= Float.valueOf(strx2);
            newMagnet.setX(x);
            String stry2 = etYAxis.getText().toString();
            String stry ="2.2";
            Log.d("stry2",stry2);
            float y=Float.valueOf(stry2);
            newMagnet.setY(y);
            if (count < COLLECTCOUNT) {
                addMagnetList.add(newMagnet);
                count++;
            } else
                unregister();
            newMagnet = null;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


    public List<MagnetBean> getAddMagnetList() {
        return addMagnetList;
    }

    public void setAddMagnetList(List<MagnetBean> addMagnetList) {
        this.addMagnetList = addMagnetList;
    }

    public void unregister() {
        sensorManager.unregisterListener(geomagneticSensorListener);
        count = 0;
        MagnetDAO md = new MagnetDAO(context);
        md.insert(addMagnetList);

    }
}
