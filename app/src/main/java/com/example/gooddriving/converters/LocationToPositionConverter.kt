package com.example.gooddriving.converters

import android.location.Location
import com.example.gooddriving.db.Position

class LocationToPositionConverter {
    fun convert(location: Location) : Position
    {
        var position : Position = Position(0.0,0.0,0,0)
        position.latitude = location.latitude
        position.longitude = location.longitude
        position.timestamp = location.time
        return position
    }
}