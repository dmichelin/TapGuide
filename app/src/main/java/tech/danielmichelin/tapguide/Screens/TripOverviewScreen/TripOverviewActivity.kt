package tech.danielmichelin.tapguide.Screens.TripOverviewScreen

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
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
import java.net.URI
import java.net.URL


/**
 * Created by Daniel on 11/27/2017.
 */
class TripOverviewActivity: AppCompatActivity(){
    lateinit var listView: ListView
    var loaded = false
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
        listView.adapter = BusinessAdapter(this,businessList.toTypedArray())
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

    inner class BusinessAdapter(context: Context, val businesses: Array<Business>): ArrayAdapter<Business>(context,R.layout.business_list_item,businesses){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val business = getItem(position)
            var v = convertView
            if(convertView==null){
                v = layoutInflater.inflate(R.layout.business_list_item,parent,false)
            }
            val image = v?.findViewById<ImageView>(R.id.businessImage)

            if(!business.imageUrl.equals(""))
                Picasso.with(context).load(business.imageUrl).resize(300,300).centerCrop().into(image)
            val name = v?.findViewById<TextView>(R.id.businessName)
            name?.text = business.name

            v?.setOnClickListener({
                val uri = Uri.parse("geo:?q="+(business.location.address1+" "+business.location.zipCode).replace(" ", "%20"))
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(uri)
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            })

            return v
        }


    }
}