package br.com.mobiplus.locationtracker.tracker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by luisfernandez on 04/11/17.
 */

class PermissionHandler {

    public int checkSelfPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission);
    }

    public boolean isPermissionGranted(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == this.checkSelfPermission(context, permission);
    }

    public boolean arePermissionsGranted(Context context, String... permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (!this.isPermissionGranted(context, permissions[i])) {
                return false;
            }
        }

        return true;
    }
}
