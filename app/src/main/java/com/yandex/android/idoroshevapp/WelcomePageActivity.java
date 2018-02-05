package com.yandex.android.idoroshevapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.ArrayList;
import java.util.List;

public class WelcomePageActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private WelcomeViewPagerAdapter mSectionsPagerAdapter;
    private List<Page> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        if (SettingsFragment.skipWelcomePage(this)) {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_welcome_page);
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
        }


        checkForUpdates();
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
