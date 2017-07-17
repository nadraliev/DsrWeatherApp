package soutvoid.com.DsrWeatherApp.ui.util

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

fun LocationListener(listener: (location: Location) -> Unit): LocationListener = object : LocationListener {
    override fun onLocationChanged(p0: Location?){
        p0?.let(listener)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}
}