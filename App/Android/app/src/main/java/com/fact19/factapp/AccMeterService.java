package com.fact19.factapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class AccMeterService extends Service implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accMeterSensor;

    long lastUpdate;
    boolean defaultAccMeterStatus = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager      = (SensorManager)getSystemService(SENSOR_SERVICE);
        if(sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accMeterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float xVal = event.values[0];
            float yVal = event.values[1];
            float zVal = event.values[2];

            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 300) {
                lastUpdate = curTime;
                if(yVal > 6.0 && yVal < 9) {
                    playAlarm(R.raw.carreversealarm);
                }
                //accMeterValueTxtVw.setText("AccMeter values: " + xVal + ", " + yVal + ", " + zVal);
                Log.d(FactAppActivity.class.getSimpleName(), "AccMeter values: " + yVal);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void playAlarm(int sound) {
        MediaPlayer mp = MediaPlayer.create(this, sound);
        mp.start();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
}
