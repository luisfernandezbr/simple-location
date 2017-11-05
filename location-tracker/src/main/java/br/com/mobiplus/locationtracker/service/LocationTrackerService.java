package br.com.mobiplus.locationtracker.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import br.com.mobiplus.locationtracker.tracker.LocationTracker;

public class LocationTrackerService extends Service {

    private static final String TAG = "LocationTrackerService";

    private LocationTracker locationTracker;

    public static void start(final Context context) {
        context.startService(getIntent(context));
    }

    public static void stop(final Context context) {
        context.stopService(getIntent(context));
    }

    @NonNull
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

        this.initLocationHandler();
    }

    private void initLocationHandler() {
        if (locationTracker == null) {
            locationTracker = new LocationTracker(this);
        }

        locationTracker.init();
    }

    @Override
    public void onDestroy() {
        locationTracker.stop();

        super.onDestroy();
    }
}
