package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.os.AsyncTask
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.exception.exceptions.UnexpectedAPIError
import com.yelp.fusion.client.models.Business
import tech.danielmichelin.tapguide.Enums.Distances
import tech.danielmichelin.tapguide.Enums.PriceLevels
import tech.danielmichelin.tapguide.Helpers.YelpApiHelper
import tech.danielmichelin.tapguide.Model.TGBusiness
import tech.danielmichelin.tapguide.Screens.TripOverviewScreen.TripOverviewActivity

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

    inner class buildItineraryTask(var error: Boolean = false) : AsyncTask<MutableMap<String, String>, Int, Map<String, List<TGBusiness>>>() {
        var errorMsg: String? = "The error was.... no error?"
        var tripName: String? = null
        override fun onPreExecute() {
            super.onPreExecute()
            tripView.showBuildingTripDialog()
        }

        override fun doInBackground(vararg params: MutableMap<String, String>): Map<String, List<TGBusiness>> {
            tripName = params[0]["location"]
            var factory = YelpFusionApiFactory()
            val newList = mutableListOf<TGBusiness>()
            val businessMap = HashMap<String, MutableList<TGBusiness>>()
            try {
                val api = factory.createAPI(YelpApiHelper.apiKey)
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

                // convert the businesses to TGBusinesses
                val tgBreakfast = ArrayList<TGBusiness>()
                breakfast.businesses.forEach { business -> tgBreakfast.add(TGBusiness(business, "Breakfast")) }

                // convert the businesses to TGBusinesses
                val tgNightlife = ArrayList<TGBusiness>()
                nightlife.businesses.forEach { business -> tgNightlife.add(TGBusiness(business, "Nightlife")) }

                // convert the businesses to TGBusinesses
                val tgFood = ArrayList<TGBusiness>()
                food.businesses.forEach { business -> tgFood.add(TGBusiness(business, "Lunch/Dinner")) }

                // convert the businesses to TGBusinesses
                val tgActivities = ArrayList<TGBusiness>()
                activities.businesses.forEach { business -> tgActivities.add(TGBusiness(business, "Activity")) }

                businessMap[TripOverviewActivity.BUSINESSES] = newList
                businessMap[TripOverviewActivity.BREAKFAST_OPTIONS] = tgBreakfast
                businessMap[TripOverviewActivity.NIGHTLIFE_OPTIONS] = tgNightlife
                businessMap[TripOverviewActivity.ACTIVITY_OPTIONS] = tgActivities
                businessMap[TripOverviewActivity.NON_BREAKFAST_OPTIONS] = tgFood
                if (newList.size < 1) {
                    error = true
                    errorMsg = "No results were found! Try a different search criteria"
                }

            } catch (err: UnexpectedAPIError) {
                error = true
                errorMsg = err.description
            }
            return businessMap
        }

        override fun onPostExecute(result: Map<String, List<TGBusiness>>) {
            super.onPostExecute(result)

            if (!error) tripView.navigateToTripOverviewScreen(result, tripName)
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