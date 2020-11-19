package com.example.gooddriving.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "position_table")
data class Position(
    var latitude : Double = 0.0,
    var longitude: Double = 0.0,
    var timestamp : Long = 0,
    var correspondingTripId : Int
)
{
    @PrimaryKey(autoGenerate = true)
    val positionId: Int = 0
}