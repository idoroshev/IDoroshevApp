package com.yandex.android.idoroshevapp.data;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String name;
    private String packageName;
    private long updateTime;
    private long launched;
    private Drawable icon;

    public AppInfo(final String name, final String packageName, final long updateTime, final Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.updateTime = updateTime;
        this.launched = Database.getLaunched(packageName);
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public Long getLaunched() {
        return launched;
    }

    public void updateLaunched() {
        launched++;
    }

    public Drawable getIcon() {
        return icon;
    }
}
