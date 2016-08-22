package org.appfields.kinerecorder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class RecordingActivity extends AppCompatActivity {

    private static int capacity = 2000;

    private static SensorEvent[] data = new SensorEvent[capacity];
    private static int data_pointer = 0;

    SensorEventListener listener = new SensorEventListener() {
        @Override
        public synchronized void onSensorChanged(SensorEvent event) {
            data[data_pointer] = event;
            int modulo = data_pointer + 1 % capacity;
            Log.v("Tracker", "Pointer size =" + data_pointer + " Modulo = " + modulo);
            if (data_pointer > 0 && modulo == capacity) {
                Log.i("SensorTraker", "Ask for persit data");
                WriterService.startActionPersist(getApplicationContext(), data.clone());
            }

            data_pointer = (short) (++data_pointer % capacity);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private TextView valueX;
    private TextView valueY;
    private TextView valueZ;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        setTitle("Acquisition du signal");

        setContentView(R.layout.activity_recording);
        /*
        this.valueX = (TextView) findViewById(R.id.AccelerationX);
        this.valueY = (TextView) findViewById(R.id.AccelerationY);
        this.valueZ = (TextView) findViewById(R.id.AccelerationZ);
        */
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this.listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          if (data_pointer - 1 < 0) {
                                              return;
                                          }
                                          final SensorEvent event = data[data_pointer - 1];
                                          if (event == null) {
                                              return;
                                          }
                                          runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  displayAcceleration(event.values[0],
                                                          event.values[1],
                                                          event.values[2]);

                                              }
                                          });


                                      }
                                  },
                0, 1000);
    }

    private void displayAcceleration(float x, float y, float z) {
        String format = "%1s";
    /*    this.valueX.setText(String.format(format, x));
        this.valueY.setText(String.format(format, y));
        this.valueZ.setText(String.format(format, z));
    */
    }

}
