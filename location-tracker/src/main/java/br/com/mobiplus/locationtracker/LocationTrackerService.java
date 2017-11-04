package br.com.mobiplus.locationtracker;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationTrackerService extends Service implements LocationListener {

    private static final String TAG = "LocationTrackerService";

    public static final String ACTION_ON_LOCATION_UPDATE = "br.com.mobiplus.locationtracker.ACTION_ON_LOCATION_UPDATE";
    public static final String EXTRA_LOCATION = "br.com.mobiplus.locationtracker.EXTRA_LOCATION";

    private GoogleApiClient googleApiClient;
    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener;
    private GoogleApiClient.ConnectionCallbacks connectionCallbacks;

    public LocationTrackerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.initServiceDependencies();
        this.startLocationTracking();
    }

    @Override
    public void onDestroy() {
        this.stopToReceivingLocationUpdates();

        super.onDestroy();
    }

    private void stopToReceivingLocationUpdates() {
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Intent intent = new Intent(ACTION_ON_LOCATION_UPDATE);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void initServiceDependencies() {
        connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Log.d(TAG, "onConnected(): ");
                LocationRequest request = this.getLocationRequest();

                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, LocationTrackerService.this);
            }

            @NonNull
            private LocationRequest getLocationRequest() {
                LocationRequest request = new LocationRequest();
                request.setInterval(5);
                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                return request;
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(TAG, "onConnectionSuspended(): ");
            }
        };
        onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.d(TAG, "onConnectionFailed(): ");
            }
        };
    }

    private void startLocationTracking() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
}
