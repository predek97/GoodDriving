package com.example.gooddriving.db

import androidx.room.Embedded
import androidx.room.Relation

data class TripWithPositions(
    @Embedded val trip : Trip,
    @Relation(
        parentColumn = "tripId",
        entityColumn = "correspondingTripId"
    )
    val positions: List<Position>
)