package com.avocarrot.adapters.sample;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({IntentStarter.SCREEN_BANNER, IntentStarter.SCREEN_INTERSTITIAL, IntentStarter.SCREEN_NATIVE})
public @interface ScreenAdType {
}
