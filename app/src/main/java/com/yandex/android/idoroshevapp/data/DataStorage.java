package com.yandex.android.idoroshevapp.data;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.yandex.android.idoroshevapp.settings.SettingsFragment;

import java.util.ArrayList;
import java.util.Collections;
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
        sortData(activity);
        return data;
    }

    public static AppInfo getAppInfoFromPackageName(final String packageName, Activity activity) throws PackageManager.NameNotFoundException {
        final PackageManager packageManager = activity.getPackageManager();
        final String name = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        final long updatedTime = packageManager.getPackageInfo(packageName, 0).lastUpdateTime;
        final Drawable icon = packageManager.getApplicationIcon(packageName);
        return new AppInfo(name, packageName, updatedTime, icon);
    }

    public static ArrayList<AppInfo> getData() {
        return data;
    }

    public static ArrayList<AppInfo> add(AppInfo appInfo) {
        data.add(appInfo);
        return data;
    }

    public static ArrayList<AppInfo> remove(AppInfo appInfo) {
        data.remove(appInfo);
        return data;
    }

    public static ArrayList<AppInfo> appAdded(final Activity activity, final Intent intent) {
        String packageName = Uri.parse(intent.getDataString()).getSchemeSpecificPart();
        try {
            AppInfo appInfo = DataStorage.getAppInfoFromPackageName(packageName, activity);
            DataStorage.add(appInfo);
            Database.insertOrUpdateLaunched(appInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static ArrayList<AppInfo> appRemoved(final Activity activity, final Intent intent) {
        for (AppInfo appInfo : data) {
            String packageName = Uri.parse(intent.getDataString()).getSchemeSpecificPart();
            if (packageName.equals(appInfo.getPackageName())) {
                DataStorage.remove(appInfo);
                Database.removeLaunched(appInfo);
                break;
            }
        }
        return data;
    }

    public static void sortData(Activity activity) {
        Collections.sort(data, SettingsFragment.getComparator(activity));
    }

}
