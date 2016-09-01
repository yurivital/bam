package org.appfields.kinerecorder;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;

/**
 * Expose the application API
 */
public class Kinerecorder {

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
     * Store the instance of current application context
     */
    private static Context context;
    /**
     * Store the instance of the last created recording service Intent
     */
    private static Intent serviceIntent;


    /**
     * Start the recoding service.
     *
     * @param ctx Instance of application context
     * @param cow Value of cow type
     */
    public static void startRecording(Context ctx, String cow) {
        context = ctx;
        cowType = cow;
        filePath = "";
        serviceIntent = new Intent(context, RecordingService.class);
        context.startService(serviceIntent);
    }

    /**
     * Stop the recording service
     */
    public static void stopRecording() {
        context.stopService(serviceIntent);
    }

}