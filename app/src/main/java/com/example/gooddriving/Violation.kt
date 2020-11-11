package com.example.gooddriving

import android.location.Location
import android.location.LocationManager

class Violation{
    val speed : Double = 0.0
    val rpm: Short = 0
    val oilTemperature: Short = 0
    val coolantTemperature: Short = 0
    val throttlePosition: Short = 0
    val lateralGForce: Double = 0.0
    val linearGForce: Double = 0.0
    val place: Location = Location(LocationManager.GPS_PROVIDER)
}