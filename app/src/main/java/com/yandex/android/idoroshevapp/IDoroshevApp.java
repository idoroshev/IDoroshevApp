package com.yandex.android.idoroshevapp;

import android.app.Application;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import com.yandex.android.idoroshevapp.settings.SettingsFragment;
import com.yandex.android.idoroshevapp.welcome_page.WelcomePageActivity;
import com.yandex.metrica.YandexMetrica;

import io.fabric.sdk.android.Fabric;

public class IDoroshevApp extends Application {

    private final String API_KEY = "e3a017fb-e76d-4266-b624-1fdcb65377ea";
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        YandexMetrica.activate(getApplicationContext(), API_KEY);
        YandexMetrica.enableActivityAutoTracking(this);

        if (!SettingsFragment.skipWelcomePage(this)) {
            final Intent intent = new Intent();
            intent.setClass(this, WelcomePageActivity.class);
            startActivity(intent);
        }
    }
}
