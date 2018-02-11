package com.yandex.android.idoroshevapp.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.data.AppInfo;
import com.yandex.metrica.YandexMetrica;

import java.util.Comparator;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String THEME_SETTINGS_CHANGED = "Theme settings changed";
    public static final String LAYOUT_SETTINGS_CHANGED = "Layout settings changed";
    public static final String SORTING_TYPE_CHANGED = "Sorting type changed";
    public static final String WELCOME_PAGE_SETTINGS_CHANGED = "Welcome page settings changed";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(SettingsActivity.KEY_THEME, "Changed");
        setConfigChanged();
        switch (key) {
            case SettingsActivity.KEY_THEME:
                setThemeChanged();
                getActivity().recreate();
                YandexMetrica.reportEvent(THEME_SETTINGS_CHANGED);
                break;
            case SettingsActivity.KEY_LAYOUT:
                YandexMetrica.reportEvent(LAYOUT_SETTINGS_CHANGED);
                break;
            case SettingsActivity.KEY_SORTING_TYPE:
                YandexMetrica.reportEvent(SORTING_TYPE_CHANGED);
                break;
            case SettingsActivity.KEY_WELCOME_PAGE:
                YandexMetrica.reportEvent(WELCOME_PAGE_SETTINGS_CHANGED);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public static boolean isThemeChanged() {
        if (SettingsActivity.themeChanged) {
            SettingsActivity.themeChanged = false;
            return true;
        }
        return false;
    }

    public static void setThemeChanged() {
        SettingsActivity.themeChanged = true;
    }

    public static boolean isConfigChanged() {
        if (SettingsActivity.configChanged) {
            SettingsActivity.configChanged = false;
            SettingsActivity.themeChanged = false;
            return true;
        }
        return false;
    }

    public static void setConfigChanged() {
        SettingsActivity.configChanged = true;
    }

    public static boolean skipWelcomePage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean good;
        good = preferences.contains(SettingsActivity.KEY_LAYOUT);
        good &= preferences.contains(SettingsActivity.KEY_THEME);
        good &= (!preferences.getBoolean(SettingsActivity.KEY_WELCOME_PAGE, false));
        return good;
    }

    public static Comparator<AppInfo> getComparator(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String code = preferences.getString(SettingsActivity.KEY_SORTING_TYPE, AppComparator.DEFAULT);
        return AppComparator.getMethod(code);
    }

    public static void setWelcomePageSwitchValue(Activity activity, boolean value) {
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(SettingsActivity.KEY_WELCOME_PAGE, value);
        ed.apply();

    }

    public static int getApplicationTheme(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String code = preferences.getString(SettingsActivity.KEY_THEME, Theme.DEFAULT);
        return Theme.getTheme(code);
    }

    public static void setApplicationTheme(String theme, Activity activity) {
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SettingsActivity.KEY_THEME, theme);
        ed.apply();
    }

    public static boolean hasApplicationTheme(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.contains(SettingsActivity.KEY_THEME);
    }

    public static int getLayoutColumnsId(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String code = preferences.getString(SettingsActivity.KEY_LAYOUT, LayoutDensity.DEFAULT);
        return LayoutDensity.getColumnsId(code);
    }

    public static void setLayout(String layout, Activity activity) {
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SettingsActivity.KEY_LAYOUT, layout);
        ed.apply();
    }

    public static boolean hasLayout(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.contains(SettingsActivity.KEY_LAYOUT);
    }

    public static Fragment getLayoutFragment(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String code = preferences.getString(SettingsActivity.KEY_LAYOUT_TYPE, LayoutType.DEFAULT);
        return LayoutType.getLayoutFragment(code);
    }
}
