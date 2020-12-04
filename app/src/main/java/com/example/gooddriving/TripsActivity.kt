package com.example.gooddriving

import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.gooddriving.db.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*

class TripsActivity : BasicLayoutActivity() {

    var db: TripRoomDatabase? = null
    var tripDao : TripDao? = null
    var positionDao : PositionDao? = null
    var violationDao: ViolationDao? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)
        this.title = "Trip summary"
        //db = TripRoomDatabase.getDatabase(this)
        var tripService = TripService(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //addToDb() //- dane już dodane
        //Thread.sleep(2000)
        //removeFromDb()

        //textMessage = findViewById(R.id.message)
        navView.selectedItemId = R.id.navigation_trips
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        var listOfTripsLayout : LinearLayout = findViewById(R.id.list_of_trips)

        var listOfTrips = tripService.getAllTripsFromDB()

/*
        var loc1 = Location(GPS_PROVIDER)
        loc1.latitude = 54.60505924199695
        loc1.longitude = 18.34790233359333
        loc1.time = 0
        var loc2 = Location(GPS_PROVIDER)
        loc2.latitude = 54.60585017805469
        loc2.longitude = 18.34271321297222
        loc2.time = 0
        var loc3 = Location(GPS_PROVIDER)
        loc3.latitude = 54.60423313695798
        loc3.longitude = 18.248823522264587
        loc3.time = 0
        var vio1 = Violation(51.2, 3.4, 5.5, 54.60585017805469, 18.34271321297222, 0, 0)
        var listLocation = mutableListOf(loc1, loc2, loc3)
        var listViolation = listOf(vio1)
        var testTripModel = TripModel(listLocation,listViolation, 4.5, "2020-12-01", 3.3, 1200000, 76.5, 43.5, null)
        tripService.save(testTripModel)
*/
        for ((i, singleTrip) in listOfTrips.withIndex())
        {
            var inflatedView = View.inflate(listOfTripsLayout.context, R.layout.trip_list_element, listOfTripsLayout)
            listOfTripsLayout.getChildAt(i).findViewById<TextView>(R.id.trip_expanded).text = "Trip #" + singleTrip.tripIdFromDb.toString() + " at " + singleTrip.dateOfTrip
            listOfTripsLayout.getChildAt(i).findViewById<Button>(R.id.trip_button).setOnClickListener {
                Toast.makeText(this, "Trip info expanded", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, TripActivity::class.java).putExtra(TripActivity.tripId, singleTrip.tripIdFromDb.toString())
                startActivity(intent)
            }
        }



    }


    fun addToDb(){
        var trip1 = Trip(grade = 8.9, dateOfTrip = "2020-11-30", distanceCovered = 15.6, timeElapsed = 900000, maxSpeed = 94.3, avgSpeed = 31.2)
        var trip2 = Trip(grade = 3.9, dateOfTrip = "2020-11-30", distanceCovered = 6.4, timeElapsed = 900000, maxSpeed = 74.3, avgSpeed = 31.2)
        var trip3 = Trip(grade = 9.4, dateOfTrip = "2020-11-30", distanceCovered = 153.6, timeElapsed = 900000, maxSpeed = 124.3, avgSpeed = 31.2)
        var pos1 = Position(latitude = 54.563522314691056, longitude = 18.40109839933261, timestamp = 12345, correspondingTripId = 0)
        var pos2 = Position(54.56415571019818, 18.399975607374525, timestamp = 0, correspondingTripId = 0)
        var pos3 = Position(54.56651326234868, 18.40216049991854, timestamp = 0, correspondingTripId = 0)
        var pos4 = Position(54.57103445481589, 18.388201464222984, timestamp = 0, correspondingTripId = 0)
        var pos5 = Position(54.57437664837222, 18.38119160025676, timestamp = 0, correspondingTripId = 0)
        var pos6 = Position(54.58477087728291, 18.363409001991673, timestamp = 0, correspondingTripId = 0)
        var pos7 = Position(54.60505924199695, 18.34790233359333, timestamp = 0, correspondingTripId = 0)
        var pos8 = Position(54.60585017805469, 18.34271321297222, timestamp = 0, correspondingTripId = 0)
        var pos9 = Position(54.60423313695798, 18.248823522264587, timestamp = 0, correspondingTripId = 0)
        var vio1 = Violation(speed = 60.5, lateralGForce = 1.2, linearGForce = 2.3, timestamp = 0, latitude = 54.56715532162732, longitude = 18.400209281470914, correspondingVTripId = 0)
        var vio2 = Violation(speed = 91.3, lateralGForce = 1.2, linearGForce = 2.3, timestamp = 0, latitude = 54.58249343307359, longitude = 18.36719312637782, correspondingVTripId = 0)
        var vio3 = Violation(speed = 114.5, lateralGForce = 1.2, linearGForce = 2.3, timestamp = 0, latitude = 54.60451501071257, longitude = 18.309545986925972, correspondingVTripId = 0)
        var dataTp: List<TripWithPositions>
        var dataTv: List<TripWithViolations>
        db = TripRoomDatabase.getDatabase(this)
        tripDao = db!!.tripDao()
        positionDao = db!!.positionDao()
        violationDao = db!!.violationDao()

        var task = GlobalScope.async {
            var tripId = tripDao!!.insertTrip(trip1)
            pos1.correspondingTripId = tripId
            pos2.correspondingTripId = tripId
            pos3.correspondingTripId = tripId
            pos4.correspondingTripId = tripId
            pos5.correspondingTripId = tripId
            positionDao!!.insertMultiplePositions(pos1, pos2, pos3, pos4, pos5)
            vio1.correspondingVTripId = tripId
            violationDao!!.insertViolation(vio1)

            tripId = tripDao!!.insertTrip(trip2)
            pos3.correspondingTripId = tripId
            pos4.correspondingTripId = tripId
            pos5.correspondingTripId = tripId
            pos6.correspondingTripId = tripId
            pos7.correspondingTripId = tripId
            positionDao!!.insertMultiplePositions(pos3, pos4, pos5, pos6, pos7)
            vio2.correspondingVTripId = tripId
            violationDao!!.insertViolation(vio2)

            tripId = tripDao!!.insertTrip(trip3)
            pos5.correspondingTripId = tripId
            pos6.correspondingTripId = tripId
            pos7.correspondingTripId = tripId
            pos8.correspondingTripId = tripId
            pos9.correspondingTripId = tripId
            positionDao!!.insertMultiplePositions(pos5, pos6, pos7, pos8, pos9)
            vio2.correspondingVTripId = tripId
            vio3.correspondingVTripId = tripId
            violationDao!!.insertMultipleViolations(vio2, vio3)

        }
        runBlocking {
            task.await()
        }

    }

    fun removeFromDb(){
        db = TripRoomDatabase.getDatabase(this)
        tripDao = db!!.tripDao()
        positionDao = db!!.positionDao()
        var task = GlobalScope.async {
            positionDao!!.deleteAll()
            tripDao!!.deleteAll()
        }
        runBlocking {
            task.await()
        }
    }
}
