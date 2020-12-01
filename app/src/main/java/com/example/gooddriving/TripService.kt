package com.example.gooddriving

import android.content.Context
import android.location.Location
import android.location.LocationManager.GPS_PROVIDER
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

    fun getAllTripsFromDB() : MutableList<TripModel>{
        var tripsWithPosFromDB : List<TripWithPositions>? = null
        var tripsWithVioFromDB: List<TripWithViolations>? = null
        var task = GlobalScope.async {
            tripsWithPosFromDB = tripWithPosDao.getTripsWithPositions()
            tripsWithVioFromDB = tripWithVioDao.getTripsWithViolations()
        }
        runBlocking {
            task.await()
        }
        var tripsToReturn : MutableList<TripModel> = mutableListOf<TripModel>()
        for ((i, trip) in tripsWithPosFromDB!!.withIndex()){
            var locList = mutableListOf<Location>()
            for (position in trip.positions){
                locList.add(positionToLocationConverter.convert(position))
            }
            tripsToReturn.add(TripModel(locList, tripsWithVioFromDB!![i].violations, trip.trip.grade, trip.trip.dateOfTrip, trip.trip.distanceCovered, trip.trip.timeElapsed, trip.trip.maxSpeed, trip.trip.avgSpeed, tripIdFromDb = trip.trip.tripId))
        }
        return tripsToReturn
    }

    fun getTrip(tripId: Long) : TripModel{
        var tripWithPosFromDB : TripWithPositions? = null
        var tripWithVioFromDB: TripWithViolations? = null
        var task = GlobalScope.async {
            tripWithPosFromDB = tripWithPosDao.getSingleTripWithPositions(tripId)
            tripWithVioFromDB = tripWithVioDao.getSingleTripWithViolations(tripId)
        }
        runBlocking {
            task.await()
        }

        var locList = mutableListOf<Location>()
        for (position in tripWithPosFromDB!!.positions){
            locList.add(positionToLocationConverter.convert(position))
        }
        return TripModel(locList, tripWithVioFromDB!!.violations, tripWithPosFromDB!!.trip.grade, tripWithPosFromDB!!.trip.dateOfTrip, tripWithPosFromDB!!.trip.distanceCovered, tripWithPosFromDB!!.trip.timeElapsed, tripWithPosFromDB!!.trip.maxSpeed, tripWithPosFromDB!!.trip.avgSpeed, tripIdFromDb = tripWithPosFromDB!!.trip.tripId)
    }

    fun save(recordedTrip: TripModel){
        var task = GlobalScope.async {
            var tripId: Long = 0
            var tripToInsert = Trip(grade = recordedTrip.grade, dateOfTrip = recordedTrip.dateOfTrip, distanceCovered = recordedTrip.distanceCovered, timeElapsed = recordedTrip.timeElapsed, maxSpeed = recordedTrip.maxSpeed, avgSpeed = recordedTrip.avgSpeed)
            tripId = tripDao.insertTrip(tripToInsert)
            var tempPosition: Position
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