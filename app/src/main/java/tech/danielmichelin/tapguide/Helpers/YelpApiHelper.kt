package tech.danielmichelin.tapguide.Helpers

import android.os.AsyncTask
import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory

/**
 * Created by Daniel on 11/26/2017.
 */
object YelpApiHelper{
    val clientId = "aQpdbu-Y1NYgJhnAJsLa0w"
    val clientSecret = "oJAdpOFKbeDToUkcsb29qmxJ5fA9uuwD602SoVp37yW9ghbkIQdU8ODBhJchxbhY"
    public val yelpApi :YelpFusionApi
    get() {
        var factory = YelpFusionApiFactory()
        return factory.createAPI(clientId, clientSecret)
    }








}