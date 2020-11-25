package com.example.gooddriving.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "trip_table")
data class Trip(
    var grade: Double = 0.0
){
    @PrimaryKey(autoGenerate = true)
    var tripId: Int = 0
}