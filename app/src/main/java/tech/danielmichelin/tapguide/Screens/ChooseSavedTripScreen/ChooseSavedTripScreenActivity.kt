package tech.danielmichelin.tapguide.Screens.ChooseSavedTripScreen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import io.paperdb.Paper
import tech.danielmichelin.tapguide.R
import tech.danielmichelin.tapguide.Screens.TripOverviewScreen.TripOverviewActivity

/**
 * Created by Daniel on 12/3/2017.
 */
class ChooseSavedTripScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_saved_trip)

        val tripView = findViewById<ListView>(R.id.saved_trips_listView)
        Paper.init(this)
        val allKeys = Paper.book(TripOverviewActivity.tripBook).allKeys.toTypedArray()
        tripView.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allKeys)

    }
}