package com.yandex.android.idoroshevapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class DescriptionFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_description, container, false);

        final Button mNextButton = view.findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                Fragment fragment = new ThemeChoosingFragment();
                fm.beginTransaction().replace(R.id.welcome_page_fragment_container, fragment).
                        addToBackStack("theme_choosing").commit();
            }
        });

        return view;
    }

}
