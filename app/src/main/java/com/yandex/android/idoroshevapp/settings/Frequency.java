package com.yandex.android.idoroshevapp.settings;

import android.app.AlarmManager;

public class Frequency {
    public static final String MINUTES_15 = "0";
    public static final String HOUR_1 = "1";
    public static final String HOUR_8 = "2";
    public static final String HOUR_24 = "3";

    public static final String DEFAULT = MINUTES_15;

    public static long getFrequency(String code) {
        switch (code) {
            case MINUTES_15: return AlarmManager.INTERVAL_FIFTEEN_MINUTES;
            case HOUR_1: return AlarmManager.INTERVAL_HOUR;
            case HOUR_8: return AlarmManager.INTERVAL_HOUR * 8;
            case HOUR_24: return AlarmManager.INTERVAL_DAY;
            default: return AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        }
    }
}
