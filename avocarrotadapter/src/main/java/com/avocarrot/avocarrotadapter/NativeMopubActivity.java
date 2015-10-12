package com.avocarrot.avocarrotadapter;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.ViewBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NativeMopubActivity extends ListActivity {

    MoPubAdAdapter mAdAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up your adapter as usual.
        List<String> data = new ArrayList<>(Arrays.asList("Greek folk dances", "Tango", "Salsa", "Rumba", "Hip Hop", "Break Dance", "Belly Dancing", "Pole Dancing", "Capoeira", "Polka", "Irish Step Dancing", "Greek folk dances", "Tango"));
        UserAdapter myAdapter = new UserAdapter(this, data);

        // Set up a ViewBinder and MoPubNativeAdRenderer as above.
        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_layout)
                .iconImageId(R.id.native_icon)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .build();

        // Set up the positioning behavior your ads should have.
        MoPubNativeAdPositioning.MoPubServerPositioning adPositioning =
                MoPubNativeAdPositioning.serverPositioning();
        MoPubStaticNativeAdRenderer adRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        // Set up the MoPubAdAdapter
        mAdAdapter = new MoPubAdAdapter(this, myAdapter, adPositioning);
        mAdAdapter.registerAdRenderer(adRenderer);

        setListAdapter(mAdAdapter);
    }

    @Override
    public void onResume() {
        mAdAdapter.loadAds(getString(R.string.mopub_native_ad_unit_id));
        super.onResume();
    }

    class UserAdapter extends ArrayAdapter<String> {

        public UserAdapter(Context context, List<String> data) {
            super(context, android.R.layout.simple_list_item_1, data);
        }

    }

}
