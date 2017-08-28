package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.avocarrot.sdk.banner.BannerAd;
import com.avocarrot.sdk.banner.BannerAdPool;
import com.avocarrot.sdk.banner.listeners.BannerAdCallback;
import com.avocarrot.sdk.logger.Logger;
import com.avocarrot.sdk.mediation.BannerSize;
import com.avocarrot.sdk.mediation.ResponseStatus;
import com.mopub.common.DataKeys;

import java.util.Map;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class AvocarrotBanner extends CustomEventBanner implements BannerAdCallback {
    @NonNull
    private static final String AD_UNIT_KEY = "adUnit";
    @NonNull
    private static final String AD_UNIT_ID_KEY = "adUnitId";
    @Nullable
    private BannerAd bannerAd;
    @Nullable
    private FrameLayout adContainer;
    @Nullable
    private CustomEventBannerListener listener;

    //region CustomeEventBanner
    @Override
    protected void loadBanner(@Nullable final Context context, @Nullable final CustomEventBannerListener listener,
                              @Nullable final Map<String, Object> localExtras, @Nullable final Map<String, String> serverExtras) {
        if (listener == null) {
            Logger.warn("Failed to request banner ad, [listener] is null");
            return;
        }
        this.listener = listener;
        if (context == null) {
            Logger.warn("Failed to request banner ad, [context] is null");
            listener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        if (!(context instanceof Activity)) {
            Logger.warn("Failed to request banner ad, [context] is not an Activity instance");
            listener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        if (localExtras == null) {
            Logger.warn("Failed to request banner ad, [localExtras] is null");
            listener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        if (serverExtras == null) {
            Logger.warn("Failed to request banner ad, [serverExtras] is null");
            listener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        final Object width = localExtras.get(DataKeys.AD_WIDTH);
        if (width == null || !(width instanceof Integer)) {
            Logger.warn("Failed to request banner ad, requested [width] is incorrect");
            listener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        final Object height = localExtras.get(DataKeys.AD_HEIGHT);
        if (height == null || !(height instanceof Integer)) {
            Logger.warn("Failed to request banner ad, requested [height] is incorrect");
            listener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        final BannerSize avoBannerSize = getAvoBannerSize((int) width, (int) height);
        if (avoBannerSize == null) {
            Logger.warn("Failed to request banner ad, the input width [" + width + "] and height [" + height + "] is not supported");
            listener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        final String adUnitId = serverExtras.containsKey(AD_UNIT_ID_KEY) ? serverExtras.get(AD_UNIT_ID_KEY) : serverExtras.get(AD_UNIT_KEY);
        if (TextUtils.isEmpty(adUnitId)) {
            Logger.warn("Failed to request banner ad, [adUnitId] is empty");
            listener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }
        adContainer = new FrameLayout(context);
        adContainer.setLayoutParams(new FrameLayout.LayoutParams(avoBannerSize.width, avoBannerSize.height));
        bannerAd = BannerAdPool.load((Activity) context, adUnitId, adContainer, avoBannerSize, this);
        bannerAd.setAutoRefreshEnabled(false);
    }

    @Override
    protected void onInvalidate() {
        if (bannerAd != null) {
            bannerAd.onActivityDestroyed();
        }
    }
    //endregion

    @Nullable
    private BannerSize getAvoBannerSize(final int width, final int height) {
        if (height == BannerSize.BANNER_SIZE_320x50.height && width == BannerSize.BANNER_SIZE_320x50.width) {
            return BannerSize.BANNER_SIZE_320x50;
        }
        if (height == BannerSize.BANNER_SIZE_728x90.height && width == BannerSize.BANNER_SIZE_728x90.width) {
            return BannerSize.BANNER_SIZE_728x90;
        }
        return null;
    }

    //region BannerAdCallback
    @Override
    public void onAdLoaded(@NonNull final BannerAd bannerAd) {
        if (listener == null || adContainer == null) {
            return;
        }
        listener.onBannerLoaded(adContainer);
    }

    @Override
    public void onAdFailed(@NonNull final BannerAd bannerAd, @NonNull final ResponseStatus responseStatus) {
        if (listener != null) {
            final MoPubErrorCode error = (responseStatus == ResponseStatus.EMPTY) ? MoPubErrorCode.NO_FILL : MoPubErrorCode.NETWORK_INVALID_STATE;
            listener.onBannerFailed(error);
        }
    }

    @Override
    public void onAdOpened(@NonNull final BannerAd bannerAd) {
    }

    @Override
    public void onAdClicked(@NonNull final BannerAd bannerAd) {
        if (listener != null) {
            listener.onBannerClicked();
        }
    }

    @Override
    public void onAdClosed(@NonNull final BannerAd bannerAd) {
    }
    //endregion
}
