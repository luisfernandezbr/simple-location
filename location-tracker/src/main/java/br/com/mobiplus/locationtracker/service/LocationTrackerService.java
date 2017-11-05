package br.com.mobiplus.locationtracker.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;

import br.com.mobiplus.locationtracker.tracker.LocationTracker;

public class LocationTrackerService extends Service {

    private static final String TAG = "LocationTrackerService";

    private LocationTracker locationTracker;
    private LocationRequest locationRequest;

    public static void start(final Context context) {
        Log.d(TAG, "start() Called!");
        context.startService(getIntent(context));
    }

    public static void start(final Context context, LocationRequest locationRequest) {
        Log.d(TAG, "start() Called!");
        context.startService(getIntent(context, locationRequest));
    }

    public static void stop(final Context context) {
        Log.d(TAG, "stop() Called!");
        context.stopService(getIntent(context));
    }

    public static boolean isServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationTrackerService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @NonNull
    private static Intent getIntent(Context context, LocationRequest locationRequest) {
        Intent intent = getIntent(context);
        intent.putExtra("LocationRequest", locationRequest);
        return intent;
    }

    private static Intent getIntent(Context context) {
        return new Intent(context, LocationTrackerService.class);
    }

    public LocationTrackerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationRequest = intent.getParcelableExtra("LocationRequest");

        this.initLocationHandler();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initLocationHandler() {
        if (locationTracker == null) {
            locationTracker = new LocationTracker(this);
        }

        if (locationRequest != null) {
            locationTracker.init(locationRequest);
        } else {
            locationTracker.init();
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");

        locationTracker.stop();

        super.onDestroy();
    }
}
