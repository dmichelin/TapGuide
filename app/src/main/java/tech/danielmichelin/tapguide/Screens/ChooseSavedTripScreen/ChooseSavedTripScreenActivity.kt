package tech.danielmichelin.tapguide.Screens.ChooseSavedTripScreen

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.beardedhen.androidbootstrap.BootstrapButton
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import tech.danielmichelin.tapguide.Model.TGBusiness
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
        val allKeys = Paper.book(TripOverviewActivity.TRIP_BOOK).allKeys.toTypedArray()
        val savedTripsAdapter = savedTripsAdapter()
        savedTripsAdapter.addAll(allKeys.asList())
        tripView.adapter = savedTripsAdapter
    }

    inner class savedTripsAdapter : ArrayAdapter<String>(this, R.layout.item_saved_trip) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            if (convertView != null) {
                view = convertView
            } else {
                view = layoutInflater.inflate(R.layout.item_saved_trip, null)
            }
            val tripNameTv = view.findViewById<TextView>(R.id.trip_id_tv)
            tripNameTv.text = getItem(position)

            val tripImage = view.findViewById<ImageView>(R.id.trip_image_imageview)
            val trips = Paper.book(TripOverviewActivity.TRIP_BOOK).read<List<TGBusiness>>(getItem(position))
            val imageString = trips[0].imageUrl
            Picasso.with(context).load(imageString).centerCrop().resize(300, 300).into(tripImage)
            tripImage.setOnClickListener({
                val intent = Intent(context, TripOverviewActivity::class.java)
                intent.putExtra("businesses", trips.toTypedArray())
                intent.putExtra("tripName", getItem(position))
                startActivity(intent)
            })
            view.findViewById<BootstrapButton>(R.id.open_trip_button).setOnClickListener {
                val intent = Intent(context, TripOverviewActivity::class.java)
                intent.putExtra("businesses", trips.toTypedArray())
                intent.putExtra("tripName", getItem(position))
                startActivity(intent)
            }
            val deleteBtn = view.findViewById<BootstrapButton>(R.id.delete_trip_button)
            deleteBtn.setOnClickListener {
                Paper.book(TripOverviewActivity.TRIP_BOOK).delete(getItem(position))
                remove(getItem(position))
                notifyDataSetChanged()
            }
            return view
        }
    }
}