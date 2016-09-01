package org.appfields.kinerecorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Displayed to user when a record is performing
 */
public class RecordingActivity extends AppCompatActivity {

    /**
     * Number of dots to display
     */
    int dots = 0;
    /**
     * Store the instance of the spinner
     */
    private TextView spinner;
    /**
     * Store the instance of the timer
     */
    private Timer timer = new Timer();

    /**
     * Initialize the activity
     * @param savedInstanceState previous state, not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getText(R.string.text_recording));
        setContentView(R.layout.activity_recording);
        TextView textView = (TextView) findViewById(R.id.input_cow_type);
        textView.setText(Kinerecorder.cowType);
        spinner = (TextView) findViewById(R.id.spinner);


        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {

                                          runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  moveSpinner();

                                              }
                                          });
                                      }
                                  },
                0, 1000);
    }

    /**
     * Stop the current recording on back button pressed
     */
    @Override
    public void onBackPressed() {
        OnStopRecord(null);
    }

    /**
     * Stop the current record and go back to welcome page
     *
     * @param v instance of sender object
     */
    public void OnStopRecord(View v) {
        Kinerecorder.stopRecording();

        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * change spinner step
     */
    private void moveSpinner() {
        dots = (++dots) % 7;
        String dottext = "";
        for (int i = 0; i < dots; i++) {
            dottext += "*";
        }
        spinner.setText(dottext);
    }

}
