package org.appfields.kinerecorder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yuriv on 31/08/2016.
 * Encapsulate the recording into an Android Service
 * Accelerometer are listened as a background task
 */
public class RecordingService extends Service {

    /**
     * Instance of Recording Service specific Binder
     */
    private final IBinder mBinder = new RecordingBinder();
    /**
     * Store the instance of Recording
     */
    private Recording recording;

    private BroadcastReceiver receiver;

    /**
     * Store the instance of notification manager
     */
    private NotificationManager mNM;
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.recording_started;

    /**
     * Return th instance of Servicebinder to etablish an communication between activity and service
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;

    }

    /**
     * Initialize the service and show the notificaiton
     */
    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        recording = new Recording(getBaseContext());
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();

        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        receiver = new PowerReceiver();
        registerReceiver(receiver, filter);
    }

    /**
     * Run the service and start recording
     *
     * @param intent  Instance of intent who want the service
     * @param flags
     * @param startId id
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("RecordService", "Received start id " + startId + ": " + intent);

        recording.start();
        SMSSender.sendRecordingStarted(getApplicationContext());

        return START_NOT_STICKY;
    }

    /**
     * Stop the recording when the service is destroyed
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        // Cancel the persistent notification.
        recording.stop();
        // mNM.cancel(NOTIFICATION);
        stopForeground(true);
        // Tell the user we stopped.

        Toast.makeText(this, R.string.recording_service_stopped, Toast.LENGTH_SHORT).show();
    }


    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(NOTIFICATION);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, RecordingActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.recording_service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
        startForeground(NOTIFICATION, notification);
    }

    /**
     * Recorder binder
     */
    public class RecordingBinder extends Binder {
        RecordingService getService() {
            return RecordingService.this;
        }
    }
}


