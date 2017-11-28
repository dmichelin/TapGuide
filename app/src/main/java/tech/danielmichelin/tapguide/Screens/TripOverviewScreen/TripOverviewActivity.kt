package tech.danielmichelin.tapguide.Screens.TripOverviewScreen

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ArrayAdapter
import android.widget.ListView
import com.fasterxml.jackson.databind.ObjectMapper
import com.willowtreeapps.spruce.Spruce
import com.willowtreeapps.spruce.animation.DefaultAnimations
import com.willowtreeapps.spruce.sort.DefaultSort
import com.yelp.fusion.client.models.Business
import kotlinx.android.synthetic.main.activity_trip_overview.view.*
import tech.danielmichelin.tapguide.R


/**
 * Created by Daniel on 11/27/2017.
 */
class TripOverviewActivity: AppCompatActivity(){
    lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View.inflate(this, R.layout.activity_trip_overview,null))
        listView = findViewById<ListView>(R.id.activity_list)
        val businesses = intent.extras.get("businesses") as List<String>
        val businessList = ArrayList<Business>()
        val mapper = ObjectMapper()
        for(business in businesses){
            businessList.add(mapper.readValue(business,Business::class.java))
        }
        listView.viewTreeObserver.addOnGlobalLayoutListener({initSpruce()})
        listView.adapter = ArrayAdapter<Business>(this,android.R.layout.simple_list_item_1,businessList as List<Business>)
    }
    fun initSpruce(){
        Spruce.SpruceBuilder(listView).sortWith(DefaultSort(50L))
                .animateWith(DefaultAnimations.fadeInAnimator(listView,800),
                        ObjectAnimator.ofFloat(listView, "translationX", -listView.getWidth().toFloat(), 0f).setDuration(800))
                .start()
    }

    inner class BusinessAdapter(context: Context, val businesses: Array<Business>): ArrayAdapter<Business>(context,R.layout.business_list_item,businesses){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val business = getItem(position)
            var v = convertView
            if(convertView==null){
                v = layoutInflater.inflate(R.layout.business_list_item,parent)
            }

            TODO("Implement viewadapter")
            return v
        }
    }
}