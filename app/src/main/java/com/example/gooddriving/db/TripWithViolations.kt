package com.example.gooddriving.db

import androidx.room.Embedded
import androidx.room.Relation

data class TripWithViolations(
    @Embedded val trip : Trip,
    @Relation(
        parentColumn = "tripId",
        entityColumn = "correspondingVTripId"
    )
    val violations: List<Violation>
)