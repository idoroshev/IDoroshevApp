package com.yandex.android.idoroshevapp.welcome_page;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.settings.LayoutDensity;
import com.yandex.android.idoroshevapp.settings.SettingsFragment;
import com.yandex.android.idoroshevapp.settings.Theme;
import com.yandex.metrica.YandexMetrica;

public class LayoutChoosingFragment extends Fragment {

    RadioButton mDefaultLayoutRadio;
    RadioButton mDenseLayoutRadio;
    LinearLayout mDefaultLayoutRadioWrapper;
    LinearLayout mDenseLayoutRadioWrapper;

    private final String LAYOUT_WELCOME_PAGE_CHANGED = "Layout welcome page changed";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!SettingsFragment.hasApplicationTheme(getActivity())) {
            SettingsFragment.setApplicationTheme(Theme.DEFAULT, getActivity());
        }
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_layout_choosing, container, false);
        getActivity().setTheme(SettingsFragment.getApplicationTheme(getActivity()));
        mDefaultLayoutRadio = view.findViewById(R.id.default_layout_radio);
        mDenseLayoutRadio = view.findViewById(R.id.dense_layout_radio);
        mDefaultLayoutRadioWrapper = view.findViewById(R.id.default_layout_radio_wrapper);
        mDenseLayoutRadioWrapper = view.findViewById(R.id.dense_layout_radio_wrapper);

        if (SettingsFragment.getLayoutColumnsId(getActivity()) == LayoutDensity.getColumnsId("0")) {
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

        mDefaultLayoutRadioWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YandexMetrica.reportEvent(LAYOUT_WELCOME_PAGE_CHANGED);
                setDefaultLayout();
            }
        });

        mDenseLayoutRadioWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YandexMetrica.reportEvent(LAYOUT_WELCOME_PAGE_CHANGED);
                setDenseLayout();
            }
        });

        return view;
    }

    public static void setLayout(Activity activity) {
        if (!SettingsFragment.hasLayout(activity)) {
            SettingsFragment.setLayout(LayoutDensity.DEFAULT, activity);
        }
    }

    private void setDefaultLayout() {
        mDefaultLayoutRadio.setChecked(true);
        mDenseLayoutRadio.setChecked(false);
        SettingsFragment.setLayout(LayoutDensity.DEFAULT, getActivity());
    }

    private void setDenseLayout() {
        mDefaultLayoutRadio.setChecked(false);
        mDenseLayoutRadio.setChecked(true);
        SettingsFragment.setLayout(LayoutDensity.DENSE, getActivity());
    }

}
