package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import tech.danielmichelin.tapguide.Enums.Distances
import tech.danielmichelin.tapguide.Enums.PriceLevels

/**
 * Created by Daniel on 11/26/2017.
 */
interface InitializeTripPresenter{
    fun makeTripBuildRequest(distance: Distances, priceLevel: PriceLevels, location: String)
    fun cancelTripBuildRequest()
}