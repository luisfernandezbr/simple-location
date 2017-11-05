package br.com.mobiplus.locationtracker;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;

import br.com.mobiplus.locationtracker.service.LocationTrackerService;

/**
 * Created by luisfernandez on 05/11/17.
 */
public class LocationTrackerBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "LocationTrackerBroadcastReceiver";

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()");

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (!LocationTrackerService.isServiceRunning(context)) {
                LocationRequest request = new LocationRequest();
                request.setInterval(1000);
                request.setFastestInterval(1000);
                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationTrackerService.start(context, request);
            }
        }
    }
}
