package br.com.mobiplus.locationtracker.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import br.com.mobiplus.locationtracker.LocationTrackerService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Location location = intent.getParcelableExtra(LocationTrackerService.EXTRA_LOCATION);
                Log.d(TAG, "onReceive: " + location.toString());
            }
        }, new IntentFilter(LocationTrackerService.ACTION_ON_LOCATION_UPDATE));

        startService(new Intent(this, LocationTrackerService.class));

    }
}
