package org.appfields.kinerecorder;

import android.app.Application;
import android.hardware.SensorEvent;

/**
 * App base class
 */
public class KinerecorderApp extends Application {
    /**
     * Store the values of current set of datas
     */
    public static SensorEvent[] data;

}