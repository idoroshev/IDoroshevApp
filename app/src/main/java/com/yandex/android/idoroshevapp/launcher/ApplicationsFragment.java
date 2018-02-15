package com.yandex.android.idoroshevapp.launcher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.settings.LayoutType;

public class ApplicationsFragment extends Fragment {

    public static ApplicationsFragment newInstance() {
        return new ApplicationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragments_applications, container, false);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.applications,
                LayoutType.getLayoutFragment(LayoutType.getCurrent())).commitAllowingStateLoss();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}

