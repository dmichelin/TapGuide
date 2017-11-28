package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.os.AsyncTask
import android.util.Log
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.SearchResponse
import tech.danielmichelin.tapguide.Enums.Distances
import tech.danielmichelin.tapguide.Enums.PriceLevels
import tech.danielmichelin.tapguide.Helpers.YelpApiHelper

/**
 * Created by Daniel on 11/27/2017.
 */
class InitializeTripPresenterImpl(val tripView: InitializeTripView): InitializeTripPresenter{
    override fun makeTripBuildRequest(distance: Distances, priceLevel: PriceLevels, location: String){
        val params : MutableMap<String,String> = HashMap()
        params["location"] = location
        params["price"] = priceLevel.price.toString()
        params["radius"] = distance.meters.toString()

        buildItineraryTask().execute(params)

    }

    inner class buildItineraryTask : AsyncTask<MutableMap<String, String>, Int, SearchResponse>(){
        override fun onPreExecute() {
            super.onPreExecute()
            tripView.showBuildingTripDialog()
        }
        override fun doInBackground(vararg params: MutableMap<String, String>): SearchResponse {
            var factory = YelpFusionApiFactory()
            val api = factory.createAPI(YelpApiHelper.clientId, YelpApiHelper.clientSecret)
            val response = api.getBusinessSearch(params[0]).execute().body()
            //TODO("Need to put trip building algorithm in")
            return response
        }

        override fun onPostExecute(result: SearchResponse) {
            super.onPostExecute(result)
            Log.d("Test",result.total.toString())

            tripView.navigateToNextScreen(result.businesses)
        }
    }

}