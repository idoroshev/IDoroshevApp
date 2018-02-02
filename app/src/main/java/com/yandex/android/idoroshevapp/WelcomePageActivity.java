package com.yandex.android.idoroshevapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

public class WelcomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        if (SettingsFragment.skipWelcomePage(this)) {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivity(intent);
            finish();
        } else {
            SettingsFragment.setWelcomePageSwitchValue(this, false);
            setContentView(R.layout.activity_welcome_page);
        }
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.welcome_page_fragment_container);

        if (fragment == null) {
            fragment = new WelcomeFragment();
            fm.beginTransaction().add(R.id.welcome_page_fragment_container, fragment).commit();
        }

        checkForUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        // ... your own onResume implementation
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }
}
