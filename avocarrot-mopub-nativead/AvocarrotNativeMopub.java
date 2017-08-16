package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.avocarrot.nativead.adapter.mopub.BuildConfig;
import com.avocarrot.sdk.mediation.ResponseStatus;
import com.avocarrot.sdk.nativeassets.MediationInfo;
import com.avocarrot.sdk.nativeassets.NativeAssetsAd;
import com.avocarrot.sdk.nativeassets.NativeAssetsAdPool;
import com.avocarrot.sdk.nativeassets.NativeAssetsConfig;
import com.avocarrot.sdk.nativeassets.listeners.NativeAssetsAdCallback;
import com.avocarrot.sdk.nativeassets.model.AdChoice;
import com.avocarrot.sdk.nativeassets.model.Image;
import com.avocarrot.sdk.nativeassets.model.NativeAssets;
import com.mopub.common.MoPub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class AvocarrotNativeMopub extends CustomEventNative {
    private static final String AD_UNIT = "adUnit";

    @Override
    protected void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener mopubListener,
                                @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        final String adUnitId = serverExtras.get(AD_UNIT);
        if (TextUtils.isEmpty(adUnitId)) {
            mopubListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        final NativeAssetsAdCallback avocarrotCallback = new NativeAssetsAdCallback() {
            AvocarrotNativeAd avocarrotNativeAd;

            @Override
            public void onAdLoaded(@NonNull final NativeAssetsAd nativeAssetsAd, @NonNull final NativeAssets nativeAssets) {
                avocarrotNativeAd = new AvocarrotNativeAd(context, nativeAssetsAd, nativeAssets, mopubListener);
            }

            @Override
            public void onAdFailed(@NonNull final NativeAssetsAd nativeAssetsAd, @NonNull final ResponseStatus responseStatus) {
                mopubListener.onNativeAdFailed(NativeErrorCode.SERVER_ERROR_RESPONSE_CODE);
            }

            @Override
            public void onAdOpened(@NonNull final NativeAssetsAd nativeAssetsAd) {
                if (avocarrotNativeAd != null) {
                    avocarrotNativeAd.notifyAdImpressed();
                }
            }

            @Override
            public void onAdClicked(@NonNull final NativeAssetsAd nativeAssetsAd) {
                if (avocarrotNativeAd != null) {
                    avocarrotNativeAd.notifyAdClicked();
                }
            }
        };

        final NativeAssetsConfig.Builder configBuilder = new NativeAssetsConfig.Builder()
                .prefetchIcon(false)
                .prefetchImage(false)
                .prefetchAdChoiceIcon(false)
                .setMediationInfo(new MediationInfo.Builder()
                        .setPlatform("mopub")
                        .setSdkVersion(MoPub.SDK_VERSION)
                        .setAdapterVersion(BuildConfig.VERSION_NAME)
                        .build());
        NativeAssetsAdPool.load(context, adUnitId, configBuilder, avocarrotCallback);
    }

    private class AvocarrotNativeAd extends StaticNativeAd {
        @NonNull
        final NativeAssetsAd nativeAssetsAd;

        AvocarrotNativeAd(@NonNull final Context context, @NonNull final NativeAssetsAd nativeAssetsAd, @NonNull final NativeAssets nativeAssets,
                          @NonNull final CustomEventNativeListener mopubListener) {
            this.nativeAssetsAd = nativeAssetsAd;

            final Image icon = nativeAssets.getIcon();
            setIconImageUrl(icon != null ? icon.getUrl() : null);

            final Image image = nativeAssets.getImage();
            setMainImageUrl(image != null ? image.getUrl() : null);

            setTitle(nativeAssets.getTitle());
            setText(nativeAssets.getText());
            setCallToAction(nativeAssets.getCallToAction());

            final AdChoice adChoices = nativeAssets.getAdChoice();
            if (adChoices != null) {
                setPrivacyInformationIconClickThroughUrl(adChoices.getClickUrl());
                setPrivacyInformationIconImageUrl(adChoices.getIcon().getUrl());
            }

            final List<String> imageUrls = new ArrayList<>();
            final String mainImageUrl = getMainImageUrl();
            if (mainImageUrl != null) {
                imageUrls.add(getMainImageUrl());
            }
            final String iconUrl = getIconImageUrl();
            if (iconUrl != null) {
                imageUrls.add(getIconImageUrl());
            }

            NativeImageHelper.preCacheImages(context, imageUrls, new NativeImageHelper.ImageListener() {
                @Override
                public void onImagesCached() {
                    mopubListener.onNativeAdLoaded(AvocarrotNativeAd.this);
                }

                @Override
                public void onImagesFailedToCache(final NativeErrorCode errorCode) {
                    mopubListener.onNativeAdFailed(errorCode);
                }
            });
        }

        @Override
        public void prepare(@NonNull final View view) {
            nativeAssetsAd.registerViewForImpression(view);
            nativeAssetsAd.registerViewsForClick(getClickableViews(view));
        }

        @NonNull
        private List<View> getClickableViews(@NonNull final View view) {
            if (!(view instanceof ViewGroup)) {
                return Collections.singletonList(view);
            }
            final ViewGroup viewGroup = (ViewGroup) view;
            final int childCount = viewGroup.getChildCount();
            final List<View> result = new LinkedList<>();
            for (int i = 0; i < childCount; i++) {
                final View child = viewGroup.getChildAt(i);
                result.addAll(getClickableViews(child));
            }
            return result;
        }

        @Override
        public void destroy() {
            nativeAssetsAd.destroy();
        }
    }
}
