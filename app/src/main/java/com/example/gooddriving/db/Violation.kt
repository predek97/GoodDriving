package com.example.gooddriving.db

import android.location.Location
import android.location.LocationManager
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "violation_table")
data class Violation(
    var speed : Double = 0.0,
    //var rpm: Short = 0,
    //var oilTemperature: Short = 0,
    //var coolantTemperature: Short = 0,
    //var throttlePosition: Short = 0,
    var lateralGForce: Double = 0.0,
    var linearGForce: Double = 0.0,
    var latitude : Double = 0.0,
    var longitude: Double = 0.0,
    var timestamp : Long = 0,
    var correspondingVTripId : Long
){
    @PrimaryKey(autoGenerate = true)
    var violationId: Long = 0
}