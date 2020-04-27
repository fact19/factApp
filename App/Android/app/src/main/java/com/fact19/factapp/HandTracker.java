package com.fact19.factapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

public class HandTracker extends AppCompatActivity implements SensorEventListener {

    private Context appContext;

    SensorManager sensorManager;
    Sensor accMeterSensor;

    long lastUpdate;
    boolean defaultAccMeterStatus = false;

    public HandTracker() {
        if(sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            //sensorManager.registerListener(this, accMeterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && defaultAccMeterStatus == true){
            float xVal = event.values[0];
            float yVal = event.values[1];
            float zVal = event.values[2];

            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 500) {
                lastUpdate = curTime;
                if(yVal > 6.0 && yVal < 9) {
                    playAlarm(R.raw.carreversealarm);
                }
                //accMeterValueTxtVw.setText("AccMeter values: " + xVal + ", " + yVal + ", " + zVal);
                //Log.d(FactAppActivity.class.getSimpleName(), "AccMeter values: " + accMeterX + ", " + accMeterY + ", " + accMeterZ);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accMeterSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
