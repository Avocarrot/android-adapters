package com.avocarrot.adapter.admob;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.avocarrot.nativead.adapter.admob.BuildConfig;
import com.avocarrot.sdk.Avocarrot;
import com.avocarrot.sdk.logger.Logger;
import com.avocarrot.sdk.mediation.ResponseStatus;
import com.avocarrot.sdk.nativeassets.MediationInfo;
import com.avocarrot.sdk.nativeassets.NativeAssetsAd;
import com.avocarrot.sdk.nativeassets.NativeAssetsAdPool;
import com.avocarrot.sdk.nativeassets.NativeAssetsConfig;
import com.avocarrot.sdk.nativeassets.listeners.NativeAssetsAdCallback;
import com.avocarrot.sdk.nativeassets.model.AdChoice;
import com.avocarrot.sdk.nativeassets.model.Image;
import com.avocarrot.sdk.nativeassets.model.NativeAssets;
import com.avocarrot.sdk.nativeassets.model.Rating;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.mediation.NativeAdMapper;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventNative;
import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener;

import java.util.Collections;
import java.util.List;

@Keep
@SuppressWarnings({"WeakerAccess", "unused"})
public class NativeAdapter implements CustomEventNative {
    @Nullable
    private NativeAssetsAd nativeAssetsAd;
    private boolean nativeImpressionRecorded;

