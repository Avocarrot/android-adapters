package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.avocarrot.androidsdk.AdError;
import com.avocarrot.androidsdk.AvocarrotCustom;
import com.avocarrot.androidsdk.AvocarrotCustomListener;
import com.avocarrot.androidsdk.CustomModel;
import com.avocarrot.androidsdk.UrlTrackerThread;

import java.util.List;
import java.util.Map;

/* Compatible with Avocarrot SDK 3.5.0+ */

public class AvocarrotNativeMopub extends CustomEventNative {

    private static final String PLACEMENT_KEY = "placementKey";
    private static final String API_KEY = "apiKey";

    private static final String SANDBOX = "sandbox";
    private static final String LOGGER = "logger";

    AvocarrotCustom mAvocarrotCustom;

    @Override
    protected void loadNativeAd(final @NonNull Context context, final @NonNull CustomEventNativeListener customEventNativeListener, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) {

        if (!(context instanceof Activity)) {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        final String placement;
        final String appId;
        if (extrasAreValid(serverExtras)) {
            placement = serverExtras.get(PLACEMENT_KEY);
            appId = serverExtras.get(API_KEY);
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        mAvocarrotCustom = new AvocarrotCustom((Activity)context, appId, placement, "mopub");

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

        mAvocarrotCustom.setSandbox(sandbox);
        mAvocarrotCustom.setLogger(logger, "ALL");

        mAvocarrotCustom.setListener(new AvocarrotCustomListener() {
            @Override
            public void onAdLoaded(List<CustomModel> ads) {
                super.onAdLoaded(ads);
                if ((ads!=null) && (ads.size()>0)) {
                    CustomModel model = ads.get(0);
                    if (model!=null) {
                        customEventNativeListener.onNativeAdLoaded(new AvocarrotNativeAd(model, context));
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
        });
        mAvocarrotCustom.loadAd();

    }

    private boolean extrasAreValid(final Map<String, String> serverExtras) {
        return (serverExtras!=null) && serverExtras.containsKey(PLACEMENT_KEY) && (serverExtras.containsKey(API_KEY));
    }

    class AvocarrotNativeAd extends StaticNativeAd {

        final CustomModel avocarrotModel;
        final ImpressionTracker impressionTracker;
        final NativeClickHandler nativeClickHandler;

        public AvocarrotNativeAd(@NonNull CustomModel customModel, Context context) {

            avocarrotModel = customModel;
            impressionTracker = new ImpressionTracker(context);
            nativeClickHandler = new NativeClickHandler(context);

            setIconImageUrl(avocarrotModel.getIconUrl());
            setMainImageUrl(avocarrotModel.getImageUrl());

            setTitle(avocarrotModel.getTitle());
            setCallToAction(avocarrotModel.getCTAText());
            setText(avocarrotModel.getDescription());

            setClickDestinationUrl(avocarrotModel.getDestinationUrl());

            for (String tracker : avocarrotModel.getImpressionUrls())
                addImpressionTracker(tracker);

        }

        @Override
        public void prepare(final View view) {
            impressionTracker.addView(view, this);
            nativeClickHandler.setOnClickListener(view, this);
        }

        @Override
        public void recordImpression(final View view) {
            notifyAdImpressed();
            mAvocarrotCustom.impressionRegistered(view, avocarrotModel);
        }

        @Override
        public void handleClick(final View view) {
            notifyAdClicked();
            nativeClickHandler.openClickDestinationUrl(getClickDestinationUrl(), view);
            if (avocarrotModel.getClickUrls()!=null)
                new Thread( new UrlTrackerThread(avocarrotModel.getClickUrls()) ).start();
        }

    }

}
