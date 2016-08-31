package org.appfields.kinerecorder;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;

/**
 * Provide fundamental function for Kinerecorder app
 */
public class KinerecorderApp {


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

    private static Context context;
    private static Intent serviceIntent;

    /**
     * Store the instance of the recording service
     */
    private static RecordingService recordingService;


    public static void setContext(Context ctx) {
        context = ctx;
    }


    public static  void startRecording() {
        serviceIntent = new Intent(context, RecordingService.class);
        context.startService(serviceIntent);
    }

    public  static void stopRecording() {
        context.stopService(serviceIntent);
    }

    public  static boolean isRecording() {
        return false;
    }
}