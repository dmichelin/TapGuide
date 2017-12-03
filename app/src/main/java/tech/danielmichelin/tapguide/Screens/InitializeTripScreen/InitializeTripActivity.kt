package tech.danielmichelin.tapguide.Screens.InitializeTripScreen

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import com.beardedhen.androidbootstrap.BootstrapButton
import com.beardedhen.androidbootstrap.BootstrapButtonGroup
import tech.danielmichelin.tapguide.Enums.Distances
import tech.danielmichelin.tapguide.Enums.PriceLevels
import tech.danielmichelin.tapguide.Model.TGBusiness
import tech.danielmichelin.tapguide.R
import tech.danielmichelin.tapguide.Screens.Dialogs.BuildingTripDialog
import tech.danielmichelin.tapguide.Screens.TripOverviewScreen.TripOverviewActivity

class InitializeTripActivity : AppCompatActivity(), InitializeTripView, BuildingTripDialog.BuildingTripDialogCancelListener {
    lateinit var tripViewPresenter: InitializeTripPresenter
    lateinit var distanceRadio: BootstrapButtonGroup
    lateinit var priceRadio: BootstrapButtonGroup
    lateinit var loadingDialog: BuildingTripDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = BuildingTripDialog()
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
            }else{
                Toast.makeText(this,"Please select a price level and distance",Toast.LENGTH_LONG).show()
            }
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

    override fun navigateToNextScreen(businesses: MutableList<TGBusiness>) {
        loadingDialog.dismiss()
        val intent = Intent(this,TripOverviewActivity::class.java)
        intent.putExtra("businesses",businesses.toTypedArray())
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
