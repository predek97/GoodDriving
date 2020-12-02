

import android.content.Context
import android.location.Location
import android.location.LocationListener
import com.google.android.gms.location.LocationServices

class TrackingController {
    protected var locations : ArrayList<Location> = ArrayList()
    fun onCreate(context : Context) {
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val mSettingsClient = LocationServices.getSettingsClient(context);
        while(true) {

            //locations.add()
        }
    }



}
