package tech.danielmichelin.tapguide.Helpers

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log


/**
 * Created by Daniel on 11/26/2017.
 */
object LocationHelper{
    /**
     * Gets the best last known location of the phone in use
     * @return the location of the user
     */
    fun getLastBestLocation(context: Context): Location? {
        // get the location manager
        var lm : LocationManager =  context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try{
            val locationGPS = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val locationNet = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            var GPSLocationTime: Long = 0
            if (null != locationGPS) {
                GPSLocationTime = locationGPS.getTime()
            }

            var NetLocationTime: Long = 0

            if (null != locationNet) {
                NetLocationTime = locationNet.getTime()
            }

            return if (0 < GPSLocationTime - NetLocationTime) {
                locationGPS
            } else {
                locationNet
            }
        } catch (exception: SecurityException){
            Log.d("LocationHelper","Security exception")
        }
        return null
    }
}