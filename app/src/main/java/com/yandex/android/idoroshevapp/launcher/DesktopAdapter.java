package com.yandex.android.idoroshevapp.launcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.yandex.android.idoroshevapp.data.AppInfo;
import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.data.Database;

import java.util.LinkedList;
import java.util.List;

public class DesktopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    @NonNull
    private final List<AppInfo> mData;
    private final String TAG;
    private final String PACKAGE = "package";
    private Intent intent;

    public DesktopAdapter(@NonNull final List<AppInfo> data, Context context) {
        mData = data;
        this.context = context;
        TAG = context.getString(R.string.launcher_adapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new Holder.GridHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        bindGridView((Holder.GridHolder) holder, position);
    }

    private void bindGridView(@NonNull final Holder.GridHolder gridHolder, final int position) {
        final View imageView = gridHolder.getImageView();
        final TextView textView = gridHolder.getTextView();
        imageView.setBackground(mData.get(position).getIcon());
        textView.setText(mData.get(position).getName());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(mData.get(position).getPackageName());
                if (launchIntent != null) {
                    context.startActivity(launchIntent);
                    mData.get(position).updateLaunched();
                    Database.insertOrUpdateLaunched(mData.get(position));
                }
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUpMenu(v, position);
                return false;
            }
        });


    }

    private void showPopUpMenu(final View view, final int position) {
        final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.desktop_context_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.delete_from_desktop:
                        Database.removeFromDesktop(mData.get(position));
                        mData.remove(position);
                        notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}

