package com.borntowindevelopers.whitelogics;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.borntowindevelopers.whitelogics.CommonUtilities.SENDER_ID;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

    static int message1 =0;

    public static final int NOTIFICATION_ID = 1;
    private static NotificationManager mNotificationManager;

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
                Log.d("NAME", MainActivity.name);
        ServerUtilities.register(context, MainActivity.name, MainActivity.email, registrationId);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {

        Log.i(TAG, "Received message");

        message1++;//Integer.parseInt(intent.getExtras().getString("id"));
        String message2="how r u";//intent.getExtras().getString("body");
        String message3="good mornin";//intent.getExtras().getString("sub");
//      String messageDate=intent.getExtras().getString("received_at");
//      String messageDateTemp1 = messageDate.replace('T', ' ');
//      String messageDateTemp2 = messageDateTemp1.replace('Z', ' ');
//      String message4= messageDateTemp2.trim();
        String message4=intent.getExtras().getString("price");

        String message=""+message1+" "+message2+" "+message3+" "+message4+" ";
        Log.d("message", message);

//        DatabaseHandler db = new DatabaseHandler(this);
//        Notification noti = new Notification(message1, message2, message3, message4);
//        db.newEntry(noti,context);


        // notifies user
       generateNotification(context, message);
    }


    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);

    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
     }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.icon;

        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);


        int numMessages = 0;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(icon)
                .setContentTitle("fuck off")
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setContentText(message)
                .setNumber(++numMessages);

        mBuilder.setContentIntent(intent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

}
