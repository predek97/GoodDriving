package com.example.gooddriving.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface TripWithViolationsDao {
    @Transaction
    @Query("SELECT * FROM trip_table")
    fun getTripsWithViolations() : List<TripWithViolations>

    @Transaction
    @Query("SELECT * FROM trip_table WHERE tripId = :trip_id")
    fun getSingleTripWithViolations(trip_id: Long) : TripWithViolations
}