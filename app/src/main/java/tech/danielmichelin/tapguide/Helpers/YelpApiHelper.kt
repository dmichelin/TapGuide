package tech.danielmichelin.tapguide.Helpers

import android.os.AsyncTask
import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory

/**
 * Created by Daniel on 11/26/2017. Provides access to the Yelp API
 */
object YelpApiHelper{
    val apiKey = "ZXfVYLA4XVmaVPwa1nPhyZKndM_OUS_w-rQ1ZIP_PCFwJTrqiskR0V3XYuBmeA1YAN7XNLkfpTpiDGF5KJdituEIT91hfJa2g2io50GCPNekx7GZiYSmgABpUf8aWnYx"
    public val yelpApi :YelpFusionApi
    get() {
        var factory = YelpFusionApiFactory()
        return factory.createAPI(apiKey)
    }








}