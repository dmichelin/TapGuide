package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.os.AsyncTask
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.exception.exceptions.UnexpectedAPIError
import com.yelp.fusion.client.models.Business
import tech.danielmichelin.tapguide.Enums.Distances
import tech.danielmichelin.tapguide.Enums.PriceLevels
import tech.danielmichelin.tapguide.Helpers.YelpApiHelper
import tech.danielmichelin.tapguide.Model.TGBusiness

/**
 * Created by Daniel on 11/27/2017.
 */
class InitializeTripPresenterImpl(val tripView: InitializeTripView): InitializeTripPresenter{
    var tripRequest: buildItineraryTask? = null
    override fun makeTripBuildRequest(distance: Distances, priceLevel: PriceLevels, location: String){
        val params : MutableMap<String,String> = HashMap()
        params["location"] = location
        params["price"] = priceLevel.price
        params["radius"] = distance.meters.toString()

        tripRequest = buildItineraryTask()
        tripRequest?.execute(params)

    }

    inner class buildItineraryTask(var error: Boolean = false) : AsyncTask<MutableMap<String, String>, Int, MutableList<TGBusiness>>() {
        var errorMsg: String? = "The error was.... no error?"
        override fun onPreExecute() {
            super.onPreExecute()
            tripView.showBuildingTripDialog()
        }
        override fun doInBackground(vararg params: MutableMap<String, String>): MutableList<TGBusiness> {
            var factory = YelpFusionApiFactory()
            val newList = mutableListOf<TGBusiness>()
            try {
                val api = factory.createAPI(YelpApiHelper.clientId, YelpApiHelper.clientSecret)
                params[0]["term"] = "breakfast"
                val breakfast = api.getBusinessSearch(params[0]).execute().body()
                params[0]["term"] = "food"
                val food = api.getBusinessSearch(params[0]).execute().body()
                params[0]["term"] = "must see"
                val activities = api.getBusinessSearch(params[0]).execute().body()
                params[0]["term"] = "nightlife"
                val nightlife = api.getBusinessSearch(params[0]).execute().body()
                // get the highest rated businesses

                breakfast.businesses.sortByDescending { business -> (business.rating - 3) * business.reviewCount }
                food.businesses.sortByDescending { business -> (business.rating - 3) * business.reviewCount }
                activities.businesses.sortByDescending { business -> (business.rating - 3) * business.reviewCount }
                nightlife.businesses.sortByDescending { business -> (business.rating - 3) * business.reviewCount }

                val businessList = mutableListOf<Business>()
                // add breakfast
                if (breakfast.businesses.size > 0 && !containsAll(newList, breakfast.businesses)) {
                    val item = TGBusiness(breakfast.businesses.first({ business -> !containsName(newList, business) }), "Breakfast")
                    newList.add(item)
                }
                // add morning activity
                if (activities.businesses.size > 0 && !containsAll(newList, activities.businesses)) {
                    val item = TGBusiness(activities.businesses.first({ business -> !containsName(newList, business) }), "Activity")
                    newList.add(item)
                }
                // add lunch
                if (food.businesses.size > 0 && !containsAll(newList, food.businesses)) {
                    val item = TGBusiness(food.businesses.first({ business -> !containsName(newList, business) }), "Lunch")
                    newList.add(item)
                }
                // add afternoon activity
                if (activities.businesses.size > 0 && !containsAll(newList, activities.businesses)) {
                    val item = TGBusiness(activities.businesses.first({ business -> !containsName(newList, business) }), "Activity")
                    newList.add(item)
                }
                // add second afternoon activity
                if (activities.businesses.size > 0 && !containsAll(newList, activities.businesses)) {
                    val item = TGBusiness(activities.businesses.first({ business -> !containsName(newList, business) }), "Activity")
                    newList.add(item)
                }
                // add dinner
                if (food.businesses.size > 0 && !containsAll(newList, food.businesses)) {
                    val item = TGBusiness(food.businesses.first({ business -> !containsName(newList, business) }), "Dinner")
                    newList.add(item)
                }
                // add night activity
                if (nightlife.businesses.size > 0 && !containsAll(newList, nightlife.businesses)) {
                    val item = TGBusiness(nightlife.businesses.first({ business -> !containsName(newList, business) }), "Nightlife")
                    newList.add(item)
                }

                breakfast.businesses.removeAll(newList)
                nightlife.businesses.removeAll(newList)
                food.businesses.removeAll(newList)
                activities.businesses.removeAll(newList)

            } catch (err: UnexpectedAPIError) {
                error = true
                errorMsg = err.description
            }
            return newList
        }

        override fun onPostExecute(result: MutableList<TGBusiness>) {
            super.onPostExecute(result)

            if (!error) tripView.navigateToNextScreen(result)
            else tripView.showErrorDialog(errorMsg)
        }
    }

    override fun cancelTripBuildRequest() {
        tripRequest?.cancel(true)
    }

    private fun containsName(list: MutableList<TGBusiness>, business: Business): Boolean {
        var notUnique = false;
        list.forEach({ b -> if (b.name.equals(business.name)) notUnique = true })
        return notUnique;
    }

    /**
     * Needed to mediate between TGBusiness and business
     */
    private fun containsAll(tgList: List<TGBusiness>, bList: List<Business>): Boolean {
        var containsAll = true;
        for (business in bList) {
            var found = false
            for (tgBusiness in tgList) {
                if (tgBusiness.equals(business)) {
                    found = true
                    break
                } else {
                    found = false
                }
            }
            containsAll = found
        }
        return containsAll && tgList.size > 0
    }

}