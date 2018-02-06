package com.yandex.android.idoroshevapp.data;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static ArrayList<AppInfo> data = new ArrayList<>();

    public static ArrayList<AppInfo> generateData(Activity activity) {
        data.clear();
        PackageManager packageManager = activity.getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(0);
        for (ApplicationInfo applicationInfo : applicationInfoList) {
            try {
                if (packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
                    AppInfo appInfo = getAppInfoFromPackageName(applicationInfo.packageName, activity);
                    if (!data.contains(appInfo)) {
                        data.add(appInfo);
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static AppInfo getAppInfoFromPackageName(final String packageName, Activity activity) throws PackageManager.NameNotFoundException {
        final PackageManager packageManager = activity.getPackageManager();
        final String name = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        final long updatedTime = packageManager.getPackageInfo(packageName, 0).lastUpdateTime;
        final Drawable icon = packageManager.getApplicationIcon(packageName);
        return new AppInfo(name, packageName, updatedTime, ((BitmapDrawable) icon).getBitmap());
    }

    public static ArrayList<AppInfo> getData() {
        return data;
    }

}
