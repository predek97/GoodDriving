package com.example.gooddriving


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.ToggleButton
import com.example.gooddriving.db.Violation
import com.example.gooddriving.tracking.AccelerometerController
import com.example.gooddriving.tracking.LocationController
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DashboardActivity : BasicLayoutActivity(),
    LocationController.SpeedSubscriber, AccelerometerController.AccelerationSubscriber {

    private var tripStarted: Boolean = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tripService: TripService
    protected lateinit var locations : ArrayList<Location>
    protected lateinit var violations : ArrayList<Violation>
    protected var distanceCovered : Array<Double> = arrayOf(0.0)
    protected lateinit var locationController: LocationController
    private lateinit var  accelerometerListener: AccelerometerController


    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    private lateinit var speedTextView: TextView
    private lateinit var lateralGForceTextView: TextView
    private lateinit var linearGForceTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        textMessage = findViewById(R.id.startStopMessage)
        navView.selectedItemId = R.id.navigation_dashboard
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        val toggleButton: ToggleButton = findViewById(R.id.toggleButton)
        toggleButton.setOnClickListener(onStartButtonSelectedListener)
        speedTextView = findViewById(R.id.speedText)
        lateralGForceTextView = findViewById(R.id.lateralGForceText)
        linearGForceTextView = findViewById(R.id.linearGForceText)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        tripService = TripService(this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    }

    protected val onStartButtonSelectedListener = View.OnClickListener {
        if(!tripStarted)
        {
            startTrip()
        }
        else
        {
            stopTrip()
        }
        tripStarted = !tripStarted

    }

    protected fun startTrip() {
        locations = ArrayList()
        violations = ArrayList()
        locationController = LocationController(this.locations, this.distanceCovered)
        locationController.addSubscriber(this)
        accelerometerListener = AccelerometerController(this.locations, this.violations)
        accelerometerListener.addSubscriber(this)
        try {
            val locationRequest = LocationRequest.create()?.apply {
                interval = 2000
                fastestInterval = 1000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient.requestLocationUpdates(locationRequest,
                locationController,
                Looper.getMainLooper())
            sensorManager.registerListener(accelerometerListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        catch(e : SecurityException ) {
            TODO("handle lack of permissions")
        }
    }

    protected fun stopTrip() {
        fusedLocationClient.removeLocationUpdates(locationController)
        sensorManager.unregisterListener(accelerometerListener)
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
            violationList = violations,
            grade = 0.0,
            dateOfTrip = dateFormat.format(dateOfTrip),
            distanceCovered = this.distanceCovered[0],
            timeElapsed = timeElapsed,
            maxSpeed = maxSpeed * 3.6, //convert to km/h from m/s
            avgSpeed = avgSpeed * 3.6,
            tripIdFromDb = 0
        )
    }

    override fun notifyNewSpeed(newSpeed: Double) {
        speedTextView.text = String.format("%.0f", newSpeed)
    }

    override fun notifyNewAccelerations(newAccelerations: Pair<Double, Double>) {
        linearGForceTextView.text = String.format("%.2f", newAccelerations.first)
        lateralGForceTextView.text = String.format("%.2f", newAccelerations.second)
    }

}
