package br.com.mobiplus.locationtracker.sample;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import br.com.mobiplus.locationtracker.tracker.LocationTracker;
import br.com.mobiplus.locationtracker.service.LocationTrackerService;

public class MainActivity extends AppCompatActivity implements PermissionListener{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.handleLocationPermission();
    }

    private void handleLocationPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this).check();
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        startToListeningLocations();
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        Log.d(TAG, "onPermissionDenied: " + response.toString());
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        Log.d(TAG, "onPermissionRationaleShouldBeShown: " + permission.toString());
    }

    private void startToListeningLocations() {
        LocationTrackerService.startAndRegister(this, new LocationTrackerService.OnLocationUpdateListener() {
            @Override
            public void onLocationUpdated(Location location) {
                Log.d(TAG, "onReceive: " + location.toString());
            }
        });
    }
}
