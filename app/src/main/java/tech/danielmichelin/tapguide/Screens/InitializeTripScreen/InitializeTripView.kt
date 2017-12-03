package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.util.SparseArray
import com.yelp.fusion.client.models.Business
import tech.danielmichelin.tapguide.Model.TGBusiness

/**
 * Created by Daniel on 11/26/2017.
 */
interface InitializeTripView{
    fun showBuildingTripDialog()
    fun navigateToNextScreen(businesses: MutableList<TGBusiness>)
}