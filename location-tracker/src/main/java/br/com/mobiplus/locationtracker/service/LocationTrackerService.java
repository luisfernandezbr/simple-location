package br.com.mobiplus.locationtracker.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import br.com.mobiplus.locationtracker.tracker.LocationTracker;

public class LocationTrackerService extends Service {

    private static final String TAG = "LocationTrackerService";

    private LocationTracker locationTracker;

    public static void start(final Context context) {
        context.startService(new Intent(context, LocationTrackerService.class));
    }

    /**
     * Intent Action to register: br.com.mobiplus.locationtracker.tracker.LocationTracker.ACTION_ON_LOCATION_UPDATE
     * Extra to get Location: br.com.mobiplus.locationtracker.tracker.LocationTracker.EXTRA_LOCATION
     * @param context
     * @param broadcastReceiver
     */
    public static void registerReceiver(final Context context, final BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(LocationTracker.ACTION_ON_LOCATION_UPDATE));
    }

    public static void startAndRegister(final Context context, final OnLocationUpdateListener onLocationUpdateListener) {
        LocationTrackerService.start(context);
        LocationTrackerService.registerReceiver(context, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Location location = intent.getParcelableExtra(LocationTracker.EXTRA_LOCATION);
                onLocationUpdateListener.onLocationUpdated(location);
            }
        });
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

    public interface OnLocationUpdateListener {
        void onLocationUpdated(Location location);
    }
}
