package com.avocarrot.adapters.sample.mopub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avocarrot.adapters.sample.R;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

public class MopubInterstitialActivity extends AppCompatActivity {
    @NonNull
    private static final String EXTRA_AD_UNIT_ID = "ad_unit_id";
    @Nullable
    private MoPubInterstitial interstitialAd;

    @NonNull
    public static Intent buildIntent(@NonNull final Context context, @NonNull final String adUnitId) {
        return new Intent(context, MopubInterstitialActivity.class).putExtra(EXTRA_AD_UNIT_ID, adUnitId);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String adUnitId = getIntent().getStringExtra(EXTRA_AD_UNIT_ID);
        if (TextUtils.isEmpty(adUnitId)) {
            finish();
            return;
        }
        setContentView(R.layout.activity_admob_interstitial);
        interstitialAd = new MoPubInterstitial(this, adUnitId);
        interstitialAd.setInterstitialAdListener(new AdListener());
        final Button load = (Button) findViewById(R.id.load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull final View v) {
                interstitialAd.load();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
    }

    private class AdListener implements MoPubInterstitial.InterstitialAdListener {
        @Override
        public void onInterstitialLoaded(@NonNull final MoPubInterstitial interstitial) {
            if (interstitialAd != null) {
                interstitialAd.show();
            }
        }

        @Override
        public void onInterstitialFailed(@NonNull final MoPubInterstitial interstitial, @NonNull final MoPubErrorCode errorCode) {
            Toast.makeText(MopubInterstitialActivity.this, "Failed to load add, errorCode [" + errorCode + "]", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onInterstitialShown(@NonNull final MoPubInterstitial interstitial) {
        }

        @Override
        public void onInterstitialClicked(@NonNull final MoPubInterstitial interstitial) {
        }

        @Override
        public void onInterstitialDismissed(@NonNull final MoPubInterstitial interstitial) {
        }
    }
}
