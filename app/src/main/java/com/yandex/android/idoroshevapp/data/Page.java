package com.yandex.android.idoroshevapp.data;

public class Page {
    private int layout;
    private int title;

    public Page(int layout, int title) {
        this.layout = layout;
        this.title = title;
    }

    public int getLayout() {
        return layout;
    }

    public int getTitle() {
        return title;
    }
}
