package tech.danielmichelin.tapguide.Screens.SplashScreen

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import tech.danielmichelin.tapguide.Helpers.LocationHelper
import tech.danielmichelin.tapguide.Helpers.PermissionHelper
import tech.danielmichelin.tapguide.R
import tech.danielmichelin.tapguide.Screens.InitializeTripScreen.InitializeTripActivity
import java.util.*


/**
 * Created by Daniel on 11/26/2017.
 */
class SplashScreenActivity : AppCompatActivity(),SplashScreenView{
    lateinit var splashScreenPresenter:SplashScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View.inflate(this, R.layout.activity_splash_screen,null))
        splashScreenPresenter = SplashScreenPresenterImpl(this)
        PermissionHelper.checkAllPermissions(this)
    }

    override fun showProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // when we have requested permissions, move on to testing our API and logging in
        when(requestCode){
            PermissionHelper.FINE_LOCATION_PERMISSION -> splashScreenPresenter.createApiAndLogin()
        }
    }
    override fun navigateToNext() {
        // get the user's current location
        val loc = LocationHelper.getLastBestLocation(this)
        val geoCoder = Geocoder(this, Locale.getDefault())
        var intent = Intent(this, InitializeTripActivity::class.java)
        if (loc != null) {
            var locList = geoCoder.getFromLocation(loc!!.latitude, loc.longitude, 1)
            val address = locList.get(0)
            val addressString = address.getAddressLine(0)
            intent.putExtra("address", addressString)
        } else {
            intent.putExtra("address", "")
        }
        // then navigate to the next activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent);
    }
}