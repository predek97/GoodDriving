package com.example.gooddriving.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "trip_table")
data class Trip(
    var grade: Double = 0.0,
    var dateOfTrip: String = "",
    var distanceCovered: Double = 0.0,
    var timeElapsed: Long = 0,
    var maxSpeed: Double = 0.0,
    var avgSpeed: Double = 0.0
){
    @PrimaryKey(autoGenerate = true)
    var tripId: Long = 0
}