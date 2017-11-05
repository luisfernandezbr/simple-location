package br.com.mobiplus.locationtracker.tracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import br.com.mobiplus.locationtracker.service.LocationTrackerService;

import static br.com.mobiplus.locationtracker.tracker.ConnectionCallbacksImpl.REQUEST_CODE_INTERNAL;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;


/**
 * Created by luisfernandez on 04/11/17.
 */
public class LocationTracker {

    private static final String TAG = "LocationTracker";

    public static final String ACTION_ON_LOCATION_UPDATE = "br.com.mobiplus.locationtracker.ACTION_ON_LOCATION_UPDATE";
    public static final String ACTION_ON_PERMISSION_REQUIRED = "br.com.mobiplus.locationtracker.ACTION_ON_PERMISSION_REQUIRED";

    public static final String EXTRA_LOCATION = "br.com.mobiplus.locationtracker.EXTRA_LOCATION";

    private Context context;

    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener;
    private ConnectionCallbacksImpl connectionCallbacks;
    private LocationListener locationListener;

    public LocationTracker(Context context) {
        this.context = context;
        this.locationListener = new LocationListenerImpl(context);
    }

    private GoogleApiClient googleApiClient;

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
                Log.e(TAG, "onConnectionFailed(): ");
            }
        };

        googleApiClient = this.getGoogleApiClient();
        connectionCallbacks.setGoogleApiClient(googleApiClient);
        googleApiClient.connect();
    }

    @NonNull
    private GoogleApiClient getGoogleApiClient() {
        return new GoogleApiClient.Builder(this.context)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
    }

    public void stop() {
        this.stopToReceivingLocationUpdates();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_INTERNAL) {
            switch (resultCode) {

                case Activity.RESULT_OK : {
                    connectionCallbacks.handleOnConnected();
                    break;
                }
                case Activity.RESULT_CANCELED : {
                    Log.w(TAG, "User has canceled the Permission Dialog.");
                    break;
                }
                default: {
                    Log.w(TAG, "Oh! Unknown resultCode = " + resultCode);
                }
            }
        }
    }
}
