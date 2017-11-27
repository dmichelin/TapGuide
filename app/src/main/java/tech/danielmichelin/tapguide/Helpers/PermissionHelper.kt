package tech.danielmichelin.tapguide.Helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Created by Daniel on 11/26/2017.
 */
object PermissionHelper{
    val FINE_LOCATION_PERMISSION = 0

    fun checkAllPermissions(activity: Activity){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                         Array(1,{Manifest.permission.ACCESS_FINE_LOCATION}),
                        FINE_LOCATION_PERMISSION);
            }
        }
    }
}