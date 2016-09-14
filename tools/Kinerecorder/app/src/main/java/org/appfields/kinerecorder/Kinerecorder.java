package org.appfields.kinerecorder;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.provider.Settings;

/**
 * Expose the application API
 */
public class Kinerecorder {


    /**
     * Store the value of the current cow race
     */
    public static String cowType = "";

    /***
     * Store the value of the contact number receiving SMS
     */
    public static String contactNumber;

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

    private static PowerManager.WakeLock lock;

    /**
     * Start the recoding service.
     *
     * @param ctx       Instance of application context
     * @param cow       Value of cow type
     * @param contactNb value of the number receiving SMS
     */
    public static void startRecording(Context ctx, String cow, String contactNb) {
        context = ctx;
        cowType = cow;
        contactNumber = contactNb;
        filePath = "";
        // Lock usage of CPU and start the recording service
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        lock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Kinerecorder");
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 1);

        lock.acquire();
        serviceIntent = new Intent(context, RecordingService.class);
        context.startService(serviceIntent);
    }

    /**
     * Stop the recording service
     */
    public static void stopRecording() {
        context.stopService(serviceIntent);
        lock.release();

    }

}