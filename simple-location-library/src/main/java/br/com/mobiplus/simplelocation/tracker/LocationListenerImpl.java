package br.com.mobiplus.simplelocation.tracker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

import static br.com.mobiplus.simplelocation.tracker.LocationTracker.ACTION_ON_LOCATION_UPDATE;
import static br.com.mobiplus.simplelocation.tracker.LocationTracker.EXTRA_LOCATION;

/**
 * Created by luisfernandez on 04/11/17.
 */

public class LocationListenerImpl implements LocationListener {

    private static final String TAG = "LocationListenerImpl";

    private Context context;

    public LocationListenerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location.toString());
        this.sendOnLocationUpdateBroadcast(location);
    }

    private void sendOnLocationUpdateBroadcast(Location location) {
        Intent intent = new Intent(ACTION_ON_LOCATION_UPDATE);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
