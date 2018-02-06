package com.yandex.android.idoroshevapp.settings;

import com.yandex.android.idoroshevapp.R;

public class Layout {
    public static final String STANDARD = "0";
    public static final String DENSE = "1";
    public static final String DEFAULT = STANDARD;

    public static int getColumnsId(String code) {
        switch (code) {
            case STANDARD:
                return R.integer.icons_default_count;
            case DENSE:
                return R.integer.icons_dense_count;
            default:
                return R.integer.icons_default_count;
        }
    }
}
