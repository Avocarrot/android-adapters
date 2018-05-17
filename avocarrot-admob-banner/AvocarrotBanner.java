package com.avocarrot.adapter.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.avocarrot.sdk.Avocarrot;
import com.avocarrot.sdk.banner.BannerAd;
import com.avocarrot.sdk.banner.BannerAdPool;
import com.avocarrot.sdk.banner.listeners.BannerAdCallback;
import com.avocarrot.sdk.logger.Logger;
import com.avocarrot.sdk.mediation.BannerSize;
import com.avocarrot.sdk.mediation.ResponseStatus;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class BannerAdapter implements CustomEventBanner {

    BannerSize[] availableSizes = new BannerSize[]{
            BannerSize.BANNER_SIZE_320x50,
            BannerSize.BANNER_SIZE_728x90,
            BannerSize.BANNER_SIZE_300x250,
            BannerSize.BANNER_SIZE_468x60,
            BannerSize.BANNER_SIZE_160x600,
            BannerSize.BANNER_SIZE_300x50
    };

    @Nullable
    private BannerAd bannerAd;
    @Nullable
    private FrameLayout bannerAdContainer;

    //region CustomEventBanner
    @Override
    public void requestBannerAd(@Nullable final Context context, @Nullable final CustomEventBannerListener listener, @Nullable final String adUnitId,
                                @Nullable final AdSize adSize, @Nullable final MediationAdRequest adRequest, @Nullable final Bundle customEventExtras) {
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
        if (adSize == null) {
            Logger.warn("Failed to request banner ad, [adSize] is null");
            listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }
        final BannerSize avoBannerSize = getAvoBannerSize(adSize);
        if (avoBannerSize == null) {
            Logger.warn("Failed to request banner ad, the input [adSize] = " + adSize.toString() + " is not supported");
            listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }
        Avocarrot.setTestMode(adRequest.isTesting());

        bannerAdContainer = new FrameLayout(context);
        bannerAdContainer.setLayoutParams(new FrameLayout.LayoutParams(avoBannerSize.width, avoBannerSize.height));
        bannerAd = BannerAdPool.load((Activity) context, adUnitId, bannerAdContainer, avoBannerSize, new AdCallback(listener));
        bannerAd.setAutoRefreshEnabled(false);
    }

    @Override
    public void onDestroy() {
        if (bannerAd != null) {
            bannerAd.onActivityDestroyed();
        }
    }

    @Override
    public void onPause() {
        if (bannerAd != null) {
            bannerAd.onActivityPaused();
        }
    }

    @Override
    public void onResume() {
        if (bannerAd != null) {
            bannerAd.onActivityResumed();
        }
    }
    //endregion

    @Nullable
    private BannerSize getAvoBannerSize(@NonNull final AdSize adMobAdSize) {
        final int height = adMobAdSize.getHeight();
        final int width = adMobAdSize.getWidth();
        for (BannerSize bs : availableSizes) {
            if (bs.height == height && bs.width == width) {
                return bs;
            }
        }
        return null;
    }

    private class AdCallback implements BannerAdCallback {
        @NonNull
        private final CustomEventBannerListener listener;

        private AdCallback(@NonNull final CustomEventBannerListener listener) {
            this.listener = listener;
        }

        @Override
        public void onAdLoaded(@NonNull final BannerAd bannerAd) {
            listener.onAdLoaded(bannerAdContainer);
        }

        @Override
        public void onAdFailed(@NonNull final BannerAd bannerAd, @NonNull final ResponseStatus responseStatus) {
            final int error = (responseStatus == ResponseStatus.EMPTY) ? AdRequest.ERROR_CODE_NO_FILL : AdRequest.ERROR_CODE_INTERNAL_ERROR;
            listener.onAdFailedToLoad(error);
        }

        @Override
        public void onAdOpened(@NonNull final BannerAd bannerAd) {
            listener.onAdOpened();
        }

        @Override
        public void onAdClicked(@NonNull final BannerAd bannerAd) {
            listener.onAdClicked();
            // Assuming all the ads leave the application when the ad is clicked,
            // sending onAdLeftApplication callback when the ad is clicked.
            listener.onAdLeftApplication();
        }

        @Override
        public void onAdClosed(@NonNull final BannerAd bannerAd) {
            listener.onAdClosed();
        }
    }
}
