package com.example.gooddriving


import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView


class DashboardActivity : BasicLayoutActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    protected lateinit var locations : ArrayList<Location>;

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
    }

    protected val onStartButtonSelectedListener = View.OnClickListener {
        locations = ArrayList();
        locationCallback = MyLocationCallback(this.locations, this)
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
        var locations = this.locations
        Toast.makeText(this, locations.size, Toast.LENGTH_LONG).show()
    }

    protected lateinit var locationCallback: LocationCallback

    class MyLocationCallback constructor(var locations : ArrayList<Location>, var context : Context) : LocationCallback() {
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
                //divide by 1000 to get time in seconds
                val timeDifference = (currentLocation.time - lastLocation.time)/1000.0
                val speed = distance/timeDifference
                currentLocation.speed = speed.toFloat();
            }
        }
    }


}
