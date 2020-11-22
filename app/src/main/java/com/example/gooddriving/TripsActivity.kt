package com.example.gooddriving

import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.gooddriving.db.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TripsActivity : BasicLayoutActivity() {

    var db: TripRoomDatabase? = null
    var tripDao : TripDao? = null
    var positionDao : PositionDao? = null
    var tripWithPosDao : TripWithPositionsDao? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)
        db = TripRoomDatabase.getDatabase(this)
        // code to test db
        /*
        var trip = Trip(grade = 8.8)
        var data: List<Trip>
        db = TripRoomDatabase.getDatabase(this)
        tripDao = db?.tripDao()
        GlobalScope.launch {
            tripDao?.insertTrip(trip)
            data = db?.tripDao()!!.getAllTrips()

            data?.forEach {
                println(it)
            }
            val textField: TextView = findViewById(R.id.dbTest)
            textField.setText(data[0].grade.toString())
        }

        */

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        addToDb()
        Thread.sleep(2000)
        removeFromDb()

        textMessage = findViewById(R.id.message)
        navView.selectedItemId = R.id.navigation_trips
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

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
        var trip = Trip(grade = 8.9)
        var pos1 = Position(latitude = 3.5, longitude = 4.5, timestamp = 12345, correspondingTripId = trip.tripId)
        var pos2 = Position(latitude = 3.5, longitude = 4.8, timestamp = 12346, correspondingTripId = trip.tripId)
        var data: List<TripWithPositions>
        db = TripRoomDatabase.getDatabase(this)
        tripDao = db!!.tripDao()
        positionDao = db!!.positionDao()
        GlobalScope.launch {
            var tripId = tripDao!!.insertTrip(trip)
            pos1.correspondingTripId = tripId.toInt()
            pos2.correspondingTripId = tripId.toInt()
            positionDao!!.insertPosition(pos1)
            positionDao!!.insertPosition(pos2)
            tripWithPosDao = db!!.tripWithPositionsDao()
            data = tripWithPosDao!!.getTripsWithPositions()
            data.forEach{
                println(it.trip.grade)
                println(it.positions.size)
            }
            val textField: TextView = findViewById(R.id.dbTest)
            textField.text = data[0].trip.grade.toString()
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
