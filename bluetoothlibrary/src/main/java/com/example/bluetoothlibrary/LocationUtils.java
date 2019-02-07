package com.example.bluetoothlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/**
 * Created by qindachang on 2017/2/23.
 */

public class LocationUtils {

    private LocationUtils() {
        //no instance
    }


    public static boolean isOpenLocService(final Context context) {
        boolean isGps = false;
        boolean isNetwork = false;
        if (context != null) {
            LocationManager locationManager
                    = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {

                isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
            if (isGps || isNetwork) {
                return true;
            }
        }
        return false;
    }


    public static void gotoLocServiceSettings(Activity activity) {
        final Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    public static void gotoLocServiceSettings(Activity activity, int requestCode) {
        final Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, requestCode);
    }
}
