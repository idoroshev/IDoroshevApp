package com.yandex.android.idoroshevapp.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import android.view.MenuItem;
import android.view.View;

import com.yandex.android.idoroshevapp.MainActivity;
import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.settings.SettingsActivity;
import com.yandex.android.idoroshevapp.data.AppInfo;
import com.yandex.android.idoroshevapp.data.Database;
import com.yandex.android.idoroshevapp.settings.SettingsFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LauncherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton fab;
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
                        appAdded(context, intent);
                        break;
                    case Intent.ACTION_PACKAGE_REMOVED:
                        appRemoved(context, intent);
                        break;
                    default:
                        return;
                }
                Collections.sort(mData, SettingsFragment.getComparator(LauncherActivity.this));
                if (launcherAdapter != null)
                    launcherAdapter.notifyDataSetChanged();
            }
        }

        private void appAdded(final Context context, final Intent intent) {
            String packageName = Uri.parse(intent.getDataString()).getSchemeSpecificPart();
            try {
                AppInfo appInfo = getAppInfoFromPackageName(packageName);
                mData.add(appInfo);
                Database.insertOrUpdate(appInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void appRemoved(final Context context, final Intent intent) {
            for (AppInfo appInfo : mData) {
                String packageName = Uri.parse(intent.getDataString()).getSchemeSpecificPart();
                if (packageName.equals(appInfo.getPackageName())) {
                    mData.remove(appInfo);
                    Database.remove(appInfo);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(SettingsFragment.getApplicationTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_nav_view);
        Database.initialize(this);
        TAG = getString(R.string.launcher_activity);
        fab = findViewById(R.id.fab);

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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });

        generateData();
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
        intentFilter.addDataScheme(PACKAGE);
        registerReceiver(monitor, intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(monitor);
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

    private void generateData() {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(0);
        for (ApplicationInfo applicationInfo : applicationInfoList) {
            try {
                if (packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
                    AppInfo appInfo = getAppInfoFromPackageName(applicationInfo.packageName);
                    if (!mData.contains(appInfo)) {
                        mData.add(appInfo);
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private AppInfo getAppInfoFromPackageName(final String packageName) throws PackageManager.NameNotFoundException {
        final PackageManager packageManager = getPackageManager();
        final String name = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        final long updatedTime = packageManager.getPackageInfo(packageName, 0).lastUpdateTime;
        final Drawable icon = packageManager.getApplicationIcon(packageName);
        return new AppInfo(name, packageName, updatedTime, icon);
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