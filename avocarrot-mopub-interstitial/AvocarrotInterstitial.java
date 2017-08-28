package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.avocarrot.sdk.interstitial.InterstitialAd;
import com.avocarrot.sdk.interstitial.InterstitialAdPool;
import com.avocarrot.sdk.interstitial.listeners.InterstitialAdCallback;
import com.avocarrot.sdk.logger.Logger;
import com.avocarrot.sdk.mediation.ResponseStatus;

import java.util.Map;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class AvocarrotInterstitial extends CustomEventInterstitial implements InterstitialAdCallback {
    @NonNull
    private static final String AD_UNIT_KEY = "adUnit";
    @NonNull
    private static final String AD_UNIT_ID_KEY = "adUnitId";
    @Nullable
    private InterstitialAd interstitialAd;
    @Nullable
    private CustomEventInterstitialListener listener;

    //region CustomEventInterstitial
    @Override
    protected void loadInterstitial(@Nullable final Context context, @Nullable final CustomEventInterstitialListener listener,
                                    @Nullable final Map<String, Object> localExtras, @Nullable final Map<String, String> serverExtras) {
        if (listener == null) {
            Logger.warn("Failed to request banner ad, [listener] is null");
            return;
        }
        this.listener = listener;
        if (context == null) {
            Logger.warn("Failed to request banner ad, [context] is null");
            listener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        if (!(context instanceof Activity)) {
            Logger.warn("Failed to request banner ad, [context] is not an Activity instance");
            listener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        if (localExtras == null) {
            Logger.warn("Failed to request banner ad, [localExtras] is null");
            listener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        if (serverExtras == null) {
            Logger.warn("Failed to request banner ad, [serverExtras] is null");
            listener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        final String adUnitId = serverExtras.containsKey(AD_UNIT_ID_KEY) ? serverExtras.get(AD_UNIT_ID_KEY) : serverExtras.get(AD_UNIT_KEY);
        if (TextUtils.isEmpty(adUnitId)) {
            Logger.warn("Failed to request banner ad, [adUnitId] is empty");
            listener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        interstitialAd = InterstitialAdPool.load((Activity) context, adUnitId, this);
    }

    @Override
    protected void showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd.showAd();
        }
    }

    @Override
    protected void onInvalidate() {
        if (interstitialAd != null) {
            interstitialAd.onActivityDestroyed();
        }
    }
    //endregion

    //region InterstitialAdCallback
    @Override
    public void onAdLoaded(@NonNull final InterstitialAd interstitialAd) {
        if (listener != null) {
            listener.onInterstitialLoaded();
        }
    }

    @Override
    public void onAdFailed(@NonNull final InterstitialAd interstitialAd, @NonNull final ResponseStatus responseStatus) {
        if (listener != null) {
            final MoPubErrorCode error = (responseStatus == ResponseStatus.EMPTY) ? MoPubErrorCode.NO_FILL : MoPubErrorCode.NETWORK_INVALID_STATE;
            listener.onInterstitialFailed(error);
        }
    }

    @Override
    public void onAdOpened(@NonNull final InterstitialAd interstitialAd) {
        if (listener != null) {
            listener.onInterstitialShown();
        }
    }

    @Override
    public void onAdClicked(@NonNull final InterstitialAd interstitialAd) {
        if (listener != null) {
            listener.onInterstitialClicked();
            listener.onInterstitialDismissed();
        }
    }

    @Override
    public void onAdClosed(@NonNull final InterstitialAd interstitialAd) {
        if (listener != null) {
            listener.onInterstitialDismissed();
        }
    }
    //endregion
}
