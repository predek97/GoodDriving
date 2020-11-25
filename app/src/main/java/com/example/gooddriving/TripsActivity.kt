package com.example.gooddriving

import android.content.Intent
import android.location.Location
import android.location.LocationManager
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
    var tripWithPosDao : TripWithPositionsDao? = null
    var tripWithVioDao : TripWithViolationsDao? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)
        db = TripRoomDatabase.getDatabase(this)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //addToDb() - dane już dodane
        Thread.sleep(2000)
        //removeFromDb()

        textMessage = findViewById(R.id.message)
        navView.selectedItemId = R.id.navigation_trips
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        var listOfTripsLayout : LinearLayout = findViewById(R.id.list_of_trips)
        tripDao = db!!.tripDao()
        var listOfTrips : List<Trip>? = null
        var task = GlobalScope.async {
            listOfTrips = tripDao!!.getAllTrips()
        }
        runBlocking{
            task.await()
        }


        for ((i, singleTrip) in listOfTrips!!.withIndex())
        {
            var inflatedView = View.inflate(listOfTripsLayout.context, R.layout.trip_list_element, listOfTripsLayout)
            listOfTripsLayout.getChildAt(i+4).findViewById<TextView>(R.id.trip_expanded).text = singleTrip.grade.toString() // i + 4 because we have four mock objects, not from db
        }


        val tripOneButton: Button = findViewById(R.id.trip1_button)
        tripOneButton.setOnClickListener {
            Toast.makeText(this, "Trip one info expanded", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TripActivity::class.java).putExtra(TripActivity.tripName, "Trip one")
            startActivity(intent)
        }

        val tripTwoButton: Button = findViewById(R.id.trip2_button)
        tripTwoButton.setOnClickListener {
            Toast.makeText(this, "Trip two info expanded", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TripActivity::class.java).putExtra(TripActivity.tripName, "Trip two")
            startActivity(intent)
        }

        val tripThreeButton: Button = findViewById(R.id.trip3_button)
        tripThreeButton.setOnClickListener {
            Toast.makeText(this, "Trip three info expanded", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TripActivity::class.java).putExtra(TripActivity.tripName, "Trip three")
            startActivity(intent)
        }

        val tripFourButton: Button = findViewById(R.id.trip4_button)
        tripFourButton.setOnClickListener {
            Toast.makeText(this, "Trip four info expanded", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TripActivity::class.java).putExtra(TripActivity.tripName, "Trip four")
            startActivity(intent)
        }


    }


    fun addToDb(){
        var trip1 = Trip(grade = 8.9)
        var trip2 = Trip(grade = 3.9)
        var trip3 = Trip(grade = 9.4)
        var pos1 = Position(latitude = 54.563522314691056, longitude = 18.40109839933261, timestamp = 12345, correspondingTripId = 0)
        var pos2 = Position(54.56415571019818, 18.399975607374525, timestamp = 0, correspondingTripId = 0)
        var pos3 = Position(54.56651326234868, 18.40216049991854, timestamp = 0, correspondingTripId = 0)
        var pos4 = Position(54.57103445481589, 18.388201464222984, timestamp = 0, correspondingTripId = 0)
        var pos5 = Position(54.57437664837222, 18.38119160025676, timestamp = 0, correspondingTripId = 0)
        var pos6 = Position(54.58477087728291, 18.363409001991673, timestamp = 0, correspondingTripId = 0)
        var pos7 = Position(54.60505924199695, 18.34790233359333, timestamp = 0, correspondingTripId = 0)
        var pos8 = Position(54.60585017805469, 18.34271321297222, timestamp = 0, correspondingTripId = 0)
        var pos9 = Position(54.60423313695798, 18.248823522264587, timestamp = 0, correspondingTripId = 0)
        var vio1 = Violation(speed = 60.5, rpm = 3500, oilTemperature = 90, coolantTemperature = 50, lateralGForce = 1.2, linearGForce = 2.3, timestamp = 0, latitude = 54.56715532162732, longitude = 18.400209281470914, correspondingVTripId = 0)
        var vio2 = Violation(speed = 91.3, rpm = 2100, oilTemperature = 90, coolantTemperature = 50, lateralGForce = 1.2, linearGForce = 2.3, timestamp = 0, latitude = 54.58249343307359, longitude = 18.36719312637782, correspondingVTripId = 0)
        var vio3 = Violation(speed = 114.5, rpm = 2800, oilTemperature = 90, coolantTemperature = 50, lateralGForce = 1.2, linearGForce = 2.3, timestamp = 0, latitude = 54.60451501071257, longitude = 18.309545986925972, correspondingVTripId = 0)
        var dataTp: List<TripWithPositions>
        var dataTv: List<TripWithViolations>
        db = TripRoomDatabase.getDatabase(this)
        tripDao = db!!.tripDao()
        positionDao = db!!.positionDao()
        violationDao = db!!.violationDao()

        GlobalScope.launch {
            var tripId = tripDao!!.insertTrip(trip1)
            pos1.correspondingTripId = tripId.toInt()
            pos2.correspondingTripId = tripId.toInt()
            pos3.correspondingTripId = tripId.toInt()
            pos4.correspondingTripId = tripId.toInt()
            pos5.correspondingTripId = tripId.toInt()
            positionDao!!.insertMultiplePositions(pos1, pos2, pos3, pos4, pos5)
            vio1.correspondingVTripId = tripId.toInt()
            vio2.correspondingVTripId = tripId.toInt()
            violationDao!!.insertMultipleViolations(vio1, vio2)

            tripId = tripDao!!.insertTrip(trip2)
            pos3.correspondingTripId = tripId.toInt()
            pos4.correspondingTripId = tripId.toInt()
            pos5.correspondingTripId = tripId.toInt()
            pos6.correspondingTripId = tripId.toInt()
            pos7.correspondingTripId = tripId.toInt()
            positionDao!!.insertMultiplePositions(pos3, pos4, pos5, pos6, pos7)
            vio2.correspondingVTripId = tripId.toInt()
            violationDao!!.insertViolation(vio2)

            tripId = tripDao!!.insertTrip(trip3)
            pos5.correspondingTripId = tripId.toInt()
            pos6.correspondingTripId = tripId.toInt()
            pos7.correspondingTripId = tripId.toInt()
            pos8.correspondingTripId = tripId.toInt()
            pos9.correspondingTripId = tripId.toInt()
            positionDao!!.insertMultiplePositions(pos5, pos6, pos7, pos8, pos9)
            vio2.correspondingVTripId = tripId.toInt()
            vio3.correspondingVTripId = tripId.toInt()
            violationDao!!.insertMultipleViolations(vio2, vio3)

        }

    }

    fun removeFromDb(){
        db = TripRoomDatabase.getDatabase(this)
        tripDao = db!!.tripDao()
        positionDao = db!!.positionDao()
        GlobalScope.launch {
            positionDao!!.deleteAll()
            tripDao!!.deleteAll()
        }
    }
}
