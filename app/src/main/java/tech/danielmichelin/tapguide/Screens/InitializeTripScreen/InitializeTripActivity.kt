package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapButtonGroup
import com.fasterxml.jackson.databind.ObjectMapper
import com.yelp.fusion.client.models.Business
import tech.danielmichelin.tapguide.Enums.Distances
import tech.danielmichelin.tapguide.Enums.PriceLevels
import tech.danielmichelin.tapguide.R
import tech.danielmichelin.tapguide.Screens.Dialogs.BuildingTripDialog
import tech.danielmichelin.tapguide.Screens.TripOverviewScreen.TripOverviewActivity

class InitializeTripActivity : AppCompatActivity(), InitializeTripView {
    lateinit var tripViewPresenter: InitializeTripPresenter
    lateinit var distanceRadio: BootstrapButtonGroup
    lateinit var priceRadio: BootstrapButtonGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripViewPresenter = InitializeTripPresenterImpl(this)
        setContentView(R.layout.activity_initialize_trip)

        // get the zip code from the previous intent
        val zipCodeEt = findViewById<EditText>(R.id.postalCodeEt)
        zipCodeEt.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("ZIP"))

        // get the two radio buttons
        distanceRadio= findViewById(R.id.distanceButtonGroup)
        priceRadio = findViewById(R.id.priceButtonGroup)
        // arm the onclick listener
        val buildTripButton = findViewById<BootstrapButton>(R.id.CreateItineraryButton)
        buildTripButton.setOnClickListener {if(distanceRadio.getSelected() != -1 && priceRadio.getSelected() != -1){
                tripViewPresenter.makeTripBuildRequest(Distances.values()[distanceRadio.getSelected()],PriceLevels.values()[priceRadio.getSelected()],zipCodeEt.text.toString())
            }
        }
    }

    override fun showBuildingTripDialog() {
        BuildingTripDialog().show(fragmentManager,"BuildingTripDialog")
    }

    override fun navigateToNextScreen(businesses: ArrayList<Business>) {
        val bundle = Bundle()
        val mapper = ObjectMapper()
        val stringList = ArrayList<String>()
        for(business in businesses){
            stringList.add(mapper.writeValueAsString(business))
        }
        bundle.putSerializable("businesses",stringList)
        val intent = Intent(this,TripOverviewActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    /**
     * Companion method to get the selected button
     */
    fun BootstrapButtonGroup.getSelected():Int{
        val childCount = childCount
        for (i in 0..childCount){
            var child = getChildAt(i)

            if(child is BootstrapButton && child.isSelected ){
                return i
            }
        }
        return -1
    }

}
