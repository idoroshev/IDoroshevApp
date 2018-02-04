package com.yandex.android.idoroshevapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.yandex.android.idoroshevapp.launcher.LauncherAdapter;

public class LayoutChoosingFragment extends Fragment {

    Button mNextButton;
    RadioButton mDefaultLayoutRadio;
    RadioButton mDenseLayoutRadio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!SettingsFragment.hasApplicationTheme(getActivity())) {
            SettingsFragment.setApplicationTheme(Theme.DEFAULT, getActivity());
        }
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_layout_choosing, container, false);
        getActivity().setTheme(SettingsFragment.getApplicationTheme(getActivity()));
        mNextButton = view.findViewById(R.id.next_button);
        mDefaultLayoutRadio = view.findViewById(R.id.default_layout_radio);
        mDenseLayoutRadio = view.findViewById(R.id.dense_layout_radio);

        if (SettingsFragment.getLayoutColumnsId(getActivity()) == Layout.getColumnsId("0")) {
            setDefaultLayout();
        } else {
            setDenseLayout();
        }

        mDefaultLayoutRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultLayout();
            }
        });

        mDenseLayoutRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDenseLayout();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SettingsFragment.hasLayout(getActivity())) {
                    SettingsFragment.setLayout(Layout.DEFAULT, getActivity());
                }
                Intent intent = new Intent(view.getContext(), LauncherActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK );
                getContext().startActivity(intent);
            }
        });
        return view;
    }

    private void setDefaultLayout() {
        mDefaultLayoutRadio.setChecked(true);
        mDenseLayoutRadio.setChecked(false);
        SettingsFragment.setLayout(Layout.DEFAULT, getActivity());
    }

    private void setDenseLayout() {
        mDefaultLayoutRadio.setChecked(false);
        mDenseLayoutRadio.setChecked(true);
        SettingsFragment.setLayout(Layout.DENSE, getActivity());
    }

}
