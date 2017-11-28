package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.os.AsyncTask
import android.util.Log
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.Business
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

    inner class buildItineraryTask : AsyncTask<MutableMap<String, String>, Int, MutableList<Business>>(){
        override fun onPreExecute() {
            super.onPreExecute()
            tripView.showBuildingTripDialog()
        }
        override fun doInBackground(vararg params: MutableMap<String, String>): MutableList<Business> {
            var factory = YelpFusionApiFactory()
            val api = factory.createAPI(YelpApiHelper.clientId, YelpApiHelper.clientSecret)
            val response = api.getBusinessSearch(params[0]).execute().body()
            // get the highest rated businesses
            response.businesses.sortBy { business -> business.rating }
            // Todo: Do more shit with this algorithm
            return response.businesses.subList(0,5)
        }

        override fun onPostExecute(result: MutableList<Business>) {
            super.onPostExecute(result)


            tripView.navigateToNextScreen(result)
        }
    }

}