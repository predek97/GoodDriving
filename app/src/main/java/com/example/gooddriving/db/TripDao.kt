package com.example.gooddriving.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface TripDao {
    @Query("SELECT * FROM trip_table")
    fun getAllTrips(): List<Trip>

    @Insert
    fun insertTrip(trip: Trip): Long

    @Query("DELETE FROM trip_table")
    fun deleteAll()
}