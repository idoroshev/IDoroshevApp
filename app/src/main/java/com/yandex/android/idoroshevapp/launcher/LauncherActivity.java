package com.yandex.android.idoroshevapp.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.yandex.android.idoroshevapp.MainActivity;
import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.data.DataStorage;
import com.yandex.android.idoroshevapp.settings.SettingsActivity;
import com.yandex.android.idoroshevapp.data.AppInfo;
import com.yandex.android.idoroshevapp.data.Database;
import com.yandex.android.idoroshevapp.settings.SettingsFragment;
import com.yandex.android.idoroshevapp.welcome_page.WelcomePageActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class LauncherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private String TAG;
    private ArrayList<AppInfo> mData = new ArrayList<>();
    private final String PACKAGE = "package";
    protected static RecyclerView.Adapter launcherAdapter;

    private BroadcastReceiver monitor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case Intent.ACTION_PACKAGE_ADDED:
                        mData = DataStorage.appAdded(LauncherActivity.this, intent);
                        break;
                    case Intent.ACTION_PACKAGE_REMOVED:
                        mData = DataStorage.appRemoved(LauncherActivity.this, intent);
                        break;
                    default:
                        break;
                }
                Collections.sort(mData, SettingsFragment.getComparator(LauncherActivity.this));
                if (launcherAdapter != null) {

                    launcherAdapter.notifyDataSetChanged();
                }
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        super.onCreate(savedInstanceState);
        if (!SettingsFragment.skipWelcomePage(this)) {
            final Intent intent = new Intent();
            intent.setClass(this, WelcomePageActivity.class);
            startActivity(intent);
            finish();
        }
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


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View navigationHeaderView = navigationView.getHeaderView(0);
        final View profileImage = navigationHeaderView.findViewById(R.id.avatar);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mData = DataStorage.generateData(this);
        Collections.sort(mData, SettingsFragment.getComparator(LauncherActivity.this));
        if (savedInstanceState == null) {
            setGridLayout();
        }

    }

    private void setGridLayout() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        GridLayoutFragment fragment = GridLayoutFragment.newInstance(mData);
        fragmentManager.beginTransaction().
                replace(R.id.launcher_fragment_container, fragment).commit();
    }

    private void setLinearLayout() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        LinearLayoutFragment fragment = LinearLayoutFragment.newInstance(mData);
        fragmentManager.beginTransaction().
                replace(R.id.launcher_fragment_container, fragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.nav_launcher:
                setGridLayout();
                break;
            case R.id.nav_list:
                setLinearLayout();
                break;
            case R.id.nav_settings:
                intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
