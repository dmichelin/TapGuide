package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.util.SparseArray
import com.yelp.fusion.client.models.Business

/**
 * Created by Daniel on 11/26/2017.
 */
interface InitializeTripView{
    fun showBuildingTripDialog()
    fun navigateToNextScreen(businesses: HashMap<Int,Business>)
}