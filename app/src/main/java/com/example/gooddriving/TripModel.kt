package com.example.gooddriving

import android.location.Location
import com.example.gooddriving.db.Position
import com.example.gooddriving.db.Violation
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

class TripModel (
    var locationList: MutableList<Location>,
    var violationList: List<Violation>,
    var grade: Double,
    var dateOfTrip: String,
    var distanceCovered: Double,
    var timeElapsed: Long,
    var maxSpeed: Double,
    var avgSpeed: Double
) {

    @ExperimentalTime
    var timeElapsedConverted: Duration = timeElapsed.toDuration(TimeUnit.MINUTES)

}