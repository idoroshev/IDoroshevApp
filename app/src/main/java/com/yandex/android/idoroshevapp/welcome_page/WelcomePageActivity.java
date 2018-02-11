package com.yandex.android.idoroshevapp.welcome_page;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.data.Page;
import com.yandex.android.idoroshevapp.launcher.LauncherActivity;
import com.yandex.android.idoroshevapp.settings.SettingsFragment;
import com.yandex.metrica.YandexMetrica;

import io.fabric.sdk.android.Fabric;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.ArrayList;
import java.util.List;

public class WelcomePageActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private WelcomeViewPagerAdapter mSectionsPagerAdapter;
    private List<Page> data = new ArrayList<>();
    private final String WELCOME_PAGE_OPENED = "Welcome page opened";
    private static final String WELCOME_PAGE_ORIENTATION_LANDSCAPE = "Welcome page orientation landscape";
    private static final String WELCOME_PAGE_ORIENTATION_PORTRAIT = "Welcome page orientation portrait";

    public static final String KEY_LAST_ORIENTATION = "last_orientation";
    private int lastOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsFragment.getApplicationTheme(this));
        setContentView(R.layout.activity_welcome_page);

        YandexMetrica.reportEvent(WELCOME_PAGE_OPENED);

        if (savedInstanceState == null) {
            lastOrientation = getResources().getConfiguration().orientation;
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.welcome_page_fragment_container);

        final FragmentManager fragmentManager = getSupportFragmentManager();


        prepareData();
        mSectionsPagerAdapter = new WelcomeViewPagerAdapter(fragmentManager, data, this);

        mViewPager = (ViewPager) findViewById(R.id.welcome_page_fragment_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == data.size() - 1) {
                    LayoutChoosingFragment.setLayout(WelcomePageActivity.this);
                    SettingsFragment.setWelcomePageSwitchValue(WelcomePageActivity.this, false);
                    Intent intent = new Intent(WelcomePageActivity.this, LauncherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK );
                    WelcomePageActivity.this.startActivity(intent);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        checkForUpdates();
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
            YandexMetrica.reportEvent(WELCOME_PAGE_ORIENTATION_PORTRAIT);
        } else {
            YandexMetrica.reportEvent(WELCOME_PAGE_ORIENTATION_LANDSCAPE);
        }
    }

    private void prepareData() {
        data.add(new Page(R.layout.fragment_welcome, R.string.welcome));
        data.add(new Page(R.layout.fragment_description, R.string.description));
        data.add(new Page(R.layout.fragment_theme_choosing, R.string.theme));
        data.add(new Page(R.layout.fragment_layout_choosing, R.string.layout));
        data.add(new Page(0, R.string.get_started));
    }

    @Override
    public void onResume() {
        super.onResume();
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
