package com.avocarrot.avocarrotadapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;


public class InterstitialMopubActivity extends Activity implements MoPubInterstitial.InterstitialAdListener {

    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mopub_interstitial);
        msg = (TextView) findViewById(R.id.interstitial_mopub_message);

        MoPubInterstitial mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_interstitial_ad_unit_id) );
        mInterstitial.setInterstitialAdListener(this);
        mInterstitial.load();

        msg.setText(R.string.mopub_intestitial_msg_loading);
    }

    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        if (interstitial.isReady()) {
            interstitial.show();
        }
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        msg.setText(R.string.mopub_intestitial_msg_failed);
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {
        msg.setText(R.string.mopub_intestitial_msg_isShown);
    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {
    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
        interstitial.destroy();
    }

}
