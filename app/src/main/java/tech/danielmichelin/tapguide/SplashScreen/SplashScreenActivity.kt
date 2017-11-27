package tech.danielmichelin.tapguide.SplashScreen

import android.Manifest
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import tech.danielmichelin.tapguide.Helpers.LocationHelper
import tech.danielmichelin.tapguide.InitializeTripScreen.InitializeTripActivity
import tech.danielmichelin.tapguide.R
import java.util.*
import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_CONTACTS
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
        //
        when(requestCode){
            PermissionHelper.FINE_LOCATION_PERMISSION -> splashScreenPresenter.createApiAndLogin()
        }
    }
    override fun navigateToNext() {
        val loc = LocationHelper.getLastBestLocation(this)
        val geoCoder = Geocoder(this, Locale.getDefault())
        var locList = geoCoder.getFromLocation(loc!!.latitude,loc.longitude,1)
        var intent = Intent(this,InitializeTripActivity::class.java)
        intent.putExtra("ZIP",locList.get(0).postalCode)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent);
    }
}