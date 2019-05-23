package com.example.admincik.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvSteps)
    TextView tvSteps;

    @BindView(R.id.tvCountSteps)
    TextView tvCountSteps;

    private int countSteps;

    private final static String INTENT_ACTION = "PedometerUpdate";
    private final static String EXTRA_NAME = "steps";
    private final static String EXTRA_NAME_ERROR = "error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(INTENT_ACTION));
        startService(new Intent(this, PedometerService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_NAME, countSteps);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvCountSteps.setText(String.valueOf(savedInstanceState.getInt(EXTRA_NAME)));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(EXTRA_NAME_ERROR, false)) {
                tvCountSteps.setText(R.string.notAvailable);
                tvSteps.setVisibility(View.GONE);
            } else {
                countSteps = intent.getIntExtra(EXTRA_NAME, 0);
                tvCountSteps.setText(String.valueOf(countSteps));
            }
        }
    };
}
