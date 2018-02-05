package com.yandex.android.idoroshevapp;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yandex.android.idoroshevapp.data.AppInfo;
import com.yandex.android.idoroshevapp.launcher.LauncherAdapter;
import com.yandex.android.idoroshevapp.launcher.OffsetItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridLayoutFragment extends Fragment {

    private LauncherAdapter mLauncherAdapter;
    private Activity mActivity;
    private ArrayList<AppInfo> mData;
    private static final String DATA_KEY = "data";
    View view;

    public static GridLayoutFragment newInstance(final ArrayList<AppInfo> data) {
        GridLayoutFragment fragment = new GridLayoutFragment();
        Bundle args = new Bundle();
        int i = 0;
        Log.d("SUKA", String.valueOf(data.size()));
        for (AppInfo appInfo : data) {
            args.putSerializable(DATA_KEY + (i++), appInfo);
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mData = new ArrayList<>();
        Bundle bundle = getArguments();
        for (String key : bundle.keySet()) {
            mData.add((AppInfo) bundle.getSerializable(key));
        }
        mActivity = getActivity();
        Collections.sort(mData, SettingsFragment.getComparator(mActivity));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.content_launcher, container, false);

        createGridLayout();
        return view;
    }

    private void createGridLayout() {
        final RecyclerView recyclerView = view.findViewById(R.id.launcher_content);
        recyclerView.setHasFixedSize(true);
        final int offset = getResources().getDimensionPixelSize(R.dimen.item_offset);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        final int spanCount = getResources().getInteger(SettingsFragment.getLayoutColumnsId(mActivity));
        final GridLayoutManager layoutManager = new GridLayoutManager(mActivity, spanCount);
        recyclerView.setLayoutManager(layoutManager);


        Log.d("BLYAAAA", "PIZDA");
        mLauncherAdapter = new LauncherAdapter(mData, mActivity);
        recyclerView.setAdapter(mLauncherAdapter);
        LauncherActivity.launcherAdapter = mLauncherAdapter;
    }

}
