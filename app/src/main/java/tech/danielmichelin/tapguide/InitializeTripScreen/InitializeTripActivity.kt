package tech.danielmichelin.tapguide.InitializeTripScreen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import tech.danielmichelin.tapguide.R

class InitializeTripActivity : AppCompatActivity(), InitializeTripView {
    lateinit var tripViewPresenter : InitializeTripPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialize_trip)
    }

}
