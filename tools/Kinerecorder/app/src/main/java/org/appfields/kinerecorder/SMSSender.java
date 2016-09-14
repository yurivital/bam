package org.appfields.kinerecorder;

import android.content.Context;
import android.telephony.SmsManager;

/**
 * Provide functionnal API for sending SMS
 */
public class SMSSender {

    public static void sendRecordingStarted(Context context) {

        SmsManager manager = SmsManager.getDefault();
        String message = context.getText(R.string.sms_recording_start).toString();
        manager.sendTextMessage(Kinerecorder.contactNumber, null, message, null, null);
    }

    public static void sendUserPowerOff(Context context) {
        SmsManager manager = SmsManager.getDefault();
        String message = context.getText(R.string.sms_user_poweroff).toString();
        manager.sendTextMessage(Kinerecorder.contactNumber, null, message, null, null);
    }

    public static void sendLowBattery(Context context) {
        SmsManager manager = SmsManager.getDefault();
        String message = context.getText(R.string.sms_low_battery).toString();
        manager.sendTextMessage(Kinerecorder.contactNumber, null, message, null, null);
    }


}
