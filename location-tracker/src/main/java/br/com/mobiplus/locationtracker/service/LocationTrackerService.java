package br.com.mobiplus.locationtracker.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import br.com.mobiplus.locationtracker.tracker.LocationTracker;

public class LocationTrackerService extends Service {

    private LocationTracker locationTracker;

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
