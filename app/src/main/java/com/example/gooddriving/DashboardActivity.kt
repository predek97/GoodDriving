package com.example.gooddriving


import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DashboardActivity : BasicLayoutActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tripService: TripService
    protected lateinit var locations : ArrayList<Location>;
    protected var distanceCovered : Array<Double> = arrayOf(0.0)

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.selectedItemId = R.id.navigation_dashboard
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        val startButton: Button = findViewById(R.id.startButton)
        startButton.setOnClickListener(onStartButtonSelectedListener)
        val stopButton: Button = findViewById(R.id.stopButton)
        stopButton.setOnClickListener(onStopButtonSelectedListener)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        tripService = TripService(this)
    }

    protected val onStartButtonSelectedListener = View.OnClickListener {
        locations = ArrayList();
        locationCallback = MyLocationCallback(this.locations, this.distanceCovered)
        try {
            val locationRequest = LocationRequest.create()?.apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper())
        }
        catch(e : SecurityException ) {
            //TO-DO handle lack of permissions
        }

    }

    protected val onStopButtonSelectedListener = View.OnClickListener {
        tripService.save(generateTrip())
    }


    protected fun generateTrip(): TripModel {
        val dateOfTrip = Date(locations.first().time)
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val timeElapsed = (locations.last().time - locations.first().time)
        var maxSpeed = 0.0
        var avgSpeed = 0.0
        for(location in locations) {
            maxSpeed = if(maxSpeed < location.speed) location.speed.toDouble() else maxSpeed
            avgSpeed += location.speed
        }
        avgSpeed /= locations.size
        return TripModel(
            locationList = locations,
            violationList = ArrayList(),
            grade = 0.0,
            dateOfTrip = dateFormat.format(dateOfTrip),
            distanceCovered = this.distanceCovered[0],
            timeElapsed = timeElapsed,
            maxSpeed = maxSpeed * 3.6, //convert to km/h from m/s
            avgSpeed = avgSpeed * 3.6,
            tripIdFromDb = 0
        )
    }

    protected lateinit var locationCallback: LocationCallback

    class MyLocationCallback constructor(var locations : ArrayList<Location>, var distanceCovered : Array<Double>) : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationResult ?: return
            for (location in locationResult.locations){
                calculateSpeed(location)
                this.locations.add(location)
            }
        }
        fun calculateSpeed(currentLocation: Location) {
            if(locations.size > 0) {
                val lastLocation = locations.last()
                val distance = lastLocation.distanceTo(currentLocation)
                distanceCovered[0] = distanceCovered[0]?.plus(distance)
                //divide by 1000 to get time in seconds
                val timeDifference = (currentLocation.time - lastLocation.time)/1000.0
                val speed = distance/timeDifference
                currentLocation.speed = speed.toFloat();
            }
        }
    }


}
