package tech.danielmichelin.tapguide.Screens.TripOverviewScreen

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageButton
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
import tech.danielmichelin.tapguide.Model.TGTimelineRow
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

    companion object {
        val TRIP_BOOK = "SAVED_TRIPS"
        val BREAKFAST_OPTIONS = "BREAKFAST_OPTIONS"
        val ACTIVITY_OPTIONS = "ACTIVITY_OPTIONS"
        val NON_BREAKFAST_OPTIONS = "NON_BREAKFAST_OPTIONS"
        val NIGHTLIFE_OPTIONS = "NIGHTLIFE_OPTIONS"
        val BUSINESSES = "MAIN_BUSINESSES"
        val TRIP_NAME = "TRIP_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize our database
        Paper.init(this);


        setContentView(View.inflate(this, R.layout.activity_trip_overview,null))
        // set up the presenter
        tripOverviewPresenter = TripOverviewPresenterImpl(this)

        // implement timelineview
        listView = findViewById<ListView>(R.id.activity_list)
        val extras = intent.extras



        if (extras.containsKey(BUSINESSES)) {
            val businesses = extras.get(BUSINESSES) as Array<TGBusiness>
            // set the trip saved button
            val tripSaveButton = findViewById<BootstrapButton>(R.id.saveTripButton)

            saveTripButton.setOnClickListener { saveTrip(intent.extras.getString(TRIP_NAME, "New Trip"), businesses.asList()) }


            val timelineRowsList = ArrayList<TimelineRow>()
            // if breakfast options is contained, the rest are

            if (extras.containsKey(BREAKFAST_OPTIONS)) {
                val breakfast = extras.get(BREAKFAST_OPTIONS) as Array<TGBusiness>
                val nightlife = extras.get(NIGHTLIFE_OPTIONS) as Array<TGBusiness>
                val activities = extras.get(ACTIVITY_OPTIONS) as Array<TGBusiness>
                val food = extras.get(NON_BREAKFAST_OPTIONS) as Array<TGBusiness>
                val combinedBusinesses = arrayOf(breakfast, activities, food, activities, activities, food, nightlife)
                for (i in 0..businesses.size - 1) {
                    val row = TGTimelineRow(i)
                    val rowBus = combinedBusinesses.first { bArr -> bArr.any { b -> b.eventType.equals(businesses[i]) } }
                    row.businesses = rowBus.plus(businesses[i])
                    row.position = rowBus.size
                    timelineRowsList.add(row)
                }
            } else {
                for (i in 0..businesses.size - 1) {
                    val row = TGTimelineRow(i)
                    row.businesses = arrayOf(businesses[i])
                    row.position = 0
                    timelineRowsList.add(row)
                }
            }


            listView.isVerticalScrollBarEnabled = false
            listView.viewTreeObserver.addOnGlobalLayoutListener({ initSpruce() })
            listView.adapter = TGTimelineView(timelineRowsList)

            // set the onclick adapter for the floating action button
            tripOptionsFab = findViewById(R.id.fab)
            tripOptionsFab.setOnClickListener {
                startIntentFromUri(tripOverviewPresenter.generateLocationUri(getLastLocationPair(), businesses.asList()))
            }

            listView.setOnScrollListener(object : AbsListView.OnScrollListener {
                override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                    if (firstVisibleItem < 1) {
                        tripOptionsFab.show(true)
                    } else {
                        tripOptionsFab.hide(true)
                    }
                }

                override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                    // do nothing
                }
            })
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

    private fun saveTrip(tripName: String, businesses: List<TGBusiness>) {
        Paper.book(TRIP_BOOK).write(tripName, businesses)
        Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show()
    }

    inner class TGTimelineView(objects: ArrayList<TimelineRow>) : TimelineViewAdapter(this, 0, objects, false) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val row = getItem(position) as TGTimelineRow
            val business = row.businesses.get(row.position)
            row.setImageSize(150)
            row.openAt = business.hours?.get(0)?.open?.get(0)?.start
            row.closedAt = business.hours?.get(0)?.open?.get(0)?.end
            val i = position
            if (i > 0) {
                val rowBefore = getItem(position - 1) as TGTimelineRow
                val businessBefore = rowBefore.businesses.get(rowBefore.position)
                val floatArray = FloatArray(10)
                Location.distanceBetween(business.coordinates.latitude,
                        business.coordinates.longitude, businessBefore.coordinates.latitude,
                        businessBefore.coordinates.longitude, floatArray)
                row.distanceInMeters = floatArray.get(0).toDouble();

            }

            row.setBelowLineColor(Color.argb(255, 10, 100, 255));
            // To set row Below Line Size in dp (optional)
            row.setBelowLineSize(20);
            row.setOnClickListener {
                val uri = Uri.parse("geo:?q=" + (business.location.address1 + " " + business.location.zipCode).replace(" ", "%20"))
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(uri)
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
            row.title = row.businesses.get(row.businesses.lastIndex).eventType
            row.description = business.name
            row.image = business.imageUrl

            val view = super.getView(position, convertView, parent)
            val leftButton = view.findViewById<ImageButton>(R.id.left_button)
            if (row.position <= 0) {
                val color = resources.getColor(android.R.color.darker_gray)
                ViewCompat.setBackgroundTintList(leftButton, ColorStateList.valueOf(color))
                ViewCompat.setElevation(leftButton, 0F)
            } else {
                val color = resources.getColor(R.color.bootstrap_brand_primary)
                ViewCompat.setBackgroundTintList(leftButton, ColorStateList.valueOf(color))
                ViewCompat.setElevation(leftButton, 10F)
            }
            leftButton.setOnClickListener({
                if (row.position > 0) {
                    row.position--
                    notifyDataSetChanged()
                }
            })
            val rightButton = view.findViewById<ImageButton>(R.id.right_button)
            if (row.position >= row.businesses.size - 1) {
                //rightButton.visibility = View.INVISIBLE
                val color = resources.getColor(android.R.color.darker_gray)
                ViewCompat.setBackgroundTintList(rightButton, ColorStateList.valueOf(color))
                ViewCompat.setElevation(rightButton, 0F)

            } else {
                rightButton.visibility = View.VISIBLE
                rightButton.isActivated = true
                val color = resources.getColor(R.color.bootstrap_brand_primary)
                ViewCompat.setBackgroundTintList(rightButton, ColorStateList.valueOf(color))
                ViewCompat.setElevation(rightButton, 10F)
            }
            rightButton.setOnClickListener({
                if (row.position < row.businesses.size - 1) {
                    row.position++
                    notifyDataSetChanged()
                }
            })

            return view
        }
    }

}