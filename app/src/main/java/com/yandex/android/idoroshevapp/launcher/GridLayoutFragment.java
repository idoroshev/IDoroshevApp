package com.yandex.android.idoroshevapp.launcher;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.data.AppInfo;
import com.yandex.android.idoroshevapp.data.DataStorage;
import com.yandex.android.idoroshevapp.settings.SettingsFragment;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;

public class GridLayoutFragment extends Fragment {

    private GridAdapter mGridAdapter;
    private Activity mActivity;
    private ArrayList<AppInfo> mData;
    private static final String DATA_KEY = "data";
    private final String GRID_LAYOUT_OPENED = "Grid layout opened";

    View view;

    public static GridLayoutFragment newInstance(final ArrayList<AppInfo> data) {
        GridLayoutFragment fragment = new GridLayoutFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        DataStorage.sortData(mActivity);
        mData = DataStorage.getData();
        YandexMetrica.reportEvent(GRID_LAYOUT_OPENED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.content_launcher, container, false);
        setNavigationView();
        createGridLayout();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setNavigationView();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mGridAdapter.notifyDataSetChanged();
    }

    private void createGridLayout() {
        final RecyclerView recyclerView = view.findViewById(R.id.launcher_content);
        final int offset = getResources().getDimensionPixelSize(R.dimen.item_offset);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        final int spanCount = getResources().getInteger(SettingsFragment.getLayoutColumnsId(mActivity));
        final GridLayoutManager layoutManager = new GridLayoutManager(mActivity, spanCount);
        recyclerView.setLayoutManager(layoutManager);


        mGridAdapter = new GridAdapter(mData, mActivity);
        recyclerView.setAdapter(mGridAdapter);
    }

    private void setNavigationView() {
        NavigationView navigationView = ((LauncherActivity) mActivity).getNavigationView();
        navigationView.getMenu().findItem(R.id.nav_grid).setChecked(true);
        navigationView.getMenu().findItem(R.id.nav_list).setChecked(false);
        navigationView.getMenu().findItem(R.id.nav_settings).setChecked(false);

    }
}
