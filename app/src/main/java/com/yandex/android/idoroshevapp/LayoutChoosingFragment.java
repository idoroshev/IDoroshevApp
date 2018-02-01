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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_layout_choosing, container, false);
        final Button mNextButton = view.findViewById(R.id.next_button);
        final RadioButton mDefaultLayoutRadio = view.findViewById(R.id.default_layout_radio);
        final RadioButton mDenseLayoutRadio = view.findViewById(R.id.dense_layout_radio);

        mDefaultLayoutRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDefaultLayoutRadio.setChecked(true);
                mDenseLayoutRadio.setChecked(false);
            }
        });

        mDenseLayoutRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDefaultLayoutRadio.setChecked(false);
                mDenseLayoutRadio.setChecked(true);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LauncherActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK );
                intent.putExtra("isDefaultLayout", mDefaultLayoutRadio.isChecked());
                getContext().startActivity(intent);
            }
        });
        return view;
    }

}
