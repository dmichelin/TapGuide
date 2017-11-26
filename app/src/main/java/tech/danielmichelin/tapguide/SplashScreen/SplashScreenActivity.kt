package tech.danielmichelin.tapguide.SplashScreen

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import tech.danielmichelin.tapguide.InitializeTripScreen.InitializeTripActivity
import tech.danielmichelin.tapguide.R

/**
 * Created by Daniel on 11/26/2017.
 */
class SplashScreenActivity : AppCompatActivity(),SplashScreenView{
    lateinit var splashScreenPresenter:SplashScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View.inflate(this, R.layout.activity_splash_screen,null))
        splashScreenPresenter = SplashScreenPresenterImpl(this)
        splashScreenPresenter.createApiAndLogin()
    }

    override fun showProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToNext() {
        var intent = Intent(this,InitializeTripActivity::class.java)
        startActivity(intent);
    }
}