package com.wateww.jawaposdownloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {

    private static final String PREFERENCES = "PREFERENCE" ;
    private static final String PREF_TIME_HOUR = "PREF_TIME_HOUR" ;
    private static final String PREF_TIME_MINUTE = "PREF_TIME_MINUTE" ;
    private static final String PREF_SWITCH_ON = "PREF_SWITCH_ON";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sharedpreferences = context.getSharedPreferences(PREFERENCES,
                    Context.MODE_PRIVATE);
            if (sharedpreferences.getBoolean(PREF_SWITCH_ON, false)) {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                int interval = 1000 * 60 * 60 * 24;

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, sharedpreferences.getInt(PREF_TIME_HOUR, 0));
                calendar.set(Calendar.MINUTE, sharedpreferences.getInt(PREF_TIME_MINUTE, 0) - 1);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    calendar.add(Calendar.HOUR_OF_DAY, 24);
                }

                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval,
                        pendingIntent);
            }
        }
    }
}
