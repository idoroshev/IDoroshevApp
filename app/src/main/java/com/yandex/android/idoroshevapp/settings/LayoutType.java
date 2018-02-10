package com.yandex.android.idoroshevapp.settings;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.yandex.android.idoroshevapp.data.DataStorage;
import com.yandex.android.idoroshevapp.launcher.GridLayoutFragment;
import com.yandex.android.idoroshevapp.launcher.LinearLayoutFragment;

public class LayoutType {
    public static final String GRID = "0";
    public static final String LINEAR = "1";

    static final String DEFAULT = GRID;

    private static String current = null;
    
    static Fragment getLayoutFragment(String code) {
        String layout = (current == null ? code : current);
        switch (layout) {
            case LayoutType.GRID:
                return GridLayoutFragment.newInstance(DataStorage.getData());
            case LayoutType.LINEAR:
                return LinearLayoutFragment.newInstance(DataStorage.getData());
            default:
                return LayoutType.getLayoutFragment(LayoutType.DEFAULT);
        }
    }

    public static void setCurrent(String code) {
        current = code;
    }
}
