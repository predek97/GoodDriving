package com.example.gooddriving

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.companion.CompanionDeviceManager
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


open class BasicLayoutActivity : AppCompatActivity() {
    protected lateinit var textMessage: TextView
    protected val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_trips -> {
                val intent = Intent(this, TripsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
