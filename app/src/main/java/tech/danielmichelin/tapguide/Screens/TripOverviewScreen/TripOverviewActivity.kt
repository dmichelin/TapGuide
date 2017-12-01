package tech.danielmichelin.tapguide.Screens.TripOverviewScreen

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import com.willowtreeapps.spruce.Spruce
import com.willowtreeapps.spruce.animation.DefaultAnimations
import com.willowtreeapps.spruce.sort.DefaultSort
import tech.danielmichelin.tapguide.R
import tech.danielmichelin.tapguide.Model.TGBusiness
import android.util.Log
import org.qap.ctimelineview.TimelineRow
import org.qap.ctimelineview.TimelineViewAdapter


/**
 * Created by Daniel on 11/27/2017.
 */
class TripOverviewActivity: AppCompatActivity() {
    lateinit var listView: ListView
    var loaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View.inflate(this, R.layout.activity_trip_overview,null))
        listView = findViewById<ListView>(R.id.activity_list)

        val businesses = intent.extras.get("businesses") as Array<TGBusiness>

        // implement timelineview
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

        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener

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

    inner class BusinessAdapter(context: Context, val businesses: Array<TGBusiness>): ArrayAdapter<TGBusiness>(context,R.layout.business_list_item,businesses) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val business = getItem(position)
            var v = convertView
            if (convertView == null) {
                v = layoutInflater.inflate(R.layout.business_list_item, parent, false)
            }
            val image = v?.findViewById<ImageView>(R.id.businessImage)




            val name = v?.findViewById<TextView>(R.id.businessName)
            name?.text = business.name
            val activityType = v?.findViewById<TextView>(R.id.description)
            activityType?.text = business.eventType

            return v
        }

    }
}