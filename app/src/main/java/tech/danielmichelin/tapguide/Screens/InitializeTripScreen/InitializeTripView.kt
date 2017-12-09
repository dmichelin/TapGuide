package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import tech.danielmichelin.tapguide.Model.TGBusiness

/**
 * Created by Daniel on 11/26/2017.
 */
interface InitializeTripView{
    fun showBuildingTripDialog()
    fun showErrorDialog(errorText: String?)
    fun navigateToTripOverviewScreen(businesses: Map<String, List<TGBusiness>>, tripName: String?)
}