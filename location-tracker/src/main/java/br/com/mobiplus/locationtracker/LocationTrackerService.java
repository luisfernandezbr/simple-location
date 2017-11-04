package br.com.mobiplus.locationtracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class LocationTrackerService extends Service {

    private LocationHandler locationHandler;

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
        if (locationHandler == null) {
            locationHandler = new LocationHandler(this);
        }

        locationHandler.init();
    }

    @Override
    public void onDestroy() {
        locationHandler.stop();

        super.onDestroy();
    }
}
