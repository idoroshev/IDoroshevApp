package com.yandex.android.idoroshevapp.data;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ItemStorage {
    private LinkedList<Item> data;

    public ItemStorage() {
        data = new LinkedList<>();
        generateData();
    }

    private void generateData() {
        data.clear();
        final Random rnd = new Random();
        for (int i = 0; i < 1000; i++) {
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            data.add(new Item(color, String.valueOf(color)));
        }
    }

    public void pushFront() {
        final Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        data.addFirst(new Item(color, String.valueOf(color)));
    }
    public void regenerateData() {
        generateData();
    }

    public List<Item> getData() {
        return data;
    }
}
