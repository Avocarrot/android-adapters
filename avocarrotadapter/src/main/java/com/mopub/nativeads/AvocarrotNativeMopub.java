package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.avocarrot.sdk.AdChoices;
import com.avocarrot.sdk.AdError;
import com.avocarrot.sdk.AdLayout;
import com.avocarrot.sdk.Avocarrot;
import com.avocarrot.sdk.NativeAssets;
import com.avocarrot.sdk.NativeAssetsAd;
import com.avocarrot.sdk.NativeAssetsAdCallback;
import com.avocarrot.sdk.logger.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Compatible with Avocarrot SDK 4.+ */

public class AvocarrotNativeMopub extends CustomEventNative {

    private static final String AD_UNIT_ID = "adUnitId";
    private static final String API_KEY = "apiKey";

    private static final String SANDBOX = "sandbox";
    private static final String LOGGER = "logger";

    @Override
    protected void loadNativeAd(
            final @NonNull Context context,
            final @NonNull CustomEventNativeListener customEventNativeListener,
            @NonNull Map<String, Object> localExtras,
            @NonNull Map<String, String> serverExtras
    ) {

        String adUnitId;
        String appKey;
        if (extrasAreValid(serverExtras)) {

            appKey = serverExtras.get(API_KEY);
            adUnitId = serverExtras.get(AD_UNIT_ID);
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        NativeAssetsAd.Configuration.Builder configurationBuilder = new NativeAssetsAd.Configuration.Builder()
                .setApiKey(appKey)
                .setAdUnitId(adUnitId)
                .isMopubMediation()
                .setCallback(new NativeAssetsAdCallback() {

                    AvocarrotNativeAd avocarrotNativeAd = null;

                    @Override
                    public void onAdLoaded(NativeAssetsAd nativeAssetsAd, List<NativeAssets> ads) {
                        if ((ads != null) && (ads.size() > 0)) {
                            NativeAssets model = ads.get(0);
                            if (model != null) {
                                avocarrotNativeAd = new AvocarrotNativeAd(model, nativeAssetsAd, context, customEventNativeListener);
                            } else {
                                customEventNativeListener.onNativeAdFailed(NativeErrorCode.EMPTY_AD_RESPONSE);
                            }
                        } else {
                            customEventNativeListener.onNativeAdFailed(NativeErrorCode.EMPTY_AD_RESPONSE);
                        }
                    }

                    @Override
                    public void onAdError(AdError error) {
                        super.onAdError(error);
                        customEventNativeListener.onNativeAdFailed(NativeErrorCode.EMPTY_AD_RESPONSE);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        if (avocarrotNativeAd != null)
                            avocarrotNativeAd.notifyAdImpressed();
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        if (avocarrotNativeAd != null)
                            avocarrotNativeAd.notifyAdClicked();
                    }

                });

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


        Avocarrot.build(context, configurationBuilder).loadAd();

    }

    private boolean extrasAreValid(final Map<String, String> serverExtras) {
        return (serverExtras != null) && (serverExtras.containsKey(AD_UNIT_ID));
    }

    class AvocarrotNativeAd extends StaticNativeAd implements View.OnClickListener {

        final NativeAssets nativeAssets;
        final NativeAssetsAd nativeAssetsAd;

        public AvocarrotNativeAd(@NonNull NativeAssets nativeAssets, NativeAssetsAd nativeAssetsAd, final Context context, final CustomEventNativeListener customEventNativeListener) {

            this.nativeAssets = nativeAssets;
            this.nativeAssetsAd = nativeAssetsAd;

            setIconImageUrl(nativeAssets.getIconUrl());
            setMainImageUrl(nativeAssets.getImageUrl());

            setTitle(nativeAssets.getTitle());
            setCallToAction(nativeAssets.getCallToAction());
            setText(nativeAssets.getText());

            setClickDestinationUrl(nativeAssets.getDestinationUrl());

            AdChoices adChoices = nativeAssets.getAdChoices();
            if (adChoices != null) {
                setPrivacyInformationIconClickThroughUrl(adChoices.getRedirectionUrl());
                setPrivacyInformationIconImageUrl(adChoices.getIconUrl());
            }

            final List<String> imageUrls = new ArrayList<String>();
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
                    customEventNativeListener.onNativeAdLoaded(AvocarrotNativeAd.this);
                }

                @Override
                public void onImagesFailedToCache(NativeErrorCode errorCode) {
                    customEventNativeListener.onNativeAdFailed(errorCode);
                }
            });

        }

        @Override
        public void prepare(final View view) {
            super.prepare(view);
            AdLayout adLayout = new AdLayout.BuilderWithView(view).build();
            nativeAssetsAd.bindView(adLayout, nativeAssets);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            nativeAssetsAd.handleClick(nativeAssets);
        }

    }

}
