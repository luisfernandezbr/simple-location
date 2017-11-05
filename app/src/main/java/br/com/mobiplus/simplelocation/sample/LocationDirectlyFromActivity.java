package br.com.mobiplus.simplelocation.sample;

import android.content.Intent;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import br.com.mobiplus.simplelocation.tracker.LocationTracker;

public class LocationDirectlyFromActivity extends BaseLocationActivity {

    private static final String TAG = "LocationDirectlyFrom";

    private WeakReference<LocationTracker> locationTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.handleLocationPermission();
    }

    @Override
    public void startToListeningLocations() {
        locationTracker = new WeakReference<>(new LocationTracker(this));
        locationTracker.get().init();
    }

    @Override
    protected void onStop() {
        locationTracker.get().stop();

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationTracker.get().onActivityResult(requestCode, resultCode, data);
    }
}
