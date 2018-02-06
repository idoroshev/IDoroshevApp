package com.yandex.android.idoroshevapp.data;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.yandex.android.idoroshevapp.R;

import java.io.Serializable;

public class AppInfo implements Parcelable {
    private String name;
    private String packageName;
    private long updateTime;
    private long launched;
    private Bitmap icon;

    public AppInfo(final String name, final String packageName, final long updateTime, final Bitmap icon) {
        this.name = name;
        this.packageName = packageName;
        this.updateTime = updateTime;
        this.launched = Database.get(packageName);
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(packageName);
        out.writeLong(updateTime);
        out.writeLong(launched);
        out.writeParcelable(icon, PARCELABLE_WRITE_RETURN_VALUE);
    }

    public static final Parcelable.Creator<AppInfo> CREATOR
            = new Parcelable.Creator<AppInfo>() {
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    private AppInfo(Parcel in) {
        name = in.readString();
        packageName = in.readString();
        updateTime = in.readLong();
        launched = in.readLong();
        icon = in.readParcelable(ContentValues.class.getClassLoader());
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

    public Bitmap getIcon() {
        return icon;
    }
}
