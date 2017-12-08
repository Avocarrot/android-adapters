package com.avocarrot.adapters.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jakewharton.processphoenix.ProcessPhoenix;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final int RC_ENDPOINT_CHANGED = 100;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(actionBar.getTitle() + "-" + BuildConfig.VERSION_NAME);
        }

        final ListView listView = new ListView(this);
        listView.setAdapter(new MainAdapter(this));
        listView.setOnItemClickListener(this);
        setContentView(listView);
    }

    @Override
    public void onItemClick(@NonNull final AdapterView<?> parent, @NonNull final View view, final int position, final long id) {
        switch (position) {
            case 0:
                IntentStarter.startMopubActivity(this);
                break;
            case 1:
                IntentStarter.startAdmobActivity(this);
                break;
            case 2:
                IntentStarter.startDFPActivity(this);
                break;
            default:
                throw new IllegalStateException("Unknown position [" + position + "]");
        }
    }

    private static final class MainAdapter extends ArrayAdapter<String> {

        private MainAdapter(@NonNull final Context context) {
            super(context, android.R.layout.simple_list_item_1, android.R.id.text1, context.getResources().getStringArray(R.array.adapters));
        }
    }
}
