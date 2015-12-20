package com.borntowindevelopers.whitelogics;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.borntowindevelopers.whitelogics.CommonUtilities.SENDER_ID;
public class MainActivity extends AppCompatActivity {
    private XWalkView xWalkWebView;
    private PendingIntent pendingIntent;
    public static String body,subject,time;
    public static int id;
    ConnectionDetector cd;
    public static String name;
    public static String email;
    DatabaseHandler db;
    Notification noti;
    Calendar calendar;

    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MainActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Getting name, email from intent


        name = "junidarling";
        email = "achu";

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);


        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        Log.d("", regId);
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, name, email, regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }


        xWalkWebView = (XWalkView) findViewById(R.id.xwalkWebView);
        xWalkWebView.loadAppFromManifest("file:///android_asset/manifest.json", null);

        // turn on debugging
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

//        db = new DatabaseHandler(this);

        Log.d("Insert: ", "Inserting ..");
//        db.addNotification(new Notification(1,"Azhar", "91000sdgsd0000", "2015-12-18 18:44:43"));
//        db.addNotification(new Notification(2,"Ashwin", "9100000gdsgs000", "2015-12-19 18:44:00"));
//        db.addNotification(new Notification(3,"kumar", "9100000000", "2015-12-18 18:44:10"));
//        db.addNotification(new Notification(4,"juni", "9100000000", "2015-12-18 19:28:00"));
//        setNotification();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (xWalkWebView != null) {
            xWalkWebView.pauseTimers();
            xWalkWebView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (xWalkWebView != null) {
            xWalkWebView.resumeTimers();
            xWalkWebView.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {

            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {

        }

        super.onDestroy();
        if (xWalkWebView != null) {
            xWalkWebView.onDestroy();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( xWalkWebView != null) {
            xWalkWebView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if ( xWalkWebView != null) {
            xWalkWebView.onNewIntent(intent);
        }
    }

}
