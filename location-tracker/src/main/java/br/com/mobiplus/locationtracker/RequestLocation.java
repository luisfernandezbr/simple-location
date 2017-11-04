package br.com.mobiplus.locationtracker;


import static com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.google.android.gms.location.LocationRequest.PRIORITY_LOW_POWER;
import static com.google.android.gms.location.LocationRequest.PRIORITY_NO_POWER;

/**
 * Created by luisfernandez on 04/11/17.
 */

public class RequestLocation {

    public static final class Builder {
        private long interval;
        private RequestPriority requestPriority;
        private long fastestInterval;

        public Builder() {
        }

        private Builder(long interval, RequestPriority requestPriority, long fastestInterval) {
            this.interval = interval;
            this.requestPriority = requestPriority;
            this.fastestInterval = fastestInterval;
        }

        public Builder setInterval(long interval) {
            this.interval = interval;
            return this;
        }

        public Builder setRequestPriority(RequestPriority requestPriority) {
            this.requestPriority = requestPriority;
            return this;
        }

        public Builder setFastestInterval(long fastestInterval) {
            this.fastestInterval = fastestInterval;
            return this;
        }

        public com.google.android.gms.location.LocationRequest build() {
            com.google.android.gms.location.LocationRequest request = new com.google.android.gms.location.LocationRequest();
            request.setInterval(this.interval);
            request.setFastestInterval(this.fastestInterval);

            switch (this.requestPriority) {
                case PRIORITY_NO_POWER: {
                    request.setPriority(PRIORITY_NO_POWER);
                    break;
                }
                case PRIORITY_LOW_POWER: {
                    request.setPriority(PRIORITY_LOW_POWER);
                    break;
                }
                case PRIORITY_HIGH_ACCURACY: {
                    request.setPriority(PRIORITY_HIGH_ACCURACY);
                    break;
                }
                case PRIORITY_BALANCED_POWER_ACCURACY: {
                    request.setPriority(PRIORITY_BALANCED_POWER_ACCURACY);
                    break;
                }
            }

            return request;
        }
    }
}
