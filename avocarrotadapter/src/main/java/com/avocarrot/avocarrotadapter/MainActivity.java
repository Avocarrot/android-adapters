package com.avocarrot.avocarrotadapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mopub.mobileads.AvocarrotInterstitialMopub;
import com.mopub.nativeads.AvocarrotNativeMopub;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String GITHUB_URL = "https://github.com/Avocarrot/android-adapter/avocarrotadapter/src/main/java/";

    TextView className;
    TextView classPathGithub;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.main_title);

        className = (TextView) findViewById(R.id.class_name);
        classPathGithub = (TextView) findViewById(R.id.class_github_url);

        spinner = (Spinner) findViewById(R.id.custom_adapter_spinner);
        spinner.setAdapter(new ArrayAdapter<CustomAdapterItem>(this, android.R.layout.simple_list_item_1, getSupportedItems()));
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        CustomAdapterItem item = (CustomAdapterItem) adapterView.getAdapter().getItem(position);

        className.setText(item.getCustomAdapterClassName().getSimpleName());
        classPathGithub.setText(GITHUB_URL + item.getCustomAdapterClassName().getName().replace(".", "/"));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        className.setText("");
        classPathGithub.setText("");
    }

    public void show_example(View v) {
        try {
            startActivity(new Intent(this, ((CustomAdapterItem) spinner.getSelectedItem()).getActivityToLoad()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<CustomAdapterItem> getSupportedItems() {

        List<CustomAdapterItem> supportedCustomAdapters = new ArrayList<>();
        /* Mopub > Interstitial */
        supportedCustomAdapters.add(
            new CustomAdapterItem(
                getString(R.string.mopub_intestitial),
                AvocarrotInterstitialMopub.class,
                InterstitialMopubActivity.class));
        /* Mopub > Native */
        supportedCustomAdapters.add(
            new CustomAdapterItem(
                getString(R.string.mopub_native),
                AvocarrotNativeMopub.class,
                NativeMopubActivity.class));
        /* Mopub > Native with RecyclerView */
        supportedCustomAdapters.add(
            new CustomAdapterItem(
                getString(R.string.mopub_native_with_recycler_view),
                AvocarrotNativeMopub.class,
                NativeMopubRecyclerActivity.class));

        return  supportedCustomAdapters;
    }

}
