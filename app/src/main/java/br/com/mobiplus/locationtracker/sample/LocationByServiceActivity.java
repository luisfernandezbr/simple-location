package br.com.mobiplus.locationtracker.sample;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.ref.WeakReference;

import br.com.mobiplus.locationtracker.service.LocationTrackerService;
import br.com.mobiplus.locationtracker.tracker.LocationTracker;

public class LocationByServiceActivity extends AppCompatActivity {

    private static final String TAG = "LocationByService";
    private PermissionListener permissionListener;
    private LocationTrackerService.OnLocationUpdateListener onLocationUpdateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.handleLocationPermission();
    }

    private void handleLocationPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this.getPermissionListener())
                .check();
    }

    private void startToListeningLocations() {
        LocationTrackerService.startAndRegister(this, this.getOnLocationUpdateListener());
    }

    @NonNull
    private PermissionListener getPermissionListener() {
        if (permissionListener == null) {
            permissionListener = new PermissionListener() {
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
            };
        }

        return permissionListener;
    }

    @NonNull
    private LocationTrackerService.OnLocationUpdateListener getOnLocationUpdateListener() {
        if (onLocationUpdateListener == null) {
            onLocationUpdateListener = new LocationTrackerService.OnLocationUpdateListener() {
                @Override
                public void onLocationUpdated(Location location) {
                    Log.d(TAG, "onReceive: " + location.toString());
                }
            };
        }

        return onLocationUpdateListener;
    }
}
