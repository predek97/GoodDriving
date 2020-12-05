package com.example.gooddriving.tracking

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import com.example.gooddriving.db.Violation

class AccelerometerController constructor(var locations : ArrayList<Location>, var violations : ArrayList<Violation>) :
    SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor, acc: Int) {}
    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val accelerationAxes = Triple(x, y, z)
        if(!violationContinous && checkViolationConditions(accelerationAxes)) {
            createViolation(z, x)
        }
        updateViolationContinous(accelerationAxes)
    }
    protected var violationContinous = false

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
}