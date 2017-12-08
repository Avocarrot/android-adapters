package com.avocarrot.adapters.sample.mopub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avocarrot.adapters.sample.R;
import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.ViewBinder;

import java.util.Locale;

public class MopubNativeActivity extends AppCompatActivity {
    @NonNull
    private static final String EXTRA_AD_UNIT_ID = "ad_unit_id";

    @NonNull
    public static Intent buildIntent(@NonNull final Context context, @NonNull final String adUnitId) {
        return new Intent(context, MopubNativeActivity.class).putExtra(EXTRA_AD_UNIT_ID, adUnitId);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mopub_native);

        final Intent intent = getIntent();
        final String adUnitId = intent.getStringExtra(EXTRA_AD_UNIT_ID);
        if (TextUtils.isEmpty(adUnitId)) {
            finish();
            return;
        }

        final RecyclerView.Adapter originalAdapter = new DemoRecyclerAdapter();
        final MoPubRecyclerAdapter adapter = new MoPubRecyclerAdapter(this, originalAdapter, new MoPubNativeAdPositioning.MoPubServerPositioning());
        adapter.registerAdRenderer(new MoPubStaticNativeAdRenderer(new ViewBinder.Builder(R.layout.mopub_list_item)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build()));

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.loadAds(adUnitId);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private static class DemoRecyclerAdapter extends RecyclerView.Adapter<DemoViewHolder> {
        private static final int ITEM_COUNT = 150;

        @Override
        public DemoViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new DemoViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(final DemoViewHolder holder, final int position) {
            holder.textView.setText(String.format(Locale.US, "Content Item #%d", position));
        }

        @Override
        public long getItemId(final int position) {
            return (long) position;
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }
    }

    private static class DemoViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final TextView textView;

        private DemoViewHolder(@NonNull final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
