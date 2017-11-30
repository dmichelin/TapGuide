package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.os.AsyncTask
import android.util.SparseArray
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.Business
import tech.danielmichelin.tapguide.Enums.Distances
import tech.danielmichelin.tapguide.Enums.PriceLevels
import tech.danielmichelin.tapguide.Helpers.YelpApiHelper
import tech.danielmichelin.tapguide.Model.TGBusiness

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

    inner class buildItineraryTask : AsyncTask<MutableMap<String, String>, Int, MutableList<TGBusiness>>(){
        override fun onPreExecute() {
            super.onPreExecute()
            tripView.showBuildingTripDialog()
        }
        override fun doInBackground(vararg params: MutableMap<String, String>): MutableList<TGBusiness> {
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

            val newList = mutableListOf<TGBusiness>()
            val businessList = mutableListOf<Business>()
            // add breakfast
            if(breakfast.businesses.size>0){
                val item = TGBusiness(breakfast.businesses.first(), "Breakfast")
                breakfast.businesses.removeAt(0);
                newList.add(item)
            }
            // add morning activity
            if(activities.businesses.size>0){
                val item = TGBusiness(activities.businesses.first(),"Activity")
                activities.businesses.removeAt(0)
                newList.add(item)
            }
            // add lunch
            if(food.businesses.size>0){
                val item = TGBusiness(food.businesses.first(), "Lunch")
                food.businesses.removeAt(0)
                newList.add(item)
            }
            // add afternoon activity
            if(activities.businesses.size>0){
                val item = TGBusiness(activities.businesses.first(), "Activity")
                activities.businesses.removeAt(0)
                newList.add(item)
            }
            // add second afternoon activity
            if(activities.businesses.size>0){
                val item = TGBusiness(activities.businesses.first(), "Activity")
                activities.businesses.removeAt(0)
                newList.add(item)
            }
            // add dinner
            if(food.businesses.size>0){
                val item = TGBusiness(food.businesses.first(), "Dinner")
                food.businesses.removeAt(0)
                newList.add(item)
            }
            // add night activity
            if(nightlife.businesses.size>0){
                val item = TGBusiness(nightlife.businesses.first(), "Nightlife")
                nightlife.businesses.removeAt(0)
                newList.add(item)
            }

            return newList
        }

        override fun onPostExecute(result: MutableList<TGBusiness>) {
            super.onPostExecute(result)
            tripView.navigateToNextScreen(result)
        }
    }

}