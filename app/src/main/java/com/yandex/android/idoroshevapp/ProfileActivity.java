package com.yandex.android.idoroshevapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.yandex.android.idoroshevapp.launcher.Holder;
import com.yandex.android.idoroshevapp.launcher.OffsetItemDecoration;
import com.yandex.android.idoroshevapp.settings.SettingsFragment;
import com.yandex.android.idoroshevapp.welcome_page.WelcomePageActivity;

import io.fabric.sdk.android.Fabric;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(SettingsFragment.getApplicationTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.profile_content);
        final int offset = getResources().getDimensionPixelSize(R.dimen.item_offset);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                this));

        recyclerView.setAdapter(new ProfileAdapter(this));
    }

}

class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;

    private List<InfoEntry> data = new ArrayList<InfoEntry>() {{
        add(new InfoEntry(R.string.mobile_number, R.string.mobile, R.drawable.ic_phone));
        add(new InfoEntry(R.string.home_number, R.string.home, R.drawable.ic_home));
        add(new InfoEntry(R.string.email_address, R.string.email, R.drawable.ic_email));
        add(new InfoEntry(R.string.my_address, R.string.address, R.drawable.ic_location_on));
        add(new InfoEntry(R.string.my_vk, R.string.vk, R.drawable.ic_vk));
        add(new InfoEntry(R.string.my_telegram, R.string.telegram, R.drawable.ic_telegram));
        add(new InfoEntry(R.string.my_github_link, R.string.github, R.drawable.ic_github));
    }};

    public ProfileAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new Holder.ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindGridView((Holder.ListHolder) holder, position);
    }

    private void bindGridView(@NonNull final Holder.ListHolder gridHolder, final int position) {
        gridHolder.getImageView().setBackground(activity.getDrawable(data.get(position).icon));
        gridHolder.getTitle().setText(data.get(position).title);
        gridHolder.getText().setText(data.get(position).subtitle);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class InfoEntry {
        Integer title;
        Integer subtitle;
        Integer icon;

        InfoEntry(Integer title, Integer subtitle, Integer icon) {
            this.title = title;
            this.subtitle = subtitle;
            this.icon = icon;
        }
    }
}
