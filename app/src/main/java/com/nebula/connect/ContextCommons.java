package com.nebula.connect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nebula.connect.logreports.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sagar on 26/7/16.
 */
public class ContextCommons {

    private static final String TAG = ContextCommons.class.getSimpleName();

    public static void setAlarm(Context context) {

        Logger.d(TAG, "inside set Alarm");
        try {
            Intent intent = new Intent();
            Logger.d(TAG,"Setting alarm");
            intent.setClass(context, AlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 123456, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            // int min = 0;
            // int hr = currentHour;
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.add(Calendar.DATE, 1);
            calendar.add(Calendar.HOUR_OF_DAY, 7);

            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
            Logger.d(TAG, "next alarmtime=" + format.format(new Date(calendar.getTimeInMillis())));

            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, sender);

        } catch (Exception e) {
            Logger.e(TAG,e);
            e.printStackTrace();
        }
    }

    public static boolean isOnline(Context context) {
        Logger.d(TAG,"inside isOnline");
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
