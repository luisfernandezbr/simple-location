package br.com.mobiplus.simplelocation.sample;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import br.com.mobiplus.simplelocation.tracker.LocationTracker;

import static br.com.mobiplus.simplelocation.tracker.LocationTracker.ACTION_ON_LOCATION_UPDATE;
import static br.com.mobiplus.simplelocation.tracker.LocationTracker.ACTION_ON_PERMISSION_REQUIRED;

/**
 * Created by luisfernandez on 04/11/17.
 */

public abstract class BaseLocationActivity extends AppCompatActivity {

    private static final String TAG = "BaseLocationActivity";
    private BroadcastReceiver broadcastReceiver;
    private PermissionListener permissionListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
        super.onStop();
    }

    protected void registerReceiver() {
        IntentFilter intentFilter = this.getIntentFilter();
        BroadcastReceiver broadcastReceiver = this.getBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    protected IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ON_LOCATION_UPDATE);
        intentFilter.addAction(LocationTracker.ACTION_ON_PERMISSION_REQUIRED);
        return intentFilter;
    }

    protected void handleLocationPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this.getPermissionListener())
                .check();
    }

    @NonNull
    protected BroadcastReceiver getBroadcastReceiver() {
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    switch (action) {
                        case ACTION_ON_LOCATION_UPDATE : {
                            Location location = intent.getParcelableExtra(LocationTracker.EXTRA_LOCATION);
                            Log.d(TAG, "onReceive: " + location.toString());
                            break;
                        }
                        case ACTION_ON_PERMISSION_REQUIRED : {
                            Log.d(TAG, "Problems. Permission required!!!");
                            break;
                        }
                    }
                }
            };
        }

        return broadcastReceiver;
    }

    @NonNull
    protected PermissionListener getPermissionListener() {
        if (permissionListener == null) {
            permissionListener = new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    registerReceiver();
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

    public abstract void startToListeningLocations();
}
