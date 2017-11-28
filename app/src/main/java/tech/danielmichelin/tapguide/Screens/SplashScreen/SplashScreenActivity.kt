package tech.danielmichelin.tapguide.Screens.SplashScreen

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import tech.danielmichelin.tapguide.Helpers.LocationHelper
import tech.danielmichelin.tapguide.Screens.InitializeTripScreen.InitializeTripActivity
import tech.danielmichelin.tapguide.R
import java.util.*
import tech.danielmichelin.tapguide.Helpers.PermissionHelper


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
        var locList = geoCoder.getFromLocation(loc!!.latitude,loc.longitude,1)

        // then navigate to the next activity
        var intent = Intent(this,InitializeTripActivity::class.java)
        intent.putExtra("ZIP",locList.get(0).postalCode)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent);
    }
}