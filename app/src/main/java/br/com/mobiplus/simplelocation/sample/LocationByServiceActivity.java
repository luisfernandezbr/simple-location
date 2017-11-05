package br.com.mobiplus.simplelocation.sample;

import android.os.Bundle;

import com.google.android.gms.location.LocationRequest;

import br.com.mobiplus.simplelocation.service.LocationTrackerService;

public class LocationByServiceActivity extends BaseLocationActivity {

    private static final String TAG = "LocationByService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.handleLocationPermission();
    }

    @Override
    public void startToListeningLocations() {
        LocationRequest request = new LocationRequest();
        request.setInterval(30000);
        request.setFastestInterval(30000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationTrackerService.start(this, request);
    }
}
