package com.example.gooddriving.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Trip::class, Violation::class, Position::class], version = 1, exportSchema = false)
abstract class TripRoomDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun violationDao(): ViolationDao
    abstract fun positionDao(): PositionDao
    abstract fun tripWithPositionsDao(): TripWithPositionsDao
    abstract fun tripWithViolationsDao(): TripWithViolationsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TripRoomDatabase? = null

        fun getDatabase(context: Context): TripRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripRoomDatabase::class.java,
                    "trips_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}