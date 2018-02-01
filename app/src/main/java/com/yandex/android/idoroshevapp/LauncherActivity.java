package com.yandex.android.idoroshevapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yandex.android.idoroshevapp.data.Item;
import com.yandex.android.idoroshevapp.data.ItemStorage;
import com.yandex.android.idoroshevapp.launcher.LauncherAdapter;
import com.yandex.android.idoroshevapp.launcher.OffsetItemDecoration;

import java.util.ArrayList;

public class LauncherActivity extends AppCompatActivity {
    private boolean isDefaultLayout;
    private ItemStorage itemStorage = new ItemStorage();
    private FloatingActionButton fab;
    private LauncherAdapter mLauncherAdapter;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        TAG = getString(R.string.launcher_activity);
        isDefaultLayout = getIntent().getBooleanExtra(getString(R.string.is_default_layout), true);
        fab = findViewById(R.id.fab);
        createGridLayout();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = itemStorage.pushFront();
                mLauncherAdapter.notifyDataSetChanged();
                if (Log.isLoggable(TAG, Log.INFO)) {
                    Log.i(TAG, getString(R.string.added_new_item) + item.getColor());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }

    private void createGridLayout() {
        final RecyclerView recyclerView = findViewById(R.id.launcher_content);
        recyclerView.setHasFixedSize(true);
        final int offset = getResources().getDimensionPixelSize(R.dimen.item_offset);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        final int spanCount = getResources().
                getInteger(isDefaultLayout ? R.integer.icons_default_count : R.integer.icons_dense_count);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        mLauncherAdapter = new LauncherAdapter(itemStorage.getData(), getApplicationContext());
        recyclerView.setAdapter(mLauncherAdapter);
    }

}
