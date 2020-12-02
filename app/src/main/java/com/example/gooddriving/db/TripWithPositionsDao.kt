package com.example.gooddriving.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface TripWithPositionsDao {
    @Transaction
    @Query("SELECT * FROM trip_table")
    fun getTripsWithPositions() : List<TripWithPositions>

    @Transaction
    @Query("SELECT * FROM trip_table WHERE tripId = :trip_id")
    fun getSingleTripWithPositions(trip_id: Long) : TripWithPositions
}