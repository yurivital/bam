package org.appfields.kinerecorder;

import android.app.Application;
import android.app.Service;
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

    /**
     * Store the instance of the recording service
     */
    private Service recordingService;

    public void startService() {

    }

    public void stopService() {

    }

    public boolean serviceIsRunning() {
        return false
    }
}