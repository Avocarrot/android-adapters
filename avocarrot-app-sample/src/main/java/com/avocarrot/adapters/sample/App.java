package com.avocarrot.adapters.sample;

import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.avocarrot.sdk.Avocarrot;
import com.facebook.stetho.Stetho;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.squareup.leakcanary.LeakCanary;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Avocarrot.setDebugMode(true);
        Avocarrot.onApplicationCreated(this);
    }
}
