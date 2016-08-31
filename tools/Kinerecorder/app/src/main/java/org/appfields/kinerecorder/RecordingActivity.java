package org.appfields.kinerecorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Activity performing the recording of motions sensors values
 */
public class RecordingActivity extends AppCompatActivity {

    int dots = 0;
    private TextView spinner;
    private Timer timer = new Timer();

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


        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {

                                          runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  displayAcceleration();

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
        KinerecorderApp app = (KinerecorderApp) getApplication();
        app.startRecording();

        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void displayAcceleration() {
        dots = (++dots) % 7;
        String dottext = "";
        for (int i = 0; i < dots; i++) {
            dottext += "*";
        }
        spinner.setText(dottext);
    }

}
