package com.example.gooddriving

import android.R.attr.path
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.gooddriving.db.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class TripActivity : BasicLayoutActivity(), OnMapReadyCallback {

    companion object {
        const val tripId = "0"
    }
    var db: TripRoomDatabase? = null
    var tripWithPosDao : TripWithPositionsDao? = null
    var tripWithVioDao : TripWithViolationsDao? = null
    var tripWithViolations : TripWithViolations? = null
    var tripWithPositions : TripWithPositions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_trip)

        var tripIdToDisplay = intent.getStringExtra(tripId).toLong()


        this.title = "Trip #$tripIdToDisplay"


        db = TripRoomDatabase.getDatabase(this)
        var task = GlobalScope.async {
            tripWithPosDao = db!!.tripWithPositionsDao()
            tripWithVioDao = db!!.tripWithViolationsDao()
            tripWithPositions = tripWithPosDao!!.getSingleTripWithPositions(tripIdToDisplay)
            tripWithViolations = tripWithVioDao!!.getSingleTripWithViolations(tripIdToDisplay)
        }
        runBlocking {
            task.await()
        }

        findViewById<TextView>(R.id.tripRating).text = tripWithPositions!!.trip.grade.toString()
        findViewById<TextView>(R.id.tripAvgSpeed).text = calculateAvgSpeed(tripWithViolations!!).toString() + " km/h"
        findViewById<TextView>(R.id.tripMaxSpeed).text = getMaxSpeed(tripWithViolations!!).toString() + " km/h"


        var textView: TextView = findViewById(R.id.message1)
        textView.text = tripIdToDisplay.toString()

        if (getString(R.string.google_maps_key).isEmpty()) {
            Toast.makeText(this, "Add your own API key in MapWithMarker/app/secure.properties as MAPS_API_KEY=YOUR_API_KEY", Toast.LENGTH_LONG).show()
        }

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.tripMap) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }

    fun calculateAvgSpeed(trip: TripWithViolations): Double {
        var sum : Double = 0.0
        for (vio in trip.violations){
            sum += vio.speed
        }
        return sum/trip.violations.size
    }

    fun getMaxSpeed(trip: TripWithViolations): Double{
        var maxSpeed: Double = 0.0
        for (vio in trip.violations) {
            if (vio.speed > maxSpeed){
                maxSpeed = vio.speed
            }
        }
        return maxSpeed
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        /*
        googleMap?.apply {
            val sydney = LatLng(54.6, 18.29)
            addMarker(
                MarkerOptions()
                    .position(sydney)
                    .title("Marker in Wejherowo")
            )
        }
        */
        var polylineOptions = PolylineOptions()
        polylineOptions.clickable(true)

        for (pos in tripWithPositions!!.positions){
            polylineOptions.add(LatLng(pos.latitude, pos.longitude))
        }
        googleMap?.addPolyline(polylineOptions)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(tripWithPositions!!.positions[0].latitude, tripWithPositions!!.positions[0].longitude), 12f))
        googleMap?.apply {
            val sydney = LatLng(tripWithPositions!!.positions[0].latitude, tripWithPositions!!.positions[0].longitude)
            addMarker(
                MarkerOptions()
                    .position(sydney)
                    .title("Trip start")
            )
        }
        googleMap?.apply {
            val sydney = LatLng(tripWithPositions!!.positions.last().latitude, tripWithPositions!!.positions.last().longitude)
            addMarker(
                MarkerOptions()
                    .position(sydney)
                    .title("Trip end")
            )
        }

        for ((i, vio) in tripWithViolations!!.violations.withIndex()){
            googleMap?.apply {
                addMarker(
                    MarkerOptions()
                        .position(LatLng(vio.latitude, vio.longitude))
                        .title("Violation " + (i+1).toString())
                )
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}