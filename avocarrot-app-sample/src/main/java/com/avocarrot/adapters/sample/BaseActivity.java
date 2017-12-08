package com.avocarrot.adapters.sample;

import android.support.v7.app.AppCompatActivity;

import com.avocarrot.sdk.Avocarrot;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Avocarrot.onActivityResumed(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Avocarrot.onActivityPaused(this);
    }
}
