package com.example.gooddriving.converters

import android.location.Location
import android.location.LocationManager.GPS_PROVIDER
import com.example.gooddriving.db.Position

class PositionToLocationConverter {
    fun convert(position: Position) : Location
    {
        var location : Location = Location(GPS_PROVIDER)
        location.latitude = position.latitude
        location.longitude = position.longitude
        location.time = position.timestamp
        return location
    }
}