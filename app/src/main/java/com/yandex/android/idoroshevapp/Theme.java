package com.yandex.android.idoroshevapp;

public class Theme {
    public static final String LIGHT = "0";
    public static final String DARK = "1";
    public static final String DEFAULT = LIGHT;

    static int getTheme(String code) {
        switch (code) {
            case "0": return R.style.AppTheme;
            case "1": return R.style.AppThemeDark;
            default: return R.style.AppTheme;
        }
    }

}
