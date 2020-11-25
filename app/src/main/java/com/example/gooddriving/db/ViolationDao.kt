package com.example.gooddriving.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ViolationDao {
    @Query("SELECT * FROM violation_table")
    fun getAllViolations(): List<Violation>

    @Query("SELECT * FROM violation_table WHERE correspondingVTripId=:tripId")
    fun getAllTripViolations(tripId : Int) : List<Violation>

    @Insert
    fun insertViolation(violation: Violation)

    @Insert
    fun insertMultipleViolations(vararg violation: Violation)

    @Query("DELETE FROM violation_table WHERE violationId = :vioId")
    fun deleteViolation(vioId : Long)
}