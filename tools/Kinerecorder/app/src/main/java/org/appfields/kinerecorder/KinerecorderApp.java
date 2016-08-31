package org.appfields.kinerecorder;

import android.app.Application;
import android.content.Intent;
import android.hardware.SensorEvent;

/**
 * Provide fundamental function for Kinerecorder app
 */
public class KinerecorderApp extends Application {
    /**
     * Store the values of current set of datas
     */
    public static SensorEvent[] data;

    /**
     * Store the value of the current cow race
     */
    public static String cowType = "";

    /**
     * Store the value of current recording file path
     */
    public static String filePath = "";
    Intent serviceIntent = new Intent(getApplicationContext(), RecordingService.class);
    /**
     * Store the instance of the recording service
     */
    private RecordingService recordingService;

    public void startRecording() {
        startService(serviceIntent);
        recordingService.start();
    }

    public void stopRecording() {
        stopService(serviceIntent);

    }

    public boolean isRecording() {
        return false;
    }
}