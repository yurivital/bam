package org.appfields.kinerecorder;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Activity performing the recording of motions sensors values
 */
public class RecordingActivity extends AppCompatActivity {
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
                WriterService.startActionPersist(getApplicationContext(), data.clone());
            }
            data_pointer = (++data_pointer % capacity);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView spinner;
    private TextView valueX;
    private TextView valueY;
    private TextView valueZ;
    private Timer timer = new Timer();
    int dots = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        setTitle(getText(R.string.text_recording));

        setContentView(R.layout.activity_recording);
        TextView textView = (TextView) findViewById(R.id.input_cow_type);
        textView.setText(KinerecorderApp.cowType);
        spinner = (TextView) findViewById(R.id.spinner);

        /*
        this.valueX = (TextView) findViewById(R.id.AccelerationX);
        this.valueY = (TextView) findViewById(R.id.AccelerationY);
        this.valueZ = (TextView) findViewById(R.id.AccelerationZ);
        */
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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

    @Override
    public void onBackPressed() {
        OnStopRecord(null);
    }

    /**
     * Stop the current record and go back to welcome page
     *
     * @param v
     */
    public void OnStopRecord(View v) {
        sensorManager.unregisterListener(listener, sensor);
        listener = null;
        WriterService.startActionPersist(getApplicationContext(), data.clone());
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void displayAcceleration(float x, float y, float z) {
        String format = "%1s";

        dots = (++dots) % 7;
        String dottext = "";
        for (int i = 0; i < dots; i++) {
            dottext += "*";
        }
        spinner.setText(dottext);


    /*    this.valueX.setText(String.format(format, x));
        this.valueY.setText(String.format(format, y));
        this.valueZ.setText(String.format(format, z));
    */
    }

}
