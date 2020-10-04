package com.example.gooddriving

import android.app.ActionBar
import android.os.Bundle
import android.widget.TextView

class TripActivity : BasicLayoutActivity() {

    companion object {
        const val tripName = "default trip name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_trip)

        val tripNameToDisplay = intent.getStringExtra(tripName)

        var textView: TextView = findViewById(R.id.message1)
        textView.setText(tripNameToDisplay)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}