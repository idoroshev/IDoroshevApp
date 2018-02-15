package com.yandex.android.idoroshevapp.launcher;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
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
import com.yandex.android.idoroshevapp.data.Database;
import com.yandex.android.idoroshevapp.settings.SettingsFragment;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DesktopFragment extends Fragment {

    private  DesktopAdapter mDesktopAdapter;
    private Activity mActivity;
    private ArrayList<AppInfo> mData;
    private static final String DATA_KEY = "data";
    private final String DESKTOP_OPENED = "Desktop opened";

    View view;

    public static DesktopFragment newInstance() {
        DesktopFragment fragment = new DesktopFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        DataStorage.sortData(mActivity);
        mData = (ArrayList<AppInfo>) getOnDesktop(DataStorage.getData());
        YandexMetrica.reportEvent(DESKTOP_OPENED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.content_launcher, container, false);
        setNavigationView();
        createGridLayout(true);
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDesktopAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mData = (ArrayList<AppInfo>) getOnDesktop(DataStorage.getData());
            if (view != null) createGridLayout(false);
        }
    }

    private void createGridLayout(boolean addOffset) {
        final RecyclerView recyclerView = view.findViewById(R.id.launcher_content);
        final int offset = getResources().getDimensionPixelSize(R.dimen.item_offset);
        if (addOffset) recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        final int spanCount = getResources().getInteger(SettingsFragment.getLayoutColumnsId(mActivity));
        final GridLayoutManager layoutManager = new GridLayoutManager(mActivity, spanCount);
        recyclerView.setLayoutManager(layoutManager);


        mDesktopAdapter = new DesktopAdapter(mData, mActivity);
        recyclerView.setAdapter(mDesktopAdapter);
    }

    private void setNavigationView() {
        NavigationView navigationView = ((LauncherActivity) mActivity).getNavigationView();
        navigationView.getMenu().findItem(R.id.nav_grid).setChecked(true);
        navigationView.getMenu().findItem(R.id.nav_list).setChecked(false);
        navigationView.getMenu().findItem(R.id.nav_settings).setChecked(false);
    }

    private static List<AppInfo> getOnDesktop(@NonNull List<AppInfo> data) {
        List<AppInfo> temp = new ArrayList<>();
        Log.d("TAGG", "get");
        for (int i = 0; i < data.size(); i++) {
            if (Database.containsOnDesktop(data.get(i))) {
                Log.d("TAGG", "hmhmh");
                temp.add(data.get(i));
            }
        }
        return temp;
    }
}
