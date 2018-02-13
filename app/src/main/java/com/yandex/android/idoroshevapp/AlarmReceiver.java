package com.yandex.android.idoroshevapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(ImageLoaderService.ACTION_LOAD_IMAGE);
        ImageLoaderService.enqueueWork(context, serviceIntent);
    }
}
