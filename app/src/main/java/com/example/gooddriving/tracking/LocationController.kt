package com.example.gooddriving.tracking

import android.location.Location
import android.location.LocationListener
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationController constructor(var locations : ArrayList<Location>, var distanceCovered : Array<Double>) : LocationCallback() {
    private val subscribers = mutableListOf<SpeedSubscriber>()

    interface SpeedSubscriber {
        fun notifyNewSpeed(newSpeed : Double)
    }

    fun addSubscriber(newSubscriber: SpeedSubscriber) {
        subscribers.add(newSubscriber)
    }

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
            for(subscriber in subscribers) {
                subscriber.notifyNewSpeed(speed)
            }
        }
    }
}
