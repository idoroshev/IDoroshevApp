package com.yandex.android.idoroshevapp.settings;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yandex.android.idoroshevapp.R;
import com.yandex.metrica.YandexMetrica;


public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_THEME = "theme";
    public static final String KEY_LAYOUT = "layout";
    public static final String KEY_WELCOME_PAGE = "welcome_page";
    public static final String KEY_SORTING_TYPE = "sorting_type";
    public static final String KEY_LAYOUT_TYPE = "layout_type";
    private final String SETTINGS_OPENED = "Settings opened";

    private static final String SETTINGS_ORIENTATION_LANDSCAPE = "Settings orientation landscape";
    private static final String SETTINGS_ORIENTATION_PORTRAIT = "Settings orientation portrait";

    static boolean themeChanged;
    static boolean configChanged;

    public static final String KEY_LAST_ORIENTATION = "last_orientation";
    private int lastOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(SettingsFragment.getApplicationTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        YandexMetrica.reportEvent(SETTINGS_OPENED);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            lastOrientation = getResources().getConfiguration().orientation;
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.settings_fragment_container, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrientationChanged();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOrientation = savedInstanceState.getInt(KEY_LAST_ORIENTATION);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_LAST_ORIENTATION, lastOrientation);
    }

    private void checkOrientationChanged() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation != lastOrientation) {
            onScreenOrientationChanged(currentOrientation);
            lastOrientation = currentOrientation;
        }
    }

    public void onScreenOrientationChanged(int currentOrientation) {
        if (currentOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            YandexMetrica.reportEvent(SETTINGS_ORIENTATION_PORTRAIT);
        } else {
            YandexMetrica.reportEvent(SETTINGS_ORIENTATION_LANDSCAPE);
        }
    }
}

