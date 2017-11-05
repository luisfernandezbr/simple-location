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

    public static final int REQUEST_CODE_INTERNAL = 2938;

    private Context context;
    private GoogleApiClient googleApiClient;
    private LocationListener locationListener;

    ConnectionCallbacksImpl(Context context, LocationListener locationListener) {
        this.context = context;
        this.locationListener = locationListener;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        handleLocationSettings();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.w(TAG, "onConnectionSuspended(): ");
    }

    void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    @SuppressLint("MissingPermission")
    void handleOnConnected() {
        Log.d(TAG, "handleOnConnected(): ");
        LocationRequest request = this.loadLocationRequest();

        boolean permissionsGranted = this.isPermissionGranted();

        if (permissionsGranted) {
            //noinspection deprecation
            FusedLocationApi.requestLocationUpdates(googleApiClient, request, locationListener);
        } else {
            Intent intent = new Intent(LocationTracker.ACTION_ON_PERMISSION_REQUIRED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            Log.w(TAG, "Permission not granted!");
        }
    }

    private void handleLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(this.loadLocationRequest());

        //noinspection deprecation
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Log.d(TAG, "LocationServices.ResultCallback.onResult(): ");

                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS: {
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        handleOnResultSuccess();
                        break;
                    }
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: {
                        handleOnResolutionRequired(status);
                        break;
                    }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                    }
                    default: {
                        Log.e(TAG, "Location Setting not satisfied and is not possible to handle using Resolution Dialog!");
                    }
                }
            }

            private void handleOnResultSuccess() {
                handleOnConnected();
            }

            private void handleOnResolutionRequired(final Status status) {
                if (context instanceof Activity) {
                    try {
                        status.startResolutionForResult((Activity) context, REQUEST_CODE_INTERNAL);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Show Notifitication with pendingIntent to Open
                }
            }
        });
    }

    private boolean isPermissionGranted() {
        PermissionHandler permissionHandler = new PermissionHandler();
        return permissionHandler.arePermissionsGranted(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        );
    }

    private LocationRequest loadLocationRequest() {
        return new RequestLocation.Builder()
                .setInterval(10000)
                .setRequestPriority(RequestPriority.PRIORITY_HIGH_ACCURACY)
                .build();
    }
}
