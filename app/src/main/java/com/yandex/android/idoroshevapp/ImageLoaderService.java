package com.yandex.android.idoroshevapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.text.TextUtils;
import android.util.Log;

public class ImageLoaderService extends JobIntentService {

    private static final String TAG = "idoroshevapp";

    public static final int JOB_ID_LOAD_IMAGE = 21234;

    public static final String ACTION_LOAD_IMAGE = "com.yandex.android.idoroshevapp.LOAD_IMAGE";

    public static final String BROADCAST_ACTION_UPDATE_IMAGE =
            "com.yandex.android.idoroshevapp.UPDATE_IMAGE";
    public static final String BROADCAST_PARAM_IMAGE = "com.yandex.android.idoroshevapp.IMAGE";


    private final ImageLoader mImageLoader;

    public ImageLoaderService() {
        mImageLoader = new ImageLoader();
    }

    public static void enqueueWork(Context context, Intent work) {
        Log.d(TAG, "ImageLoaderService#enqueueWork() with intent = " + work);
        enqueueWork(context, ImageLoaderService.class, JOB_ID_LOAD_IMAGE, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "ImageLoaderService#onCreate()");
    }

    @Override
    protected void onHandleWork(@NonNull final Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "ImageLoaderService#onHandleWork() with action = " + action);

        if (ACTION_LOAD_IMAGE.equals(action)) {
            final String imageUrl = mImageLoader.getImageUrl();
            if (!TextUtils.isEmpty(imageUrl)) {
                final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
                final String imageName = "myImage.png";
                ImageSaver.getInstance().saveImage(getApplicationContext(), bitmap, imageName);

                final Intent broadcastIntent = new Intent(BROADCAST_ACTION_UPDATE_IMAGE);
                broadcastIntent.putExtra(BROADCAST_PARAM_IMAGE, imageName);
                Log.d(TAG, "Sent broadcast");
                sendBroadcast(broadcastIntent);
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "ImageLoaderService#onDestroy()");
        super.onDestroy();
    }
}