    //region CustomEventNative
    @Override
    public void requestNativeAd(@Nullable final Context context, @Nullable final CustomEventNativeListener listener, @Nullable final String adUnitId,
                                @Nullable final NativeMediationAdRequest adRequest, @Nullable final Bundle customEventExtras) {
        if (listener == null) {
            Logger.warn("Failed to request banner ad, [listener] is null");
            return;
        }
        if (context == null) {
            Logger.warn("Failed to request banner ad, [context] is null");
            listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
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

        final NativeAdOptions options = adRequest.getNativeAdOptions();
        final int adChoicePlacement = options == null ? NativeAdOptions.ADCHOICES_TOP_RIGHT : options.getAdChoicesPlacement();

        final boolean prefetchMedia = options != null && options.shouldReturnUrlsForImageAssets();
        final NativeAssetsConfig.Builder config = new NativeAssetsConfig.Builder()
                .prefetchIcon(prefetchMedia)
                .prefetchImage(prefetchMedia)
                .prefetchAdChoiceIcon(true)
                .setMediationInfo(new MediationInfo.Builder()
                        .setPlatform("admob")
                        .setSdkVersion("10.2.4")
                        .setAdapterVersion(BuildConfig.VERSION_NAME)
                        .build());

        nativeAssetsAd = NativeAssetsAdPool.load(context, adUnitId, config, new AdCallback(listener, adRequest, adChoicePlacement));
    }

    @Override
    public void onDestroy() {
        if (nativeAssetsAd != null) {
            nativeAssetsAd.destroy();
        }
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }
    //endregion

    private class AdCallback implements NativeAssetsAdCallback {
        @NonNull
        private final CustomEventNativeListener listener;
        @NonNull
        private final NativeMediationAdRequest adRequest;
        private final int adChoicePlacement;

        public AdCallback(@NonNull final CustomEventNativeListener listener, @NonNull final NativeMediationAdRequest adRequest, final int adChoicePlacement) {
            this.listener = listener;
            this.adRequest = adRequest;
            this.adChoicePlacement = adChoicePlacement;
        }

        @Override
        public void onAdLoaded(@NonNull final NativeAssetsAd nativeAssetsAd, @NonNull final NativeAssets nativeAssets) {
            if (NativeAdapter.this.nativeAssetsAd != nativeAssetsAd) {
                Logger.warn("Ad loaded is not a native ad.");
                listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
                return;
            }
            NativeAdMapper adMapper = null;
            if (adRequest.isAppInstallAdRequested()) {
                adMapper = new AvocarrotNativeAppInstallAdMapper(nativeAssetsAd, nativeAssets, adChoicePlacement);
            } else if (adRequest.isContentAdRequested()) {
                adMapper = new AvocarrotNativeContentAdMapper(nativeAssetsAd, nativeAssets, adChoicePlacement);
            }
            if (adMapper == null) {
                Logger.warn("Failed to request native ad, both app install and content ad is null");
                listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
                return;
            }
            listener.onAdLoaded(adMapper);
        }

        @Override
        public void onAdFailed(@NonNull final NativeAssetsAd nativeAssetsAd, @NonNull final ResponseStatus responseStatus) {
            final int error = responseStatus == ResponseStatus.EMPTY ? AdRequest.ERROR_CODE_NO_FILL : AdRequest.ERROR_CODE_INTERNAL_ERROR;
            listener.onAdFailedToLoad(error);
        }

        @Override
        public void onAdOpened(@NonNull final NativeAssetsAd nativeAssetsAd) {
            if (nativeImpressionRecorded) {
                Logger.debug("Received [onAdOpened] callback for a native whose impression is already recorded. Ignoring the duplicate callback.");
                return;
            }
            listener.onAdImpression();
            nativeImpressionRecorded = true;
        }

        @Override
        public void onAdClicked(@NonNull final NativeAssetsAd nativeAssetsAd) {
            listener.onAdClicked();
            // Assuming all the ads leave the application when the ad is clicked,
            // sending onAdLeftApplication callback when the ad is clicked.
            listener.onAdLeftApplication();
        }
    }

    /**
     * A {@link NativeAppInstallAdMapper} used to map a Avocarrot NativeAssetsAd to Google native app install native ad.
     */
    private static class AvocarrotNativeAppInstallAdMapper extends NativeAppInstallAdMapper {
        private static final int MAX_STAR_RATING = 5;
        @NonNull
        private final NativeAdMapperDelegate delegate;

        private AvocarrotNativeAppInstallAdMapper(@NonNull final NativeAssetsAd nativeAd, @NonNull final NativeAssets nativeAssets,
                                                  final int adChoicePlacement) {
            delegate = new NativeAdMapperDelegate(nativeAd, nativeAssets, adChoicePlacement);

            setHeadline(nativeAssets.getTitle());
            setBody(nativeAssets.getText());
            setCallToAction(nativeAssets.getCallToAction());

            final Double starRating = convert(nativeAssets.getRating());
            if (starRating != null) {
                setStarRating(starRating);
            }

            setIcon(NativeAdMapperDelegate.buildImage(nativeAssets.getIcon()));
            setImages(NativeAdMapperDelegate.buildImages(nativeAssets.getImage()));

            setOverrideImpressionRecording(true);
            setOverrideClickHandling(true);
        }

        /**
         * Convert rating to a scale of 1 to 5.
         */
        private static Double convert(@Nullable final Rating rating) {
            if (rating == null) {
                return null;
            }
            if (rating.getScale() == 5) {
                return rating.getValue();
            }
            return (MAX_STAR_RATING * rating.getValue()) / rating.getScale();
        }

        @Override
        public void trackView(@NonNull final View view) {
            super.trackView(view);
            delegate.trackView(view);
        }

        @Override
        public void untrackView(@NonNull final View view) {
            super.untrackView(view);
            delegate.untrackView();
        }
    }

    /**
     * A {@link NativeContentAdMapper} used to map a Avocarrot NativeAssetsAd to Google native app install native ad.
     */
    private static class AvocarrotNativeContentAdMapper extends NativeContentAdMapper {
        @NonNull
        private final NativeAdMapperDelegate delegate;

        private AvocarrotNativeContentAdMapper(@NonNull final NativeAssetsAd nativeAd, @NonNull final NativeAssets nativeAssets, final int adChoicePlacement) {
            delegate = new NativeAdMapperDelegate(nativeAd, nativeAssets, adChoicePlacement);

            setHeadline(nativeAssets.getTitle());
            setBody(nativeAssets.getText());
            setCallToAction(nativeAssets.getCallToAction());

            setLogo(NativeAdMapperDelegate.buildImage(nativeAssets.getIcon()));
            setImages(NativeAdMapperDelegate.buildImages(nativeAssets.getImage()));

            setOverrideImpressionRecording(true);
            setOverrideClickHandling(true);
        }

        @Override
        public void trackView(@NonNull final View view) {
            super.trackView(view);
            delegate.trackView(view);
        }

        @Override
        public void untrackView(@NonNull final View view) {
            super.untrackView(view);
            delegate.untrackView();
        }
    }

    private static class NativeAdMapperDelegate {
        @NonNull
        private final NativeAssetsAd nativeAd;
        @Nullable
        private final AdChoice adChoice;
        private final int adChoicePlacement;
        private ImageView adChoiceView;

        private NativeAdMapperDelegate(@NonNull final NativeAssetsAd nativeAd, @NonNull final NativeAssets nativeAssets, final int adChoicePlacement) {
            this.nativeAd = nativeAd;

            adChoice = nativeAssets.getAdChoice();
            this.adChoicePlacement = adChoicePlacement;
        }

        @Nullable
        private static NativeAd.Image buildImage(@Nullable final Image image) {
            return MappedImage.build(image);
        }

        @NonNull
        private static List<NativeAd.Image> buildImages(@Nullable final Image image) {
            final NativeAd.Image mappedImage = MappedImage.build(image);
            if (image == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(mappedImage);
        }

        // Find the overlay view in the given ad view. The overlay view will always be the top most view in the hierarchy.
        @Nullable
        private static FrameLayout getOverlayView(@NonNull final View view) {
            if (view instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup) view;
                final View childView = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
                if (childView instanceof FrameLayout) {
                    return (FrameLayout) childView;
                }
            }
            return null;
        }

        private void trackView(@NonNull final View view) {
            nativeAd.registerViewForImpression(view);
            nativeAd.registerViewsForClick(Collections.singletonList(view));

            final FrameLayout overlayView = getOverlayView(view);
            if (overlayView == null) {
                return;
            }
            adChoiceView = MappedAdChoice.build(view.getContext(), adChoice, adChoicePlacement);
            if (adChoiceView == null) {
                return;
            }
            nativeAd.registerAdChoiceViewForClick(adChoiceView);
            overlayView.addView(adChoiceView);
            view.requestLayout();
        }

        // Called when the native ad view no longer needs tracking. Remove any previously added trackers.
        private void untrackView() {
            nativeAd.unregisterViews();
            final ViewGroup parentView = (ViewGroup) adChoiceView.getParent();
            if (adChoiceView != null && parentView != null) {
                parentView.removeView(adChoiceView);
            }
        }

        private static class MappedAdChoice {
            private static final int DEFAULT_ICON_SIZE_DP = 20;
            private static final int MINIMUM_ICON_SIZE_DP = 10;
            private static final int MAXIMUM_ICON_SIZE_DP = 30;

            @Nullable
            private static ImageView build(@NonNull final Context context, @Nullable final AdChoice adChoice, final int placement) {
                if (adChoice == null) {
                    return null;
                }
                final Image image = adChoice.getIcon();
                final Drawable drawable = image.getDrawable();
                if (drawable == null) {
                    return null;
                }
                final ImageView view = new ImageView(context);
                view.setImageDrawable(drawable);

                final float density = context.getResources().getDisplayMetrics().density;
                view.setLayoutParams(buildLayoutParams(convert(density, image.getWidth()), convert(density, image.getHeight()), placement));
                view.setVisibility(View.VISIBLE);
                return view;
            }

            private static int convert(final float density, final int size) {
                final int sizeDp = size < MINIMUM_ICON_SIZE_DP || size > MAXIMUM_ICON_SIZE_DP ? DEFAULT_ICON_SIZE_DP : size;
                return (int) (sizeDp * density + 0.5);
            }

            @NonNull
            private static FrameLayout.LayoutParams buildLayoutParams(final int widthPixels, final int heightPixels, final int placement) {
                final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthPixels, heightPixels);
                switch (placement) {
                    case NativeAdOptions.ADCHOICES_TOP_LEFT:
                        params.gravity = Gravity.TOP | Gravity.START;
                        break;
                    case NativeAdOptions.ADCHOICES_BOTTOM_RIGHT:
                        params.gravity = Gravity.BOTTOM | Gravity.END;
                        break;
                    case NativeAdOptions.ADCHOICES_BOTTOM_LEFT:
                        params.gravity = Gravity.BOTTOM | Gravity.START;
                        break;
                    case NativeAdOptions.ADCHOICES_TOP_RIGHT:
                    default:
                        params.gravity = Gravity.TOP | Gravity.END;
                }
                return params;
            }
        }

        private static class MappedImage extends NativeAd.Image {
            private static final double DEFAULT_IMAGE_SCALE = 1;

            @Nullable
            private final Drawable drawable;
            @NonNull
            private final Uri url;
            private final double scale;

            private MappedImage(@Nullable final Drawable drawable, @NonNull final Uri url, final double scale) {
                this.drawable = drawable;
                this.url = url;
                this.scale = scale;
            }

            @Nullable
            private static NativeAd.Image build(@Nullable final Image image) {
                if (image == null) {
                    return null;
                }
                return new MappedImage(image.getDrawable(), Uri.parse(image.getUrl()), DEFAULT_IMAGE_SCALE);
            }

            @Nullable
            @Override
            public Drawable getDrawable() {
                return drawable;
            }

            @NonNull
            @Override
            public Uri getUri() {
                return url;
            }

            @Override
            public double getScale() {
                return scale;
            }
        }
    }
}
