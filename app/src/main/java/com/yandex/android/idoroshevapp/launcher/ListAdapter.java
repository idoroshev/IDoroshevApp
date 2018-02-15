package com.yandex.android.idoroshevapp.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.android.idoroshevapp.data.AppInfo;
import com.yandex.android.idoroshevapp.R;
import com.yandex.android.idoroshevapp.data.DataStorage;
import com.yandex.android.idoroshevapp.data.Database;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    @NonNull
    private final List<AppInfo> mData;
    private final String TAG;
    private final String PACKAGE = "package";
    private Intent intent;

    public ListAdapter(@NonNull final List<AppInfo> data, Context context) {
        mData = data;
        this.context = context;
        TAG = context.getString(R.string.list_adapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new Holder.ListHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        bindGridView((Holder.ListHolder) holder, position);
    }

    private void bindGridView(@NonNull final Holder.ListHolder listHolder, final int position) {
        final View imageView = listHolder.getImageView();
        final TextView title = listHolder.getTitle();
        final TextView text = listHolder.getText();
        imageView.setBackground( mData.get(position).getIcon());
        title.setText(mData.get(position).getName());
        text.setText(mData.get(position).getPackageName());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(mData.get(position).getPackageName());
                if (launchIntent != null) {
                    context.startActivity(launchIntent);
                    mData.get(position).updateLaunched();
                    Database.insertOrUpdateLaunched(mData.get(position));
                    DataStorage.sortData((Activity) context);
                    notifyDataSetChanged();
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
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.launcher_context_menu);
        String title = (String) popupMenu.getMenu().findItem(R.id.launch_count).getTitle() + " ";
        title += mData.get(position).getLaunched();
        popupMenu.getMenu().findItem(R.id.launch_count).setTitle(title);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.delete:
                        intent = new Intent(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse(PACKAGE + ":" + mData.get(position).getPackageName()));
                        view.getContext().startActivity(intent);
                        break;
                    case R.id.info:
                        intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse(PACKAGE + ":" + mData.get(position).getPackageName()));
                        view.getContext().startActivity(intent);
                        break;
                    case R.id.add_to_desktop:
                        if (!Database.containsOnDesktop(mData.get(position))) {
                            Database.addToDesktop(mData.get(position));
                        } else {
                            Toast.makeText(context, R.string.already_on_desktop, Toast.LENGTH_SHORT).show();
                        }
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

