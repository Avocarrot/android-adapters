package com.avocarrot.avocarrotadapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.ViewBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NativeMopubRecyclerActivity extends Activity {

    MoPubRecyclerAdapter adAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> items = new ArrayList<>(Arrays.asList("Greek folk dances", "Tango", "Salsa", "Rumba", "Hip Hop", "Break Dance", "Belly Dancing", "Pole Dancing", "Capoeira", "Polka", "Irish Step Dancing", "Greek folk dances", "Tango"));
        Adapter adapter = new Adapter(items);
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Pass the recycler Adapter your original adapter.
        adAdapter = new MoPubRecyclerAdapter(this, adapter);
        // Create an ad renderer and view binder that describe your native ad layout.
        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_layout)
                .iconImageId(R.id.native_icon)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_ad_privacy)
                .build();

        MoPubStaticNativeAdRenderer myRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
        adAdapter.registerAdRenderer(myRenderer);
        recyclerView.setAdapter(adAdapter);

        setContentView(recyclerView);

    }

    @Override
    public void onResume() {
        adAdapter.loadAds(getString(R.string.mopub_native_ad_unit_id));
        super.onResume();
    }

    static class VH extends RecyclerView.ViewHolder {

        final TextView textView;

        public VH(TextView itemView) {
            super(itemView);
            this.textView = itemView;
        }

    }

    class Adapter extends RecyclerView.Adapter<VH> {

        List<String> items;

        Adapter(List<String> items) {
            this.items = items;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
            return new VH(textView);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.textView.setText(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}
