package org.appfields.kinerecorder;

import android.telephony.SmsManager;

/**
 * Created by yuriv on 13/09/2016.
 */
public class SMSSender {

    public static void sendRecordingStarted() {

        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage("",null,"ttp", );
    }

    public static void sendUserPowerOff() {

    }



}
