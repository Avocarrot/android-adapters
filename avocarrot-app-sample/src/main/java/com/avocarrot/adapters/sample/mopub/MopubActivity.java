package com.avocarrot.adapters.sample.mopub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.avocarrot.adapters.sample.IntentStarter;
import com.avocarrot.adapters.sample.R;

public class MopubActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    @NonNull
    public static Intent buildIntent(@NonNull final Context context) {
        return new Intent(context, MopubActivity.class);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ListView listView = new ListView(this);
        listView.setAdapter(new MainAdapter(this));
        listView.setOnItemClickListener(this);
        setContentView(listView);
    }

    @Override
    public void onItemClick(@NonNull final AdapterView<?> parent, @NonNull final View view, final int position, final long id) {
        switch (position) {
            case 0:
                IntentStarter.startMopubConfigActivity(this, IntentStarter.SCREEN_NATIVE);
                break;
            case 1:
                IntentStarter.startMopubConfigActivity(this, IntentStarter.SCREEN_BANNER);
                break;
            case 2:
                IntentStarter.startMopubConfigActivity(this, IntentStarter.SCREEN_INTERSTITIAL);
                break;
            default:
                throw new IllegalStateException("Unknown position [" + position + "]");
        }
    }

    private static final class MainAdapter extends ArrayAdapter<String> {

        private MainAdapter(@NonNull final Context context) {
            super(context, android.R.layout.simple_list_item_1, android.R.id.text1, context.getResources().getStringArray(R.array.mopub_ad_types));
        }
    }
}
