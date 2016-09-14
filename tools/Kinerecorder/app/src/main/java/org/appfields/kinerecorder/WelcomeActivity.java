package org.appfields.kinerecorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static org.appfields.kinerecorder.R.id;
import static org.appfields.kinerecorder.R.layout;

/**
 * Gather information of the recording session and start the recording service
 */
public class WelcomeActivity extends AppCompatActivity {

    private EditText cow_type;
    private EditText contact_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_welcome);
        cow_type = (EditText) findViewById(id.input_cow_type);
        contact_number = (EditText) findViewById(id.input_telephone);
        final Button btn_start = (Button) findViewById(id.btn_start_record);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_start.setEnabled(isValid());
            }
        };

        cow_type.addTextChangedListener(watcher);
        contact_number.addTextChangedListener(watcher);
    }

    public boolean isValid() {
        return !contact_number.toString().trim().isEmpty() && !cow_type.toString().trim().isEmpty();
    }


    /**
     * Start the recording service and go to the Recording Activity
     *
     * @param v Sender view
     */
    public void StartRecord(View v) {
        Kinerecorder.startRecording(getApplicationContext(),
                cow_type.getText().toString().trim(),
                contact_number.getText().toString().trim());
        Intent intent = new Intent(getApplicationContext(), RecordingActivity.class);
        startActivity(intent);
        finish();
    }
}
