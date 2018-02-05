package com.yandex.android.idoroshevapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class WelcomeViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Page> mData = new ArrayList<>();
    private Activity mActivity;

    WelcomeViewPagerAdapter(@NonNull final FragmentManager fm, List<Page> data, Activity activity) {
        super(fm);
        mData = data;
        mActivity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WelcomeFragment();
            case 1:
                return new DescriptionFragment();
            case 2:
                return new ThemeChoosingFragment();
            case 3:
                return new LayoutChoosingFragment();
            case 4:
                return new Fragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return mActivity.getString(mData.get(position).getTitle());
    }
}
