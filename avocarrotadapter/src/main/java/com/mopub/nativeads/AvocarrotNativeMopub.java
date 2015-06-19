package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.avocarrot.androidsdk.AdError;
import com.avocarrot.androidsdk.AvocarrotCustom;
import com.avocarrot.androidsdk.AvocarrotCustomListener;
import com.avocarrot.androidsdk.CustomModel;
import com.avocarrot.androidsdk.UrlTrackerThread;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/* Compatible with Avocarrot SDK 3.3.4+ */

public class AvocarrotNativeMopub extends CustomEventNative {

    private static final String PLACEMENT = "placement";
    private static final String APP_ID = "appId";

    private static final String SANDBOX = "sandbox";
    private static final String LOGGER = "logger";

    AvocarrotCustom mAvocarrotCustom;
    CustomEventNativeListener customEventNativeListener;

    static ConcurrentLinkedQueue<CustomModel> CachedModels = new ConcurrentLinkedQueue<>();

    AvocarrotCustomListener mAvocarrorListener = new AvocarrotCustomListener() {
        @Override
        public void onAdLoaded(List<CustomModel> ads) {
            super.onAdLoaded(ads);
            if ((ads!=null) && (ads.size()>0)) {
                for (CustomModel model : ads)
                    CachedModels.add(model);
                CustomModel model = CachedModels.poll();
                if (model!=null) {
                    customEventNativeListener.onNativeAdLoaded(new AvocarrotNativeAd(model));
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
    };

    @Override
    protected void loadNativeAd(@NonNull Context context, @NonNull CustomEventNativeListener customEventNativeListener, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) {

        if (CachedModels.size()>0) {
            CustomModel model = CachedModels.poll();
            if (model!=null) {
                customEventNativeListener.onNativeAdLoaded(new AvocarrotNativeAd(model));
                return;
            }
        }

        if (!(context instanceof Activity)) {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        final String placement;
        final String appId;
        if (extrasAreValid(serverExtras)) {
            placement = serverExtras.get(PLACEMENT);
            appId = serverExtras.get(APP_ID);
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        mAvocarrotCustom = new AvocarrotCustom((Activity)context, appId, placement);

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
        mAvocarrotCustom.setLogger(logger, "ALL" );

        mAvocarrotCustom.loadAds(3);
        mAvocarrotCustom.setListener(mAvocarrorListener);
        this.customEventNativeListener = customEventNativeListener;

    }

    private boolean extrasAreValid(final Map<String, String> serverExtras) {
        return (serverExtras!=null) && serverExtras.containsKey(PLACEMENT) && (serverExtras.containsKey(APP_ID));
    }

    class AvocarrotNativeAd extends BaseForwardingNativeAd {

        CustomModel avocarrotModel;

        public AvocarrotNativeAd(@NonNull CustomModel customModel) {
            avocarrotModel = customModel;

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
        public void handleClick(@Nullable View view) {
            super.handleClick(view);
            if (avocarrotModel.getClickUrls()!=null)
                new UrlTrackerThread(avocarrotModel.getClickUrls()).start();

        }

    }

}