package com.nextsense.offlinemap;

import android.app.Application;

public class MapApp extends Application {
    public static MapApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MapApp getInstance() {
        return instance;
    }
}
