package br.com.mobiplus.locationtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

/**
 * Created by luisfernandez on 04/11/17.
 */

public class ConnectionCallbacksImpl implements GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "ConnectionCallbacksImpl";

    private Context context;
    private GoogleApiClient googleApiClient;
    private LocationListener locationListener;

    public ConnectionCallbacksImpl(Context context, LocationListener locationListener) {
        this.context = context;
        this.locationListener = locationListener;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected(): ");
        LocationRequest request = this.loadLocationRequest();

        PermissionHandler permissionHandler = new PermissionHandler();

        boolean permissionsGranted = permissionHandler.arePermissionsGranted(context, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionsGranted) {
            //noinspection deprecation
            FusedLocationApi.requestLocationUpdates(googleApiClient, request, locationListener);
        }
    }

    private LocationRequest loadLocationRequest() {
        return new RequestLocation.Builder()
                .setInterval(10000)
                .setRequestPriority(RequestPriority.PRIORITY_HIGH_ACCURACY)
                .build();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended(): ");
    }
}
