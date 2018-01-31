package tech.danielmichelin.tapguide.Screens.BusinessOverviewScreen

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_business_overview.*
import kotlinx.android.synthetic.main.content_business_overview.*
import tech.danielmichelin.tapguide.Model.TGBusiness
import tech.danielmichelin.tapguide.R

class BusinessOverviewActivity : AppCompatActivity() {
    companion object {
        val BUSINESS = "BUSINESS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_overview)
        // get the business from the intent extra
        val business = intent.getSerializableExtra(BUSINESS) as TGBusiness

        // set some of the UI stuff here
        title = business.name

        // here's a bit of non-intuitive coding. I'm setting a layout listener to listen for when
        // the imageview is drawn. After the imageview is drawn, load the picture in.
        lateinit var listener: ViewTreeObserver.OnGlobalLayoutListener
        listener = ViewTreeObserver.OnGlobalLayoutListener {
            if (businessImage != null) {
                businessImage.viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
            Picasso.with(this).load(business.imageUrl).resize(businessImage.width, businessImage.height).centerCrop().into(businessImage, object : Callback {
                override fun onSuccess() {
                    // darken the image once the imageview has been loaded
                    businessImage.setColorFilter(Color.argb(80, 0, 0, 0), PorterDuff.Mode.DARKEN)
                }

                override fun onError() {
                    // do nothing
                }

            })
        }
        businessImage.viewTreeObserver.addOnGlobalLayoutListener(listener)
        rating_bar.rating = business.rating.toFloat()
        rating_bar.stepSize = 0.25.toFloat()
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            val uri = Uri.parse("geo:?q=" + (business.location.address1 + " " + business.location.zipCode).replace(" ", "%20"))
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(uri)
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }


}
