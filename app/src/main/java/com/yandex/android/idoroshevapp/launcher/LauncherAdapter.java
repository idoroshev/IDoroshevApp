package com.yandex.android.idoroshevapp.launcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
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

    public LauncherAdapter(@NonNull final List<Item> data, Context context) {
        mData = data;
        this.context = context;
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
                            }
                        });
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {

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

