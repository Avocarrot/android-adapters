package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;

import com.avocarrot.sdk.AdError;
import com.avocarrot.sdk.Avocarrot;
import com.avocarrot.sdk.InterstitialAd;
import com.avocarrot.sdk.InterstitialAdCallback;
import com.avocarrot.sdk.logger.Level;

import java.util.Map;

/* Compatible with Avocarrot SDK 4.+ */

public class AvocarrotInterstitialMopub extends CustomEventInterstitial {

    private static final String AD_UNIT_ID = "adUnitId";
    private static final String API_KEY = "apiKey";

    private static final String SANDBOX = "sandbox";
    private static final String LOGGER = "logger";

    CustomEventInterstitialListener customEventInterstitialListener;

    InterstitialAd mAvocarrotInterstitial;

    private InterstitialAdCallback mAvocarrorCallback = new InterstitialAdCallback() {
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            customEventInterstitialListener.onInterstitialLoaded();
        }

        @Override
        public void onAdError(AdError error) {
            super.onAdError(error);
            customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.UNSPECIFIED);
        }

        @Override
        public void onAdClicked() {
            super.onAdClicked();
            customEventInterstitialListener.onInterstitialClicked();
        }

        @Override
        public void onAdDisplayed() {
            super.onAdDisplayed();
            customEventInterstitialListener.onInterstitialShown();
        }

        @Override
        public void onAdDismissed() {
            super.onAdDismissed();
            customEventInterstitialListener.onInterstitialDismissed();
        }
    };

    @Override
    protected void loadInterstitial(Context context, CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {

        String adUnitId;
        String appKey;
        if (extrasAreValid(serverExtras)) {
            appKey = serverExtras.get(API_KEY);
            adUnitId = serverExtras.get(AD_UNIT_ID);
        } else {
            customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        InterstitialAd.Configuration.Builder configurationBuilder = new InterstitialAd.Configuration.Builder()
                .setApiKey(appKey)
                .setAdUnitId(adUnitId)
                .isMopubMediation()
                .setCallback(mAvocarrorCallback);

        try {
            if (serverExtras.containsKey(SANDBOX)) {
                configurationBuilder.setSandbox(Boolean.parseBoolean(serverExtras.get(SANDBOX)));
            }
        } catch (Exception e) {
            configurationBuilder.setSandbox(false);
        }
        try {
            if (serverExtras.containsKey(LOGGER)) {
                if (Boolean.parseBoolean(serverExtras.get(LOGGER))) {
                    configurationBuilder.setLogLevel(Level.DEBUG);
                }
            }
        } catch (Exception e) {
            configurationBuilder.setLogLevel(Level.ERROR);
        }

        this.customEventInterstitialListener = customEventInterstitialListener;
        mAvocarrotInterstitial = Avocarrot.build((Activity) context, configurationBuilder);
        mAvocarrotInterstitial.loadAd();

    }

    @Override
    protected void showInterstitial() {
        mAvocarrotInterstitial.showAd();
    }

    @Override
    protected void onInvalidate() {
        if (mAvocarrotInterstitial != null)
            mAvocarrotInterstitial.clear();
    }

    private boolean extrasAreValid(final Map<String, String> serverExtras) {
        return (serverExtras != null) && (serverExtras.containsKey(AD_UNIT_ID));
    }

}
