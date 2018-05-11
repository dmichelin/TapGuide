package tech.danielmichelin.tapguide.Helpers

import android.os.AsyncTask
import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory

/**
 * Created by Daniel on 11/26/2017. Provides access to the Yelp API
 */
object YelpApiHelper{
    val apiKey = "_oGWTwCIAkp9KXQn5uL5w3Mc1VIgXOmagNSdubrpKoelns70Q9CqwpI14W_hadt0GNOckHCFRC6-1eONrqMmnUPicm98bCuptxR_GD5qTofKtPqww9AGL6BW2fH1WnYx"
    public val yelpApi :YelpFusionApi
    get() {
        var factory = YelpFusionApiFactory()
        return factory.createAPI(apiKey)
    }








}