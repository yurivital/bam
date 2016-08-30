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
import java.text.DateFormat;
import java.util.Date;

/**
 * Write data into local folder
 */
public class WriterService extends IntentService {

    /**
     * Write data to space
     */
    private static final String ACTION_PERSIST = "PERSIST_SENSOR_DATA";


    public WriterService() {
        super("WriterService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionPersist(Context context, SensorEvent[] datas) {

        KinerecorderApp.data = datas;
        Intent intent = new Intent(context, WriterService.class);
        intent.setAction(ACTION_PERSIST);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PERSIST.equals(action)) {
                Log.v(ACTION_PERSIST, "Retrieve data");
                final SensorEvent[] datas = KinerecorderApp.data;
                Log.v(ACTION_PERSIST, "data len " + datas.length);
                handleActionPersist(datas);
            }
        }
    }

    protected static File buildPath() {
        if (KinerecorderApp.filePath.isEmpty()) {
            File basePath = Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_DOCUMENTS));
            File cowPath = new File(basePath, KinerecorderApp.cowType);
            String now = DateFormat.getDateInstance().format(new Date());
            File filePath = new File(cowPath, String.format("data_%s.txt", now));
            KinerecorderApp.filePath = filePath.toString();
        }
        return new File(KinerecorderApp.filePath);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPersist(SensorEvent[] datas) {
        String filename = "data_raw.txt";

        Log.v("handleActionPersist", "Retrieve data");
        int count = 0;

        try {
            File path = buildPath();
            File file = new File(path, filename);
            boolean newFile = !file.exists();

            FileOutputStream fos = new FileOutputStream(file, true);
            DataOutputStream dos = new DataOutputStream(fos);
            if (newFile) {
                dos.writeChars(KinerecorderApp.cowType);
            }

            for (int i = 0; i < datas.length; i++) {
                SensorEvent event = datas[i];
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
