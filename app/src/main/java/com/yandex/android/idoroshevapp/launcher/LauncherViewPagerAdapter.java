package com.yandex.android.idoroshevapp.launcher;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yandex.android.idoroshevapp.settings.LayoutType;

public class LauncherViewPagerAdapter extends FragmentStatePagerAdapter {

    public LauncherViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DesktopFragment.newInstance();
            case 1:
                return ApplicationsFragment.newInstance();
            default:
                return DesktopFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
