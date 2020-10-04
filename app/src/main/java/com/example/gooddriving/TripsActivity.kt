package com.example.gooddriving

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class TripsActivity : BasicLayoutActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

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
}
