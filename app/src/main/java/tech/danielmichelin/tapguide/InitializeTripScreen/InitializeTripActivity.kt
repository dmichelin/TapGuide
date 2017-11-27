package tech.danielmichelin.tapguide.InitializeTripScreen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.widget.EditText
import tech.danielmichelin.tapguide.R

class InitializeTripActivity : AppCompatActivity(), InitializeTripView {
    lateinit var tripViewPresenter: InitializeTripPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialize_trip)

        // get the zip code from the previous intent
        val zipCodeEt = findViewById<EditText>(R.id.postalCodeEt)
        zipCodeEt.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("ZIP"))
    }
}
