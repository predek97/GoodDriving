package com.example.gooddriving.db

import androidx.room.Dao
import androidx.room.Query


@Dao
interface TripDao {
    @Query("SELECT * FROM trip_table")
    fun getAllTrips(): List<Trip>

}