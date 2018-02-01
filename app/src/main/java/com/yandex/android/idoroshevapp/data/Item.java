package com.yandex.android.idoroshevapp.data;

public class Item {
    private int color;
    private String text;

    public Item(int color, String text) {
        this.color = color;
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public String getText() {
        return text;
    }
}
