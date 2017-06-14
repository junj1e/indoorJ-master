package com.junjie.indoorj.features;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.junjie.indoorj.R;
import com.junjie.indoorj.database.entity.MagnetBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by junjihua on 2017/6/12.
 */

public class SensorsDataManager {

    //计数控制10次记录一次
    private static int count=0;
    private EditText etXAxis;
    private EditText etYAxis;

    private SensorManager sensorManager;
    private Sensor geomagneticSensor;
    private SensorEventListener geomagneticSensorListener;
    private List<MagnetBean> addMagnetList = new ArrayList<MagnetBean>();

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

    public void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_main, null);
        etXAxis = (EditText) view.findViewById(R.id.edit_x_axis);
        etYAxis = (EditText) view.findViewById(R.id.edit_y_axis);

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
            String strx=etXAxis.getText().toString();
            System.out.println(strx);
            //float x= Float.valueOf(strx);
            newMagnet.setX(0f);
            String stry=etYAxis.getText().toString();
            System.out.println(stry);
            //float y=Float.valueOf(stry);
            newMagnet.setY(0f);
            if(count<10)
                addMagnetList.add(newMagnet);
            count++;
            newMagnet = null;


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    public void unregister() {
        sensorManager.unregisterListener(geomagneticSensorListener);
    }

    public List<MagnetBean> getAddMagnetList() {
        return addMagnetList;
    }

    public void setAddMagnetList(List<MagnetBean> addMagnetList) {
        this.addMagnetList = addMagnetList;
    }
}
