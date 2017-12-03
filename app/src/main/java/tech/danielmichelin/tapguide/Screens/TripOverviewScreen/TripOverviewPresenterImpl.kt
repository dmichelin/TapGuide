package tech.danielmichelin.tapguide.Screens.TripOverviewScreen

import android.net.Uri
import com.yelp.fusion.client.models.Business

/**
 * Created by Daniel on 12/3/2017.
 */
class TripOverviewPresenterImpl(val view: TripOverviewView) : TripOverviewPresenter {
    override fun generateLocationUri(startingCoordinates: Pair<Float?, Float?>, businesses: List<Business>): Uri {
        var uriString = "https://www.google.com/maps/dir/?api=1&origin=" +
                startingCoordinates.first + "," + startingCoordinates.second +
                "&destination=" + businesses.last().coordinates.latitude + "," + businesses.last().coordinates.longitude +
                "&waypoints="
        // build the URI for the rest of the businesses

        // get the second from the last index
        for (i in 0..businesses.lastIndex - 1) {
            val business = businesses.get(i)
            if (i == businesses.lastIndex - 1) {
                uriString += business.coordinates.latitude.toString() + "," + business.coordinates.longitude
            } else {
                uriString += business.coordinates.latitude.toString() + "," + business.coordinates.longitude + "|"
            }
        }
        return Uri.parse(uriString)
    }
}