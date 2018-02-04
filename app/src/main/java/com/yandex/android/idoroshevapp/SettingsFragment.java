package com.yandex.android.idoroshevapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.android.idoroshevapp.data.AppInfo;

import java.util.Comparator;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    public static boolean skipWelcomePage(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
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
        String code = preferences.getString(SettingsActivity.KEY_LAYOUT, Layout.DEFAULT);
        return Layout.getColumnsId(code);
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
}
