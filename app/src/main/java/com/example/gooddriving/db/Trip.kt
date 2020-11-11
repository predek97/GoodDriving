package com.example.gooddriving.db

import android.location.Location
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gooddriving.Violation

@Entity(tableName = "trip_table")
data class Trip (
    @PrimaryKey(autoGenerate = true) val id : Int,
    @Embedded val LocationList : List<Location>,
    @Embedded val ViolationList: List<Violation>,
    val grade : Float
)