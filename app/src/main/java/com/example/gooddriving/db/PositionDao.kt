package com.example.gooddriving.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface PositionDao {
    @Query("SELECT * FROM position_table")
    fun getAllPositions(): List<Position>

    @Query("SELECT * FROM position_table WHERE correspondingTripId=:tripId")
    fun getAllTripPositions(tripId : Int) : List<Position>

    @Insert
    fun insertPosition(position : Position)

    @Insert
    fun insertMultiplePositions(vararg position : Position)

    @Query("DELETE FROM position_table")
    fun deleteAll()
}