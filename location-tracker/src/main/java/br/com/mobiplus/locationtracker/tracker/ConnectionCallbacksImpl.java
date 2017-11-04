package br.com.mobiplus.locationtracker.tracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        handleLocationSettings();
    }

    public void handleLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(this.loadLocationRequest());

        //noinspection deprecation
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Log.d(TAG, "onResult(): ");

                Status status = locationSettingsResult.getStatus();
                int statusCode = status.getStatusCode();

                if (LocationSettingsStatusCodes.SUCCESS == statusCode) {
                    handleOnConnected();
                } else {
                    if (context instanceof Activity) {
                        try {
                            status.startResolutionForResult((Activity) context, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Show Notifitication with pendingIntent to Open
                    }

                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void handleOnConnected() {
        Log.d(TAG, "onConnected(): ");
        LocationRequest request = this.loadLocationRequest();

        PermissionHandler permissionHandler = new PermissionHandler();

        boolean permissionsGranted = permissionHandler.arePermissionsGranted(context, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionsGranted) {
            //noinspection deprecation
            FusedLocationApi.requestLocationUpdates(googleApiClient, request, locationListener);
        } else {
            // TODO define the best way to handle this
            Log.w(TAG, "Permission not granted!");
        }
    }

    private LocationRequest loadLocationRequest() {
        return new RequestLocation.Builder()
                .setInterval(10000)
                .setRequestPriority(RequestPriority.PRIORITY_HIGH_ACCURACY)
                .build();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.w(TAG, "onConnectionSuspended(): ");
    }
}
