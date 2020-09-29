package com.example.gooddriving

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
        }
    }
}
