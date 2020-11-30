package com.example.gooddriving

import android.content.Context
import com.example.gooddriving.converters.LocationToPositionConverter
import com.example.gooddriving.converters.PositionToLocationConverter
import com.example.gooddriving.db.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class TripService (var context: Context){
    var db = TripRoomDatabase.getDatabase(context)
    var tripDao = db.tripDao()
    var positionDao = db.positionDao()
    var violationDao = db.violationDao()
    var tripWithPosDao = db.tripWithPositionsDao()
    var tripWithVioDao = db.tripWithViolationsDao()
    val locationToPositionConverter = LocationToPositionConverter()
    var positionToLocationConverter = PositionToLocationConverter()

    fun getAllTripsFromDB() { //TODO
        var tripsToReturn : MutableList<TripModel> = mutableListOf()
        val tripsWithPosFromDB = tripWithPosDao.getTripsWithPositions()
        val tripsWithVioFromDB = tripWithVioDao.getTripsWithViolations()
        for ((i, trip) in tripsWithPosFromDB.withIndex()){

        }
    }

    fun getTrip(tripId: Long) : TripModel{ //TODO
        var tripToReturn : TripModel? = null
        return tripToReturn!!
    }

    fun save(recordedTrip: TripModel){
        var task = GlobalScope.async {
            var tripId: Long = 0
            var tripToInsert = Trip(grade = recordedTrip.grade, dateOfTrip = recordedTrip.dateOfTrip, distanceCovered = recordedTrip.distanceCovered, timeElapsed = recordedTrip.timeElapsed, maxSpeed = recordedTrip.maxSpeed, avgSpeed = recordedTrip.avgSpeed)
            tripId = tripDao.insertTrip(tripToInsert)
            var tempPosition : Position = Position(0.0,0.0,0,0)
            for (location in recordedTrip.locationList){
                tempPosition = locationToPositionConverter.convert(location)
                tempPosition.correspondingTripId = tripId
                positionDao.insertPosition(tempPosition)
            }
            for (vio in recordedTrip.violationList){
                vio.correspondingVTripId = tripId
                violationDao.insertViolation(vio)
            }
        }
        runBlocking {
            task.await()
        }

    }


}