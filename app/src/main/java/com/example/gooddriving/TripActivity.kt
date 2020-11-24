package com.example.gooddriving

import android.Manifest
import android.app.ActionBar
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_trip.*

class TripActivity : BasicLayoutActivity(), OnMapReadyCallback {

    companion object {
        const val tripName = "default trip name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_trip)

        val tripNameToDisplay = intent.getStringExtra(tripName)

        var textView: TextView = findViewById(R.id.message1)
        textView.text = tripNameToDisplay

        if (getString(R.string.google_maps_key).isEmpty()) {
            Toast.makeText(this, "Add your own API key in MapWithMarker/app/secure.properties as MAPS_API_KEY=YOUR_API_KEY", Toast.LENGTH_LONG).show()
        }

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.tripMap) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.apply {
            val sydney = LatLng(54.6, 18.29)
            addMarker(
                MarkerOptions()
                    .position(sydney)
                    .title("Marker in Wejherowo")
            )
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}