package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.os.AsyncTask
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.Business
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
        params["price"] = priceLevel.price
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
            params[0]["term"]="breakfast"
            val breakfast = api.getBusinessSearch(params[0]).execute().body()
            params[0]["term"]="food"
            val food = api.getBusinessSearch(params[0]).execute().body()
            params[0]["term"]="must see"
            val activities = api.getBusinessSearch(params[0]).execute().body()
            params[0]["term"]="nightlife"
            val nightlife = api.getBusinessSearch(params[0]).execute().body()
            // get the highest rated businesses

            breakfast.businesses.sortByDescending { business -> (business.rating-3)*business.reviewCount}
            food.businesses.sortByDescending { business -> (business.rating-3)*business.reviewCount}
            activities.businesses.sortByDescending{business -> (business.rating-3)*business.reviewCount}
            nightlife.businesses.sortByDescending{business -> (business.rating-3)*business.reviewCount}

            val newList = ArrayList<Business>()
            // add breakfast
            if(breakfast.businesses.size>0)newList.add(breakfast.businesses.first({business ->!containsName(newList,business)}))
            // add morning activity
            if(activities.businesses.size>0)newList.add(activities.businesses.first({business ->!containsName(newList,business)}))
            // add lunch
            if(food.businesses.size>0)newList.add(food.businesses.first({business ->!containsName(newList,business)}))
            // add afternoon activity
            if(activities.businesses.size>1)newList.add(activities.businesses.first({business ->!containsName(newList,business)}))
            // add dinner
            if(food.businesses.size>1)newList.add(food.businesses.first({business ->!containsName(newList,business)}))
            // add night activity
            if(nightlife.businesses.size>1)newList.add(nightlife.businesses.first({business ->!containsName(newList,business)}))

            return newList
        }

        override fun onPostExecute(result: MutableList<Business>) {
            super.onPostExecute(result)
            tripView.navigateToNextScreen(result)
        }

        private fun containsName(list: MutableList<Business>,business: Business): Boolean{
            var notUnique = false;
            list.forEach({b -> if(b.name.equals(business.name)) notUnique= true})
            return notUnique;
        }
    }

}