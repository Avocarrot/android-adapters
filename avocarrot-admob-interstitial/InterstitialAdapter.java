package com.avocarrot.adapter.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.avocarrot.sdk.Avocarrot;
import com.avocarrot.sdk.interstitial.InterstitialAd;
import com.avocarrot.sdk.interstitial.InterstitialAdPool;
import com.avocarrot.sdk.interstitial.listeners.InterstitialAdCallback;
import com.avocarrot.sdk.logger.Logger;
import com.avocarrot.sdk.mediation.ResponseStatus;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class InterstitialAdapter implements CustomEventInterstitial {
    @Nullable
    private InterstitialAd interstitialAd;

    //region CustomEventInterstitial
    @Override
    public void requestInterstitialAd(@Nullable final Context context, @Nullable final CustomEventInterstitialListener listener,
                                      @Nullable final String adUnitId, @Nullable final MediationAdRequest adRequest,
                                      @Nullable final Bundle customEventExtras) {
        if (listener == null) {
            Logger.warn("Failed to request banner ad, [listener] is null");
            return;
        }
        if (context == null) {
            Logger.warn("Failed to request banner ad, [context] is null");
            listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
            return;
        }
        if (!(context instanceof Activity)) {
            Logger.warn("Failed to request banner ad, [context] is not an Activity instance");
            listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }
        if (TextUtils.isEmpty(adUnitId)) {
            Logger.warn("Failed to request banner ad, [adUnitId] is null");
            listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
            return;
        }
        if (adRequest == null) {
            Logger.warn("Failed to request banner ad, [adRequest] is null");
            listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }
        Avocarrot.setTestMode(adRequest.isTesting());

        interstitialAd = InterstitialAdPool.load((Activity) context, adUnitId, new AdCallback(listener));
    }

    @Override
    public void showInterstitial() {
        if (interstitialAd != null && interstitialAd.isReady()) {
            interstitialAd.showAd();
        }
    }

    @Override
    public void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.onActivityDestroyed();
        }
    }

    @Override
    public void onPause() {
        if (interstitialAd != null) {
            interstitialAd.onActivityPaused();
        }
    }

    @Override
    public void onResume() {
        if (interstitialAd != null) {
            interstitialAd.onActivityResumed();
        }
    }
    //endregion

    private static class AdCallback implements InterstitialAdCallback {
        @NonNull
        private final CustomEventInterstitialListener listener;

        public AdCallback(@NonNull final CustomEventInterstitialListener listener) {
            this.listener = listener;
        }

        @Override
        public void onAdLoaded(@NonNull final InterstitialAd interstitialAd) {
            listener.onAdLoaded();
        }

        @Override
        public void onAdFailed(@NonNull final InterstitialAd interstitialAd, @NonNull final ResponseStatus responseStatus) {
            final int error = (responseStatus == ResponseStatus.EMPTY) ? AdRequest.ERROR_CODE_NO_FILL : AdRequest.ERROR_CODE_INTERNAL_ERROR;
            listener.onAdFailedToLoad(error);
        }

        @Override
        public void onAdOpened(@NonNull final InterstitialAd interstitialAd) {
            listener.onAdOpened();
        }

        @Override
        public void onAdClicked(@NonNull final InterstitialAd interstitialAd) {
            listener.onAdClicked();
            // Assuming all the ads leave the application when the ad is clicked,
            // sending onAdLeftApplication callback when the ad is clicked.
            listener.onAdLeftApplication();
        }

        @Override
        public void onAdClosed(@NonNull final InterstitialAd interstitialAd) {
            listener.onAdClosed();
        }
    }
}
