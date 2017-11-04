package br.com.mobiplus.locationtracker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.LocationListener;

import static br.com.mobiplus.locationtracker.tracker.LocationTracker.ACTION_ON_LOCATION_UPDATE;
import static br.com.mobiplus.locationtracker.tracker.LocationTracker.EXTRA_LOCATION;

/**
 * Created by luisfernandez on 04/11/17.
 */

public class LocationListenerImpl implements LocationListener {

    Context context;

    public LocationListenerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        Intent intent = new Intent(ACTION_ON_LOCATION_UPDATE);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
