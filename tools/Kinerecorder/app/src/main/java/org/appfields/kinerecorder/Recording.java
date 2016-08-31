package org.appfields.kinerecorder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by yuriv on 31/08/2016.
 */
public class Recording {
    /**
     * Data buffer capacity
     */
    private static int capacity = 2000;

    /**
     * Data buffer
     */
    private static SensorEvent[] data = new SensorEvent[capacity];

    /**
     * Index of last data
     */
    private static int data_pointer = 0;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Context context;
    /**
     * Listen sensor values changes and write the to file
     */
    SensorEventListener listener = new SensorEventListener() {
        @Override
        public synchronized void onSensorChanged(SensorEvent event) {
            data[data_pointer] = event;
            int modulo = data_pointer + 1 % capacity;
            Log.v("Tracker", "Pointer size =" + data_pointer + " Modulo = " + modulo);
            if (data_pointer > 0 && modulo == capacity) {
                Log.i("SensorTraker", "Ask for persisting data");
                WriterService.startActionPersist(context, data.clone());
            }
            data_pointer = (++data_pointer % capacity);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public Recording(Context context) {
        this.context = context;
    }

    public void start() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this.listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(listener, sensor);
        listener = null;
        WriterService.startActionPersist(context, data.clone());
    }
}
