package com.avocarrot.adapters.sample;

import android.support.multidex.MultiDexApplication;

import com.avocarrot.sdk.Avocarrot;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Avocarrot.setDebugMode(true);
        Avocarrot.onApplicationCreated(this);
    }
}
