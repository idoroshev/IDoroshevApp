package com.yandex.android.idoroshevapp.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.yandex.android.idoroshevapp.ProfileActivity;
import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.data.DataStorage;
import com.yandex.android.idoroshevapp.settings.LayoutType;
import com.yandex.android.idoroshevapp.settings.SettingsActivity;
import com.yandex.android.idoroshevapp.data.AppInfo;
import com.yandex.android.idoroshevapp.data.Database;
import com.yandex.android.idoroshevapp.settings.SettingsFragment;
import com.yandex.android.idoroshevapp.welcome_page.WelcomePageActivity;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class LauncherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private String TAG;
    private ArrayList<AppInfo> mData = new ArrayList<>();
    private final String PACKAGE = "package";
    private static final String APP_ADDED = "App added";
    private static final String APP_DELETED = "App deleted";
    private static final String LAUNCHER_ORIENTATION_LANDSCAPE = "Launcher orientation landscape";
    private static final String LAUNCHER_ORIENTATION_PORTRAIT = "Launcher orientation portrait";
    public static final String KEY_LAST_ORIENTATION = "last_orientation";
    private int lastOrientation;
    private Fragment mFragment;
    private NavigationView navigationView;

    private BroadcastReceiver monitor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case Intent.ACTION_PACKAGE_ADDED:
                        YandexMetrica.reportEvent(APP_ADDED);
                        mData = DataStorage.appAdded(LauncherActivity.this, intent);
                        break;
                    case Intent.ACTION_PACKAGE_REMOVED:
                        YandexMetrica.reportEvent(APP_DELETED);
                        mData = DataStorage.appRemoved(LauncherActivity.this, intent);
                        break;
                    default:
                        break;
                }
                DataStorage.sortData(LauncherActivity.this);
                mFragment.onConfigurationChanged(null);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setTheme(SettingsFragment.getApplicationTheme(this));
        setContentView(R.layout.activity_launcher_nav_view);
        Database.initialize(this);
        TAG = getString(R.string.launcher_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View navigationHeaderView = navigationView.getHeaderView(0);


        final View profileImage = navigationHeaderView.findViewById(R.id.avatar);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LauncherActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        DataStorage.generateData(this);

        if (savedInstanceState == null) {
            lastOrientation = getResources().getConfiguration().orientation;
            setLayout();
        }

    }

    private void setLayout() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = SettingsFragment.getLayoutFragment(this);
        fragmentManager.beginTransaction().
                replace(R.id.launcher_fragment_container, mFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrientationChanged();
        if (SettingsFragment.isConfigChanged()) {
            recreate();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addDataScheme(PACKAGE);
        registerReceiver(monitor, intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //unregisterReceiver(monitor);
        for (AppInfo appInfo : mData) {
            Database.insertOrUpdate(appInfo);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            YandexMetrica.reportEvent(LAUNCHER_ORIENTATION_PORTRAIT);
        } else {
            YandexMetrica.reportEvent(LAUNCHER_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.nav_grid:
                LayoutType.setCurrent(LayoutType.GRID);
                setLayout();
                break;
            case R.id.nav_list:
                LayoutType.setCurrent(LayoutType.LINEAR);
                setLayout();
                break;
            case R.id.nav_settings:
                intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }
}
