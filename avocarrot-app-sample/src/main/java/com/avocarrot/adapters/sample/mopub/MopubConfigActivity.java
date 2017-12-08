package com.avocarrot.adapters.sample.mopub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avocarrot.adapters.sample.IntentStarter;
import com.avocarrot.adapters.sample.R;
import com.avocarrot.adapters.sample.ScreenAdType;

public class MopubConfigActivity extends AppCompatActivity {
    @NonNull
    private static final String EXTRA_AD_UNIT_ID = "ad_unit_id";
    @NonNull
    private static final String EXTRA_AD_TYPE = "ad_type";
    @Nullable
    private EditText adUnitIdView;

    @NonNull
    public static Intent buildIntent(@NonNull final Context context, @ScreenAdType final int adTypeScreen, @NonNull final String defAdUnitId) {
        return new Intent(context, MopubConfigActivity.class).putExtra(EXTRA_AD_TYPE, adTypeScreen).putExtra(EXTRA_AD_UNIT_ID, defAdUnitId);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mopub_config);

        final Intent intent = getIntent();
        final String defAdUnitId = intent.getStringExtra(EXTRA_AD_UNIT_ID);
        final int adTypeScreen = intent.getIntExtra(EXTRA_AD_TYPE, -1);
        if (adTypeScreen < 0) {
            finish();
            return;
        }

        adUnitIdView = (EditText) findViewById(R.id.ad_unit_id);
        if (adUnitIdView != null) {
            adUnitIdView.setText(defAdUnitId);
            adUnitIdView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(final TextView textView, final int id, final KeyEvent keyEvent) {
                    if (id == R.id.next || id == EditorInfo.IME_NULL) {
                        attempt(adTypeScreen);
                        return true;
                    }
                    return false;
                }
            });
        }
        final Button actionView = (Button) findViewById(R.id.next_button);
        if (actionView != null) {
            actionView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View view) {
                    attempt(adTypeScreen);
                }
            });
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void attempt(final int adTypeScreen) {
        if (adUnitIdView == null) {
            return;
        }
        adUnitIdView.setError(null);
        final String adUnitId = adUnitIdView.getText().toString();
        if (TextUtils.isEmpty(adUnitId)) {
            adUnitIdView.setError(getString(R.string.error_field_required));
            adUnitIdView.requestFocus();
        } else {
            IntentStarter.startMopubAdTypeActivity(this, adTypeScreen, adUnitId);
            finish();
        }
    }
}
