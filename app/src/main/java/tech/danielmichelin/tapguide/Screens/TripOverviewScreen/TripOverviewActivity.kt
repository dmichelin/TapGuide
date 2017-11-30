package tech.danielmichelin.tapguide.Screens.TripOverviewScreen

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.picasso.Picasso
import com.willowtreeapps.spruce.Spruce
import com.willowtreeapps.spruce.animation.DefaultAnimations
import com.willowtreeapps.spruce.sort.DefaultSort
import com.yelp.fusion.client.models.Business
import tech.danielmichelin.tapguide.R
import tech.danielmichelin.tapguide.Enums.ActivityType
import tech.danielmichelin.tapguide.Model.TGBusiness
import android.support.v4.view.GestureDetectorCompat
import android.view.MotionEvent
import android.text.method.Touch.onTouchEvent
import android.util.Log


/**
 * Created by Daniel on 11/27/2017.
 */
class TripOverviewActivity: AppCompatActivity() {
    lateinit var listView: ListView
    var loaded = false
    lateinit var businessToType : MutableList<TGBusiness>
    val DEBUG_TAG = "Gestures"
    var gestureDetector: GestureDetectorCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View.inflate(this, R.layout.activity_trip_overview,null))
        listView = findViewById<ListView>(R.id.activity_list)
        val businesses = intent.extras.get("businesses")
        listView.viewTreeObserver.addOnGlobalLayoutListener({initSpruce()})

        listView.adapter = BusinessAdapter(this,businesses as Array<TGBusiness>)
        Log.d("Test", "Test")

        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener

    }
    fun initSpruce(){
        // make sure to only do this once
        if(!loaded){
            Spruce.SpruceBuilder(listView).sortWith(DefaultSort(50L))
                    .animateWith(DefaultAnimations.fadeInAnimator(listView,800),
                            ObjectAnimator.ofFloat(listView, "translationX", -listView.getWidth().toFloat(), 0f).setDuration(800))
                    .start()
            loaded = true
            Toast.makeText(this,"Tap a destination to navigate there",Toast.LENGTH_LONG).show()
        }

    }

    inner class BusinessAdapter(context: Context, val businesses: Array<TGBusiness>): ArrayAdapter<TGBusiness>(context,R.layout.business_list_item,businesses) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val business = getItem(position)
            var v = convertView
            if (convertView == null) {
                v = layoutInflater.inflate(R.layout.business_list_item, parent, false)
            }
            val image = v?.findViewById<ImageView>(R.id.businessImage)



            if (!business.imageUrl.equals(""))
                Picasso.with(context).load(business.imageUrl).resize(300, 300).centerCrop().into(image)
            val name = v?.findViewById<TextView>(R.id.businessName)
            name?.text = business.name
            val activityType = v?.findViewById<TextView>(R.id.description)
            activityType?.text = business.eventType


            return v
        }

    }

    /**
     * Detects left and right swipes across a view.
     */
    inner class OnSwipeTouchListener(context: Context) : View.OnTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, GestureListener())
        }

        fun onSwipeLeft() {}

        fun onSwipeRight() {}

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(event)
        }

        private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_DISTANCE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                val distanceX = e2.x - e1.x
                val distanceY = e2.y - e1.y
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight()
                    else
                        onSwipeLeft()
                    return true
                }
                return false
            }

        }
    }
}