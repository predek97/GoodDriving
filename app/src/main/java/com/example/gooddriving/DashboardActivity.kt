package com.example.gooddriving


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import com.example.gooddriving.db.Violation
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KFunction1


class DashboardActivity : BasicLayoutActivity() {

    private var tripStarted: Boolean = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tripService: TripService
    protected lateinit var locations : ArrayList<Location>
    protected lateinit var violations : ArrayList<Violation>
    protected var distanceCovered : Array<Double> = arrayOf(0.0)
    protected var violationContinous = false


    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private lateinit var textView : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.selectedItemId = R.id.navigation_dashboard
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        val toggleButton: ToggleButton = findViewById(R.id.toggleButton)
        toggleButton.setOnClickListener(onStartButtonSelectedListener)
        textView = findViewById(R.id.textView)

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
        locationCallback = MyLocationCallback(this.locations, this.distanceCovered)
        accelerometerListener = MyAccelerometerListener(::accelerometerCallback)
        try {
            val locationRequest = LocationRequest.create()?.apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper())
            sensorManager.registerListener(accelerometerListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        catch(e : SecurityException ) {
            //TO-DO handle lack of permissions
        }
    }

    protected fun stopTrip() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        sensorManager.unregisterListener(accelerometerListener)
        tripService.save(generateTrip())
    }

    protected fun accelerometerCallback(accelerationAxes : Triple<Float, Float, Float>) {
        val (x, y, z) = accelerationAxes
        if(!violationContinous && checkViolationConditions(accelerationAxes)) {
            createViolation(z, x)
        }
        updateViolationContinous(accelerationAxes)

    }

    protected fun createViolation(linearGForce : Float, lateralGForce : Float) {
        val lastLocation = locations.last()
        var violation = Violation(
            speed = lastLocation.speed.toDouble(),
            lateralGForce = linearGForce.toDouble(),
            linearGForce = lateralGForce.toDouble(),
            latitude = lastLocation.latitude,
            longitude = lastLocation.longitude,
            timestamp = lastLocation.time,
            correspondingVTripId = 0
        )
        violations.add(violation)
    }

    protected fun checkViolationConditions(accelerationAxes: Triple<Float, Float, Float>) : Boolean {
        val (x, y, z) = accelerationAxes
        return (z > 5 || z < -5)
    }

    protected fun updateViolationContinous(accelerationAxes: Triple<Float, Float, Float>) {
        val (x, y, z) = accelerationAxes
        if(checkViolationConditions(accelerationAxes) && !violationContinous) {
            violationContinous = true
        }
        else if(!checkViolationConditions(accelerationAxes) && violationContinous) {
            violationContinous = false
        }
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


    protected lateinit var locationCallback: LocationCallback

    private lateinit var  accelerometerListener: SensorEventListener

    class MyAccelerometerListener(val callback: KFunction1<@ParameterName(name = "coordinates") Triple<Float, Float, Float>, Unit>) : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, acc: Int) {}
        override fun onSensorChanged(event: SensorEvent) {
            callback(Triple(event.values[0], event.values[1], event.values[2]))
    }
    }
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
