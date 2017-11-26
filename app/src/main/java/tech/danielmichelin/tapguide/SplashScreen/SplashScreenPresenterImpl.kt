package tech.danielmichelin.tapguide.SplashScreen

import android.os.AsyncTask
import android.util.Log
import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.SearchResponse
import tech.danielmichelin.tapguide.Helpers.YelpApiHelper

/**
 * Created by Daniel on 11/26/2017.
 */
class SplashScreenPresenterImpl(val view: SplashScreenView) : SplashScreenPresenter{

    override fun createApiAndLogin() {
        // test implementation
        var params : MutableMap<String, String> = HashMap()
        params["location"] = "90045"
        var task = SearchBusinessTask().execute(params)
    }

    inner class SearchBusinessTask: AsyncTask<MutableMap<String,String>,Int,SearchResponse>(){
        override fun doInBackground(vararg params: MutableMap<String, String>): SearchResponse {
            var factory = YelpFusionApiFactory()
            val api = factory.createAPI(YelpApiHelper.clientId, YelpApiHelper.clientSecret)
            val response = api.getBusinessSearch(params[0]).execute().body()
            return response
        }

        override fun onPostExecute(result: SearchResponse) {
            super.onPostExecute(result)
            Log.d("Test",result.total.toString())
            view.navigateToNext()
        }
    }


}