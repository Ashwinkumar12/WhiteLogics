package com.borntowindevelopers.whitelogics;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Utils  {
//    MainActivity object = new MainActivity();
    public static NotificationManager mNotificationManager;
    public static String msg="hello";
    public static final int NOTIFICATION_ID = 1;
    static DatabaseHandler db;
    public static void generateNotification(Context context) {

        Log.d("RegisterActivity", "onClick of Share: After finish.");
        mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent in = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://almondmendoza.com/android-applications/"));
        //"http://www.google.com");// Main2Activity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, in, 0);

        int numMessages = 0;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(MainActivity.subject)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(MainActivity.body))
                .setAutoCancel(true)
                .setContentText(MainActivity.body)
                .setNumber(++numMessages);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        db.deleteFirstNotification();

        Notification notification = db.getNotification();

        db.setNotification(notification,context);



    }


}