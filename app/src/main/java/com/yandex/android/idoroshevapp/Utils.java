package com.yandex.android.idoroshevapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.yandex.android.idoroshevapp.settings.SettingsFragment;


public class Utils {
    public static void scheduleAlarm(Context context) {

        cancelAlarm(context);
        Intent intent = new Intent(context, AlarmReceiver.class);

        final PendingIntent pIntent = PendingIntent.getBroadcast(context, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarm != null)
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                    SettingsFragment.getFrequency(context), pIntent);
    }

    public static void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarm != null) alarm.cancel(pIntent);
    }
}
