package com.avocarrot.adapters.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.avocarrot.adapters.sample.admob.AdmobActivity;
import com.avocarrot.adapters.sample.admob.AdmobBannerActivity;
import com.avocarrot.adapters.sample.admob.AdmobConfigActivity;
import com.avocarrot.adapters.sample.admob.AdmobInterstitialActivity;
import com.avocarrot.adapters.sample.admob.AdmobNativeActivity;
import com.avocarrot.adapters.sample.dfp.DFPActivity;
import com.avocarrot.adapters.sample.dfp.DFPBannerActivity;
import com.avocarrot.adapters.sample.dfp.DFPConfigActivity;
import com.avocarrot.adapters.sample.dfp.DFPInterstitialActivity;
import com.avocarrot.adapters.sample.mopub.MopubActivity;
import com.avocarrot.adapters.sample.mopub.MopubBannerActivity;
import com.avocarrot.adapters.sample.mopub.MopubConfigActivity;
import com.avocarrot.adapters.sample.mopub.MopubInterstitialActivity;
import com.avocarrot.adapters.sample.mopub.MopubNativeActivity;

public final class IntentStarter {
    public static final int SCREEN_BANNER = 0;
    public static final int SCREEN_INTERSTITIAL = 1;
    public static final int SCREEN_NATIVE = 2;

    static void startAdmobActivity(@NonNull final Activity context) {
        context.startActivity(AdmobActivity.buildIntent(context));
    }

    static void startMopubActivity(@NonNull final Activity context) {
        context.startActivity(MopubActivity.buildIntent(context));
    }

    static void startDFPActivity(@NonNull final Activity context) {
        context.startActivity(DFPActivity.buildIntent(context));
    }

    public static void startAdmobAdTypeActivity(@NonNull final Activity context, @ScreenAdType final int adTypeScreen, @NonNull final String adUnitId) {
        context.startActivity(buildIntentByScreenType(adTypeScreen, new AdTypeIntentBuilder() {
            @NonNull
            @Override
            public Intent buildBannerIntent() {
                return AdmobBannerActivity.buildIntent(context, adUnitId);
            }

            @NonNull
            @Override
            public Intent buildInterstitialIntent() { return AdmobInterstitialActivity.buildIntent(context, adUnitId);
            }

            @NonNull
            @Override
            public Intent buildNativeIntent() {
                return AdmobNativeActivity.buildIntent(context, adUnitId);
            }
        }));
    }

    @NonNull
    private static Intent buildIntentByScreenType(@ScreenAdType final int adTypeScreen, @NonNull final AdTypeIntentBuilder adTypeIntentBuilder) {
        switch (adTypeScreen) {
            case SCREEN_BANNER:
                return adTypeIntentBuilder.buildBannerIntent();
            case SCREEN_INTERSTITIAL:
                return adTypeIntentBuilder.buildInterstitialIntent();
            case SCREEN_NATIVE:
                return adTypeIntentBuilder.buildNativeIntent();
            default:
                throw new IllegalArgumentException("Unknown adTypeScreen [" + adTypeScreen + "]");
        }
    }

    public static void startAdmobConfigActivity(@NonNull final Activity context, @ScreenAdType final int adTypeScreen) {
        context.startActivity(buildIntentByScreenType(adTypeScreen, new AdTypeIntentBuilder() {
            @NonNull
            @Override
            public Intent buildBannerIntent() {
                return AdmobConfigActivity.buildIntent(context, adTypeScreen, getString(context, "admob_ad_unit_id_banner"));
            }

            @NonNull
            @Override
            public Intent buildInterstitialIntent() {
                return AdmobConfigActivity.buildIntent(context, adTypeScreen, getString(context, "admob_ad_unit_id_interstitial"));
            }

            @NonNull
            @Override
            public Intent buildNativeIntent() {
                return AdmobConfigActivity.buildIntent(context, adTypeScreen, getString(context, "admob_ad_unit_id_native"));
            }
        }));
    }

    public static void startMopubConfigActivity(@NonNull final Activity context, @ScreenAdType final int adTypeScreen) {
        context.startActivity(buildIntentByScreenType(adTypeScreen, new AdTypeIntentBuilder() {
            @NonNull
            @Override
            public Intent buildBannerIntent() {
                return MopubConfigActivity.buildIntent(context, adTypeScreen, getString(context, "mopub_ad_unit_id_banner"));
            }

            @NonNull
            @Override
            public Intent buildInterstitialIntent() {
                return MopubConfigActivity.buildIntent(context, adTypeScreen, getString(context, "mopub_ad_unit_id_interstitial"));
            }

            @NonNull
            @Override
            public Intent buildNativeIntent() {
                return MopubConfigActivity.buildIntent(context, adTypeScreen, getString(context, "mopub_ad_unit_id_native"));
            }
        }));
    }

    public static void startMopubAdTypeActivity(@NonNull final Activity context, @ScreenAdType final int adTypeScreen, @NonNull final String adUnitId) {
        context.startActivity(buildIntentByScreenType(adTypeScreen, new AdTypeIntentBuilder() {
            @NonNull
            @Override
            public Intent buildBannerIntent() {
                return MopubBannerActivity.buildIntent(context, adUnitId);
            }

            @NonNull
            @Override
            public Intent buildInterstitialIntent() {
                return MopubInterstitialActivity.buildIntent(context, adUnitId);
            }

            @NonNull
            @Override
            public Intent buildNativeIntent() {
                return MopubNativeActivity.buildIntent(context, adUnitId);
            }
        }));
    }

    public static void startDFPConfigActivity(@NonNull final Activity context, @ScreenAdType final int adTypeScreen) {
        context.startActivity(buildIntentByScreenType(adTypeScreen, new AdTypeIntentBuilder() {
            @NonNull
            @Override
            public Intent buildBannerIntent() {
                return DFPConfigActivity.buildIntent(context, adTypeScreen, getString(context, "dfp_ad_unit_id_banner"));
            }

            @NonNull
            @Override
            public Intent buildInterstitialIntent() {
                return DFPConfigActivity.buildIntent(context, adTypeScreen, getString(context, "dfp_ad_unit_id_interstitial"));
            }

            @NonNull
            @Override
            public Intent buildNativeIntent() {
                throw new IllegalArgumentException("Unsupported adType  [" + adTypeScreen + "]");
            }

        }));
    }

    public static void startDFPAdTypeActivity(@NonNull final Activity context, @ScreenAdType final int adTypeScreen, @NonNull final String adUnitId) {
        context.startActivity(buildIntentByScreenType(adTypeScreen, new AdTypeIntentBuilder() {
            @NonNull
            @Override
            public Intent buildBannerIntent() {
                return DFPBannerActivity.buildIntent(context, adUnitId);
            }

            @NonNull
            @Override
            public Intent buildInterstitialIntent() {
                return DFPInterstitialActivity.buildIntent(context, adUnitId);
            }

            @NonNull
            @Override
            public Intent buildNativeIntent() {
                throw new IllegalArgumentException("Unsupported adType [" + adTypeScreen + "]");
            }

        }));
    }

    private interface AdTypeIntentBuilder {
        @NonNull
        Intent buildBannerIntent();
        @NonNull
        Intent buildInterstitialIntent();
        @NonNull
        Intent buildNativeIntent();
    }

    @NonNull
    public static String getString(@NonNull final Activity context, @NonNull final String stringId) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier(stringId, "string", context.getPackageName());
        if (resourceId > 0) {
            return res.getString(resourceId);
        }
        return "";
    }

}
