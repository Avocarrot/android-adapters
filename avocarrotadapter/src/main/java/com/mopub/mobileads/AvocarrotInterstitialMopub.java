package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;

import com.avocarrot.androidsdk.AdError;
import com.avocarrot.androidsdk.AvocarrotInterstitial;
import com.avocarrot.androidsdk.AvocarrotInterstitialListener;

import java.util.Map;

/* Compatible with Avocarrot SDK 3.3.4+ */

public class AvocarrotInterstitialMopub extends CustomEventInterstitial {

    private static final String PLACEMENT_KEY = "placementKey";
    private static final String API_KEY = "apiKey";

    private static final String SANDBOX = "sandbox";
    private static final String LOGGER = "logger";

    CustomEventInterstitialListener customEventInterstitialListener;

    AvocarrotInterstitial mAvocarrotInterstitial;

    AvocarrotInterstitialListener mAvocarrorListener = new AvocarrotInterstitialListener() {
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

        final String placement;
        final String appId;
        if (extrasAreValid(serverExtras)) {
            placement = serverExtras.get(PLACEMENT_KEY);
            appId = serverExtras.get(API_KEY);
        } else {
            customEventInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        mAvocarrotInterstitial = new AvocarrotInterstitial((Activity)context, appId, placement, "mopub");

        boolean sandbox = false;
        try {
            if (serverExtras.containsKey(SANDBOX)) {
                sandbox = Boolean.parseBoolean(serverExtras.get(SANDBOX));
            }
        } catch (Exception e) {
            sandbox = false;
        }
        boolean logger = false;
        try {
            if (serverExtras.containsKey(LOGGER)) {
                logger = Boolean.parseBoolean(serverExtras.get(LOGGER));
            }
        } catch (Exception e) {
            logger = false;
        }

        mAvocarrotInterstitial.setSandbox(sandbox);
        mAvocarrotInterstitial.setLogger(logger, "ALL");

        mAvocarrotInterstitial.loadAd();
        mAvocarrotInterstitial.setListener(mAvocarrorListener);
        this.customEventInterstitialListener = customEventInterstitialListener;

    }

    @Override
    protected void showInterstitial() {
        mAvocarrotInterstitial.showAd();
    }

    @Override
    protected void onInvalidate() {
        if(mAvocarrotInterstitial!=null)
            mAvocarrotInterstitial.clear();
    }

    private boolean extrasAreValid(final Map<String, String> serverExtras) {
        return (serverExtras!=null) && serverExtras.containsKey(PLACEMENT_KEY) && (serverExtras.containsKey(API_KEY));
    }

}
