package com.yandex.android.idoroshevapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yandex.android.idoroshevapp.data.Item;
import com.yandex.android.idoroshevapp.data.ItemStorage;
import com.yandex.android.idoroshevapp.launcher.LauncherAdapter;
import com.yandex.android.idoroshevapp.launcher.OffsetItemDecoration;
import com.yandex.android.idoroshevapp.list.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ItemStorage itemStorage = new ItemStorage();
    private FloatingActionButton fab;
    private ListAdapter mListAdapter;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        TAG = getString(R.string.list_activity);
        fab = findViewById(R.id.fab);
        createLinearLayout();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = itemStorage.pushFront();
                mListAdapter.notifyDataSetChanged();
                if (Log.isLoggable(TAG, Log.INFO)) {
                    Log.i(TAG, getString(R.string.added_new_item) + item.getColor());
                }
            }
        });
    }

    private void createLinearLayout() {
        final RecyclerView recyclerView = findViewById(R.id.list_content);
        recyclerView.setHasFixedSize(true);
        final int offset = getResources().getDimensionPixelSize(R.dimen.item_offset);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mListAdapter = new ListAdapter(itemStorage.getData(), getApplicationContext());
        recyclerView.setAdapter(mListAdapter);
    }

}
