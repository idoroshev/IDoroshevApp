package com.yandex.android.idoroshevapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.yandex.android.idoroshevapp.data.Item;
import com.yandex.android.idoroshevapp.data.ItemStorage;
import com.yandex.android.idoroshevapp.launcher.LauncherAdapter;
import com.yandex.android.idoroshevapp.launcher.OffsetItemDecoration;
import com.yandex.android.idoroshevapp.list.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ItemStorage itemStorage = new ItemStorage();
    private FloatingActionButton fab;
    private ListAdapter mListAdapter;
    private DrawerLayout mDrawerLayout;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(SettingsFragment.getApplicationTheme(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nav_view);
        TAG = getString(R.string.list_activity);
        fab = findViewById(R.id.fab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View navigationHeaderView = navigationView.getHeaderView(0);
        final View profileImage = navigationHeaderView.findViewById(R.id.avatar);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent();

        switch (id) {
            case R.id.nav_launcher:
                intent.setClass(this, LauncherActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
