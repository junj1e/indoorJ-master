package com.junjie.indoorj.features;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.junjie.indoorj.R;
import com.junjie.indoorj.database.entity.MagnetBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by junjihua on 2017/6/12.
 */

public class SensorsDataManager {

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


    private class MSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {

//            tvMagnetX.setText("X:" + String.valueOf(magnetX));
//            tvMagnetY.setText("Y:" + String.valueOf(magnetY));
//            tvMagnetZ.setText("Z:" + String.valueOf(magnetZ));
            MagnetBean newMagnet = new MagnetBean();
            for (int i = 0; i < 100; i++) {
                float magnetX = event.values[0];
                float magnetY = event.values[1];
                float magnetZ = event.values[2];
                newMagnet.setXmagnet(magnetX);
                newMagnet.setYmagnet(magnetY);
                newMagnet.setZmagnet(magnetZ);
                newMagnet.setX(Float.valueOf(etXAxis.getText().toString()));
                newMagnet.setY(Float.valueOf(etYAxis.getText().toString()));
                addMagnetList.add(newMagnet);
                newMagnet = null;
            }

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
