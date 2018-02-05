package com.yandex.android.idoroshevapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class ThemeChoosingFragment extends Fragment {

    RadioButton mLightThemeRadio;
    RadioButton mDarkThemeRadio;
    LinearLayout mLightThemeRadioWrapper;
    LinearLayout mDarkThemeRadioWrapper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_theme_choosing, container, false);
        mLightThemeRadio = view.findViewById(R.id.light_theme_radio);
        mDarkThemeRadio = view.findViewById(R.id.dark_theme_radio);
        mLightThemeRadioWrapper = view.findViewById(R.id.light_theme_radio_wrapper);
        mDarkThemeRadioWrapper = view.findViewById(R.id.dark_theme_radio_wrapper);

        if (SettingsFragment.getApplicationTheme(getActivity()) == Theme.getTheme("0")) {
            setLightTheme();
        } else {
            setDarkTheme();
        }

        mLightThemeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLightTheme();
            }
        });

        mDarkThemeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDarkTheme();
            }
        });

        return view;
    }

    private void setLightTheme() {
        mLightThemeRadio.setChecked(true);
        mDarkThemeRadio.setChecked(false);
        mDarkThemeRadioWrapper.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        mDarkThemeRadioWrapper.setBackground(getResources().getDrawable(R.drawable.border, null));
        SettingsFragment.setApplicationTheme(Theme.LIGHT, getActivity());
    }

    private void setDarkTheme() {
        mLightThemeRadio.setChecked(false);
        mDarkThemeRadio.setChecked(true);
        mDarkThemeRadioWrapper.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
        SettingsFragment.setApplicationTheme(Theme.DARK, getActivity());
    }

}
