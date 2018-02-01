package com.yandex.android.idoroshevapp.launcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.android.idoroshevapp.LauncherActivity;
import com.yandex.android.idoroshevapp.data.Item;
import com.yandex.android.idoroshevapp.R;

import java.util.List;

public class LauncherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    @NonNull
    private final List<Item> mData;
    private final String TAG;

    public LauncherAdapter(@NonNull final List<Item> data, Context context) {
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
        final int squareColor = mData.get(position).getColor();
        final String hexColor = Integer.toHexString(squareColor).substring(2);
        imageView.setBackgroundColor(squareColor);
        textView.setText(hexColor);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                Snackbar snackbar = Snackbar.make(v, context.getString(R.string.delete_this_item), Snackbar.LENGTH_SHORT)
                        .setDuration(5000)
                        .setAction(context.getString(R.string.yes), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mData.remove(position);
                                LauncherAdapter.this.notifyDataSetChanged();
                                if (Log.isLoggable(TAG, Log.INFO)) {
                                    Log.i(TAG, context.getString(R.string.deleted_item) + 
                                            context.getString(R.string.position) + position + ", " + context.getString(R.string.color) + hexColor);
                                }
                            }
                        });
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                            final String description;
                            switch (event) {
                                case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                    description = context.getString(R.string.action_click);
                                    break;
                                case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                                    description = context.getString(R.string.new_snackbar_being_shown);
                                    break;
                                case Snackbar.Callback.DISMISS_EVENT_MANUAL:
                                    description = context.getString(R.string.call_to_dismiss);
                                    break;
                                case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                                    description = context.getString(R.string.swipe);
                                    break;
                                case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                    description = context.getString(R.string.timeout);
                                    break;
                                default:
                                    description = context.getString(R.string.unknown);
                                    break;
                            }
                        if (Log.isLoggable(TAG, Log.INFO)) {
                            Log.i(TAG, context.getString(R.string.snackbar_dismissed) + context.getString(R.string.cause) + description);
                        }
                    }
                });
                snackbar.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

