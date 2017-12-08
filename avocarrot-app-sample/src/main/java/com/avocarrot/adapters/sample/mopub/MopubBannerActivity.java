package com.avocarrot.adapters.sample.mopub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.avocarrot.adapters.sample.R;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

public class MopubBannerActivity extends AppCompatActivity {
    @NonNull
    private static final String EXTRA_AD_UNIT_ID = "ad_unit_id";
    @Nullable
    private MoPubView adView;

    @NonNull
    public static Intent buildIntent(@NonNull final Context context, @NonNull final String adUnitId) {
        return new Intent(context, MopubBannerActivity.class).putExtra(EXTRA_AD_UNIT_ID, adUnitId);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String adUnitId = getIntent().getStringExtra(EXTRA_AD_UNIT_ID);
        if (TextUtils.isEmpty(adUnitId)) {
            finish();
            return;
        }
        setContentView(R.layout.activity_mopub_banner);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        adView = (MoPubView) findViewById(R.id.adview);
        adView.setAdUnitId(adUnitId);
        adView.setBannerAdListener(new AdListener());
        adView.loadAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }

    private class AdListener implements MoPubView.BannerAdListener {

        @Override
        public void onBannerLoaded(@NonNull final MoPubView banner) {
        }

        @Override
        public void onBannerFailed(@NonNull final MoPubView banner, @NonNull final MoPubErrorCode errorCode) {
            Toast.makeText(MopubBannerActivity.this, "Failed to load add, errorCode [" + errorCode + "]", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBannerClicked(@NonNull final MoPubView banner) {
        }

        @Override
        public void onBannerExpanded(@NonNull final MoPubView banner) {
        }

        @Override
        public void onBannerCollapsed(@NonNull final MoPubView banner) {
        }
    }
}
