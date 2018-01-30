package com.yandex.android.idoroshevapp.launcher;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yandex.android.idoroshevapp.R;

class Holder {

    static class GridHolder extends RecyclerView.ViewHolder {

        private final View mImageView;
        private final TextView mTextView;

        GridHolder(final View view) {
            super(view);

            mImageView = view.findViewById(R.id.launcher_image);
            mTextView = view.findViewById(R.id.launcher_text);
        }

        View getImageView() {
            return mImageView;
        }

        TextView getTextView() {
            return mTextView;
        }
    }
}
