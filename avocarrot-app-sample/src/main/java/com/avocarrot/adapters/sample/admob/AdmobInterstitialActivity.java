package com.avocarrot.adapters.sample.admob;

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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdmobInterstitialActivity extends AppCompatActivity {
    @NonNull
    private static final String EXTRA_AD_UNIT_ID = "ad_unit_id";

    @NonNull
    public static Intent buildIntent(@NonNull final Context context, @NonNull final String adUnitId) {
        return new Intent(context, AdmobInterstitialActivity.class).putExtra(EXTRA_AD_UNIT_ID, adUnitId);
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
        final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(adUnitId);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(final int reason) {
                Toast.makeText(AdmobInterstitialActivity.this, "Failed to load add, reason [" + reason + "]", Toast.LENGTH_SHORT).show();
            }
        });
        final Button load = (Button) findViewById(R.id.load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull final View v) {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

    }
}
