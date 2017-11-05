package br.com.mobiplus.locationtracker.sample;

import android.os.Bundle;

import br.com.mobiplus.locationtracker.service.LocationTrackerService;

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
        LocationTrackerService.start(this);
    }
}
