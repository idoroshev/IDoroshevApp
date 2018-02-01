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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_theme_choosing, container, false);
        final Button mNextButton = view.findViewById(R.id.next_button);
        final RadioButton mLightThemeRadio = view.findViewById(R.id.light_theme_radio);
        final RadioButton mDarkThemeRadio = view.findViewById(R.id.dark_theme_radio);
        final LinearLayout mLightThemeRadioWrapper = view.findViewById(R.id.light_theme_radio_wrapper);
        final LinearLayout mDarkThemeRadioWrapper = view.findViewById(R.id.dark_theme_radio_wrapper);

        mLightThemeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLightThemeRadio.setChecked(true);
                mDarkThemeRadio.setChecked(false);
                mDarkThemeRadioWrapper.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                mDarkThemeRadioWrapper.setBackground(getResources().getDrawable(R.drawable.border, null));
                SettingsFragment.setApplicationTheme(Theme.LIGHT, getActivity());
            }
        });

        mDarkThemeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLightThemeRadio.setChecked(false);
                mDarkThemeRadio.setChecked(true);
                mDarkThemeRadioWrapper.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
                SettingsFragment.setApplicationTheme(Theme.DARK, getActivity());
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                Fragment fragment = new LayoutChoosingFragment();
                fm.beginTransaction().replace(R.id.welcome_page_fragment_container, fragment).
                        addToBackStack("layout_choosing").commit();
            }
        });

        return view;
    }

}
