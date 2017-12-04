package tech.danielmichelin.tapguide.Screens.TripOverviewScreen

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.beardedhen.androidbootstrap.BootstrapButton
import com.github.clans.fab.FloatingActionButton
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_trip_overview.*
import org.qap.ctimelineview.TimelineRow
import org.qap.ctimelineview.TimelineViewAdapter
import tech.danielmichelin.tapguide.Helpers.LocationHelper
import tech.danielmichelin.tapguide.Model.TGBusiness
import tech.danielmichelin.tapguide.R


/**
 * Created by Daniel on 11/27/2017.
 */
class TripOverviewActivity : AppCompatActivity(), TripOverviewView {
    lateinit var listView: ListView
    lateinit var tripOverviewPresenter: TripOverviewPresenter
    // currently only starts a multi part-navigation
    lateinit var tripOptionsFab: FloatingActionButton
    var loaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize our database
        Paper.init(this);


        setContentView(View.inflate(this, R.layout.activity_trip_overview,null))
        // set up the presenter
        tripOverviewPresenter = TripOverviewPresenterImpl(this)

        // implement timelineview
        listView = findViewById<ListView>(R.id.activity_list)
        val businesses = intent.extras.get("businesses") as Array<TGBusiness>

        // set the trip saved button
        val tripSaveButton = findViewById<BootstrapButton>(R.id.saveTripButton)

        // TODO: Edit this so that duplicate trips cannot be saved
        saveTripButton.setOnClickListener { saveTrip(intent.extras.getString("tripName", "New Trip"), businesses.asList()) }

        //saveTrip("1",businesses.asList())
        //getTrip("1")
        val timelineRowsList = ArrayList<TimelineRow>()
        for(i in 0..businesses.size-1){
            val business = businesses.get(i)
            val row = TimelineRow(i)
            row.setImageSize(150);
            row.setBelowLineColor(Color.argb(255, 10, 100, 255));
// To set row Below Line Size in dp (optional)
            row.setBelowLineSize(20);
            row.setOnClickListener {
                val uri = Uri.parse("geo:?q="+(business.location.address1+" "+business.location.zipCode).replace(" ", "%20"))
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(uri)
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } }
            row.title= business.eventType
            row.description = business.name
            row.image = business.imageUrl

            timelineRowsList.add(row)
        }
        listView.isVerticalScrollBarEnabled= false
        listView.viewTreeObserver.addOnGlobalLayoutListener({initSpruce()})
        listView.adapter = TimelineViewAdapter(this,0,timelineRowsList,false)
        Log.d("Test", "Test")

        // set the onclick adapter for the floating action button
        tripOptionsFab = findViewById(R.id.fab)
        tripOptionsFab.setOnClickListener {
            startIntentFromUri(tripOverviewPresenter.generateLocationUri(getLastLocationPair(), businesses.asList()))
        }

    }
    fun initSpruce(){
        // make sure to only do this once
        if(!loaded){
            val animator = ObjectAnimator.ofFloat(listView, "translationX", -listView.width.toFloat(), 0f).setDuration(800)
            animator.addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator) {
                }
                override fun onAnimationEnd(animation: Animator) {
                    listView.isVerticalScrollBarEnabled = true
                }
                override fun onAnimationCancel(animation: Animator) {
                }
                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            animator.start()
            loaded = true
            Toast.makeText(this,"Tap a destination to navigate there",Toast.LENGTH_LONG).show()
        }
    }

    private fun getLastLocationPair(): Pair<Float?, Float?> {
        val loc = LocationHelper.getLastBestLocation(this)
        return Pair(loc?.latitude?.toFloat(), loc?.longitude?.toFloat())
    }

    private fun startIntentFromUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(uri)
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    companion object {
        val tripBook = "SAVED_TRIPS"
    }

    private fun saveTrip(tripName: String, businesses: List<TGBusiness>) {
        Paper.book(tripBook).write(tripName, businesses)
    }

    private fun getTrip(tripName: String): List<TGBusiness> {
        return Paper.book(tripBook).read(tripName, ArrayList())
    }
}