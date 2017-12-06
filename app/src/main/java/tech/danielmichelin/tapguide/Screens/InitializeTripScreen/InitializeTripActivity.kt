package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapButtonGroup
import io.paperdb.Paper
import tech.danielmichelin.tapguide.Enums.Distances
import tech.danielmichelin.tapguide.Enums.PriceLevels
import tech.danielmichelin.tapguide.Model.TGBusiness
import tech.danielmichelin.tapguide.R
import tech.danielmichelin.tapguide.Screens.ChooseSavedTripScreen.ChooseSavedTripScreenActivity
import tech.danielmichelin.tapguide.Screens.Dialogs.BuildingTripDialog
import tech.danielmichelin.tapguide.Screens.TripOverviewScreen.TripOverviewActivity

class InitializeTripActivity : AppCompatActivity(), InitializeTripView, BuildingTripDialog.BuildingTripDialogCancelListener {
    lateinit var tripViewPresenter: InitializeTripPresenter
    lateinit var distanceRadio: BootstrapButtonGroup
    lateinit var priceRadio: BootstrapButtonGroup
    lateinit var loadingDialog: BuildingTripDialog
    lateinit var openSavedTripsBtn: BootstrapButton

    override fun onCreate(savedInstanceState: Bundle?) {
        Paper.init(this)
        super.onCreate(savedInstanceState)
        loadingDialog = BuildingTripDialog()
        tripViewPresenter = InitializeTripPresenterImpl(this)
        setContentView(R.layout.activity_initialize_trip)

        // get the zip code from the previous intent
        val zipCodeEt = findViewById<EditText>(R.id.postalCodeEt)
        zipCodeEt.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("address"))

        // get the two radio buttons
        distanceRadio= findViewById(R.id.distanceButtonGroup)
        priceRadio = findViewById(R.id.priceButtonGroup)

        // arm the onclick listener to build the trip
        val buildTripButton = findViewById<BootstrapButton>(R.id.CreateItineraryButton)
        buildTripButton.setOnClickListener {if(distanceRadio.getSelected() != -1 && priceRadio.getSelected() != -1){
                tripViewPresenter.makeTripBuildRequest(Distances.values()[distanceRadio.getSelected()],PriceLevels.values()[priceRadio.getSelected()],zipCodeEt.text.toString())
            }else{
                Toast.makeText(this,"Please select a price level and distance",Toast.LENGTH_LONG).show()
            }
        }

        // build the onclick listener for the saved trips button
        openSavedTripsBtn = findViewById<BootstrapButton>(R.id.load_trip_btn)
        openSavedTripsBtn.setOnClickListener({
            val intent = Intent(this, ChooseSavedTripScreenActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onResume() {
        super.onResume()
        if (Paper.book(TripOverviewActivity.tripBook).allKeys.size > 0) {
            openSavedTripsBtn.visibility = View.VISIBLE
        }
    }

    override fun showBuildingTripDialog() {
        loadingDialog.listener = this
        loadingDialog.retainInstance = true
        loadingDialog.show(fragmentManager,"BuildingTripDialog")
    }

    override fun showErrorDialog(errorText: String?) {
        loadingDialog.dismiss()
        AlertDialog.Builder(this).setMessage(errorText).show()
    }

    override fun navigateToTripOverviewScreen(businesses: MutableList<TGBusiness>, tripName: String?) {
        loadingDialog.dismiss()
        val intent = Intent(this,TripOverviewActivity::class.java)
        intent.putExtra("businesses",businesses.toTypedArray())
        intent.putExtra("tripName", tripName)
        startActivity(intent)
    }

    override fun onDialogCancel() {
        tripViewPresenter.cancelTripBuildRequest()
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
