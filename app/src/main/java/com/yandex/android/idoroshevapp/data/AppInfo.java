package com.yandex.android.idoroshevapp.data;

import android.graphics.drawable.Drawable;
import android.os.Parcelable;

import java.io.Serializable;

public class AppInfo implements Serializable {
    private String name;
    private String packageName;
    private long updateTime;
    private long launched;
    private Drawable icon;

    public AppInfo(final String name, final String packageName, final long updateTime, final Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.updateTime = updateTime;
        this.launched = Database.get(packageName);
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
