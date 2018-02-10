package com.yandex.android.idoroshevapp.launcher;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.data.AppInfo;
import com.yandex.android.idoroshevapp.data.DataStorage;
import com.yandex.android.idoroshevapp.settings.SettingsFragment;

import java.util.ArrayList;
import java.util.Collections;

public class LinearLayoutFragment extends Fragment {

    private ListAdapter mAdapter;
    private Activity mActivity;
    private ArrayList<AppInfo> mData;
    private static final String DATA_KEY = "data";
    View view;

    public static LinearLayoutFragment newInstance(final ArrayList<AppInfo> data) {
        LinearLayoutFragment fragment = new LinearLayoutFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        DataStorage.sortData(mActivity);
        mData = DataStorage.getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.content_launcher, container, false);

        setNavigationView();
        createLinearLayout();
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
        mAdapter.notifyDataSetChanged();
    }

    private void createLinearLayout() {
        final RecyclerView recyclerView = view.findViewById(R.id.launcher_content);
        final int offset = getResources().getDimensionPixelSize(R.dimen.item_offset);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));


        final LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        final DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);



        mAdapter = new ListAdapter(mData, mActivity);
        recyclerView.setAdapter(mAdapter);
    }

    private void setNavigationView() {
        NavigationView navigationView = ((LauncherActivity) mActivity).getNavigationView();
        navigationView.getMenu().findItem(R.id.nav_grid).setChecked(false);
        navigationView.getMenu().findItem(R.id.nav_list).setChecked(true);
        navigationView.getMenu().findItem(R.id.nav_settings).setChecked(false);
    }

}
