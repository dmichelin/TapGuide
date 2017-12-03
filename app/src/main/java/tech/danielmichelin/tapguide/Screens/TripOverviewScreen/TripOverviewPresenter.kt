package tech.danielmichelin.tapguide.Screens.TripOverviewScreen

import android.net.Uri
import com.yelp.fusion.client.models.Business

/**
 * Created by Daniel on 12/3/2017.
 */
interface TripOverviewPresenter {
    fun generateLocationUri(startingCoordinates: Pair<Float?, Float?>, businesses: List<Business>): Uri
}