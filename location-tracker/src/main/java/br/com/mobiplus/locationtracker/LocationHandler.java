package br.com.mobiplus.locationtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;
import static com.google.android.gms.location.LocationServices.SettingsApi;

/**
 * Created by luisfernandez on 04/11/17.
 */

public class LocationHandler {

    public static final String ACTION_ON_LOCATION_UPDATE = "br.com.mobiplus.locationtracker.ACTION_ON_LOCATION_UPDATE";
    public static final String EXTRA_LOCATION = "br.com.mobiplus.locationtracker.EXTRA_LOCATION";

    private static final String TAG = "LocationHandler";

    private Context context;

    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener;
    private ConnectionCallbacksImpl connectionCallbacks;
    private LocationListener locationListener;

    public LocationHandler(Context context) {
        this.context = context;
        this.locationListener = new LocationListenerImpl(context);
    }

    private GoogleApiClient googleApiClient;



//    public void handleLocationSettings() {
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
//        LocationSettingsRequest.Builder locationRequestBuilder = builder.addLocationRequest(this.getLocationRequest());
//
//        //noinspection deprecation
//        PendingResult<LocationSettingsResult> result = SettingsApi.checkLocationSettings(googleApiClient, locationRequestBuilder.build());
//    }



    private void stopToReceivingLocationUpdates() {
        if (googleApiClient != null) {
            FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
        }
    }



    public void init() {
        connectionCallbacks = new ConnectionCallbacksImpl(context, locationListener);
        onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.d(TAG, "onConnectionFailed(): ");
            }
        };

        googleApiClient = new GoogleApiClient.Builder(this.context)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();

        connectionCallbacks.setGoogleApiClient(googleApiClient);
        googleApiClient.connect();
    }

    public void stop() {
        this.stopToReceivingLocationUpdates();
    }
}
