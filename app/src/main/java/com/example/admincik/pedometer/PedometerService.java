package com.example.admincik.pedometer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.admincik.pedometer.sensor.StepDetector;
import com.example.admincik.pedometer.sensor.StepListener;

public class PedometerService extends Service implements SensorEventListener, StepListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private StepDetector stepDetector;
    private int numSteps;

    private final static String INTENT_ACTION = "PedometerUpdate";
    private final static String EXTRA_NAME = "steps";
    private final static String EXTRA_NAME_ERROR = "error";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (checkSensor()) {
            stepDetector = new StepDetector();
            stepDetector.registerListener(this);
            mSensorManager.registerListener(this,
                    mAccelerometer,
                    SensorManager.SENSOR_DELAY_UI);
        } else {
            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(EXTRA_NAME_ERROR, true);
            sendBroadcast(intent);
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            stepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra(EXTRA_NAME, numSteps);
        sendBroadcast(intent);
    }

    private boolean checkSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if (mAccelerometer != null) {
                return true;
            }
        }

        return false;
    }
}
