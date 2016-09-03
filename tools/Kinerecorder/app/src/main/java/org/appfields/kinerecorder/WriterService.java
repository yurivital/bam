package org.appfields.kinerecorder;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.os.Environment;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Write data into local folder
 */
public class WriterService extends IntentService {

    /**
     * Store the values of current set of datas
     */
    private static SensorEvent[] data;

    /**
     * Write data
     */
    private static final String ACTION_PERSIST = "PERSIST_SENSOR_DATA";

    /**
     * Create an new instance ofe WriterService
     */
    public WriterService() {
        super("WriterService");
    }

    /**
     * Starts this service to persist sensor events data. If
     * the service is already performing a task this action will be queued.
     *
     * @param context instance of current context
     * @param datas   array of SensorEvent to persist
     * @see IntentService
     */
    public static void startActionPersist(Context context, SensorEvent[] datas) {

        data = datas;
        Intent intent = new Intent(context, WriterService.class);
        intent.setAction(ACTION_PERSIST);
        context.startService(intent);
    }

    /**
     * Create sensor data file path based on cow type and timestamp.
     * For convenience with the cheap test device, files are written into Download directory
     *
     * @return Configured File instance
     */
    protected static File buildPath() {
        if (Kinerecorder.filePath.isEmpty()) {
            File basePath = Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_DOWNLOADS));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmms");
            String now = formatter.format(new Date());
            File filePath = new File(basePath, String.format("%1s_%2s.txt", Kinerecorder.cowType, now));
            Kinerecorder.filePath = filePath.toString();
            Log.i(ACTION_PERSIST, "Writing into " + filePath);
        }
        return new File(Kinerecorder.filePath);
    }

    /**
     * Retrive data from the application API and store
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PERSIST.equals(action)) {
                Log.v(ACTION_PERSIST, "Retrieve data");
                handleActionPersist();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPersist() {
        Log.v("handleActionPersist", "Retrieve data");
        int count = 0;

        try {
            File file = buildPath();
            boolean newFile = !file.exists();

            FileOutputStream fos = new FileOutputStream(file, true);
            DataOutputStream dos = new DataOutputStream(fos);
            // the first line contain the cow type
            if (newFile) {
                dos.writeChars(Kinerecorder.cowType);
            }


            for (int i = 0; i < data.length; i++) {
                SensorEvent event = data[i];
                if (event == null) {
                    break;
                }
                count++;
                dos.writeLong(event.timestamp);
                for (int j = 0; (j < event.values.length || j < 3); j++) {
                    dos.writeFloat(event.values[j]);
                }
                dos.writeChars("\r");
            }
            dos.flush();
            fos.flush();
            dos.close();
            fos.close();
            Log.i(ACTION_PERSIST, "data persisted " + count);
        } catch (Exception e) {
            Log.e(ACTION_PERSIST, "Error in writing data", e);
            e.printStackTrace();
        }
    }
}
