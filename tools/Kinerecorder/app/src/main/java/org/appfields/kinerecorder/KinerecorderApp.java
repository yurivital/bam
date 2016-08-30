package org.appfields.kinerecorder;

import android.app.Application;
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
    public static String filePath="";

}