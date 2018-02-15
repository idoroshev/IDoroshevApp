package com.yandex.android.idoroshevapp;


import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;

import com.yandex.android.idoroshevapp.launcher.DesktopFragment;
import com.yandex.android.idoroshevapp.launcher.LauncherActivity;

public class OnScreenChangeListener extends ViewPager.SimpleOnPageChangeListener {
    private final Activity mActivity;

    public OnScreenChangeListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        NavigationView navigationView = ((LauncherActivity) mActivity).getNavigationView();
        navigationView.getMenu().findItem(R.id.nav_apps).setChecked(false);
        navigationView.getMenu().findItem(R.id.nav_desktop).setChecked(false);
        switch (position) {
            case 0:
                navigationView.getMenu().findItem(R.id.nav_desktop).setChecked(true);
                break;
            case 1:
                navigationView.getMenu().findItem(R.id.nav_apps).setChecked(true);
                break;
        }
    }
}

